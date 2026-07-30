package com.adevinta.spark.catalog.baselineprofile

import android.content.Intent
import androidx.benchmark.macro.CompilationMode
import androidx.benchmark.macro.ExperimentalMetricApi
import androidx.benchmark.macro.FrameTimingMetric
import androidx.benchmark.macro.MacrobenchmarkScope
import androidx.benchmark.macro.MemoryUsageMetric
import androidx.benchmark.macro.StartupMode
import androidx.benchmark.macro.TraceSectionMetric
import androidx.benchmark.macro.junit4.MacrobenchmarkRule
import androidx.test.platform.app.InstrumentationRegistry
import androidx.test.uiautomator.By
import androidx.test.uiautomator.Until
import org.junit.Rule

/**
 * Base for per-component scenario benchmarks.
 *
 * Each component has its own subclass with one `@Test` per interaction worth measuring. A scenario
 * is a self-contained composable in the catalog app (see BenchmarkScenarios.kt), launched via the
 * `scenario_id` extra and wrapped in a `Spark::<id>` trace section.
 *
 * Self-driving scenarios run an internal animation loop, so [measureScenario] holds the window for a
 * fixed duration to capture a stable number of frames. Interactive scenarios pass an [interactionBlock]
 * that drives real input via UiAutomator.
 */
internal abstract class ScenarioBenchmark {

    @get:Rule
    val rule = MacrobenchmarkRule()

    private val packageName: String
        get() = InstrumentationRegistry.getArguments().getString("targetAppId")
            ?: throw Exception("targetAppId not passed as instrumentation runner arg")

    /**
     * @param scenarioId id registered in the catalog app, also the `Spark::<id>` trace section.
     * @param measureFrames whether to record [FrameTimingMetric]. Disable for static scenarios: a
     *   screen that neither animates nor is interacted with produces no frames in the window, which
     *   makes [FrameTimingMetric] throw. Composition and trace metrics still capture the cost.
     * @param interactionBlock real input driven via UiAutomator. If null, the scenario is expected to
     *   self-drive an animation and the window is held for a fixed duration.
     */
    @OptIn(ExperimentalMetricApi::class)
    protected fun measureScenario(
        scenarioId: String,
        measureFrames: Boolean = true,
        interactionBlock: (MacrobenchmarkScope.() -> Unit)? = null,
    ) = rule.measureRepeated(
        packageName = packageName,
        metrics = buildList {
            if (measureFrames) add(FrameTimingMetric())
            add(MemoryUsageMetric(MemoryUsageMetric.Mode.Last))
            add(TraceSectionMetric("Spark::$scenarioId", TraceSectionMetric.Mode.Sum))
            add(CompositionMetric("Spark::$scenarioId"))
        },
        compilationMode = CompilationMode.Partial(),
        startupMode = StartupMode.WARM,
        iterations = 5,
        setupBlock = { pressHome() },
        measureBlock = {
            startActivityAndWait(
                Intent().apply {
                    setPackage(packageName)
                    setAction("com.adevinta.spark.catalog.BENCHMARK")
                    putExtra("scenario_id", scenarioId)
                },
            )
            device.wait(Until.hasObject(By.res("benchmark_scenario")), 5_000)
            device.waitForIdle()

            if (interactionBlock != null) {
                interactionBlock()
            } else if (measureFrames) {
                // Self-driving scenario: hold the window so its animation loop produces frames.
                Thread.sleep(SELF_DRIVING_WINDOW_MS)
            }
        },
    )

    companion object {
        const val SELF_DRIVING_WINDOW_MS = 3_000L
    }
}
