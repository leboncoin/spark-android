package com.adevinta.spark.catalog.baselineprofile

import android.content.Intent
import androidx.benchmark.macro.junit4.BaselineProfileRule
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.filters.LargeTest
import androidx.test.platform.app.InstrumentationRegistry
import androidx.test.uiautomator.By
import androidx.test.uiautomator.Direction
import androidx.test.uiautomator.Until
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

/**
 * Generates baseline and startup profiles for the Spark catalog.
 *
 * - [startup]: minimal cold-start journey → included in startup profile (DEX layout optimisation)
 * - [componentJourneys]: launches BenchmarkActivity showing all components → baseline profile only
 * - [iconJourneys]: launches BenchmarkIconsActivity showing all icons → baseline profile only
 *
 * Run with:
 * ```
 * ./gradlew :catalog:generateReleaseBaselineProfile
 * ```
 *
 * When using this class to generate a baseline profile, only API 33+ or rooted API 28+ are supported.
 **/
@RunWith(AndroidJUnit4::class)
@LargeTest
internal class BaselineProfileGenerator {

    @get:Rule
    internal val rule: BaselineProfileRule = BaselineProfileRule()

    private val packageName: String
        get() = InstrumentationRegistry.getArguments().getString("targetAppId")
            ?: throw Exception("targetAppId not passed as instrumentation runner arg")

    @Test
    internal fun startup() {
        rule.collect(
            packageName = packageName,
            includeInStartupProfile = true,
        ) {
            pressHome()
            startActivityAndWait()
        }
    }

    @Test
    internal fun componentJourneys() {
        rule.collect(
            packageName = packageName,
            includeInStartupProfile = false,
        ) {
            startActivityAndWait(Intent().apply {
                setPackage(packageName)
                setAction("com.adevinta.spark.catalog.BENCHMARK")
            })

            device.wait(Until.hasObject(By.res("benchmark_list")), 5_000)

            val list = device.findObject(By.res("benchmark_list")) ?: return@collect
            list.setGestureMargin(device.displayWidth / 4)

            repeat(5) {
                list.scroll(Direction.DOWN, 2f)
                device.waitForIdle()
            }
            repeat(5) {
                list.scroll(Direction.UP, 2f)
                device.waitForIdle()
            }
        }
    }

    @Test
    internal fun iconJourneys() {
        rule.collect(
            packageName = packageName,
            includeInStartupProfile = false,
        ) {
            startActivityAndWait(Intent().apply {
                setPackage(packageName)
                setAction("com.adevinta.spark.catalog.BENCHMARK_ICONS")
            })

            device.wait(Until.hasObject(By.res("benchmark_icons_grid")), 5_000)

            val grid = device.findObject(By.res("benchmark_icons_grid")) ?: return@collect
            grid.setGestureMargin(device.displayWidth / 4)

            // Scroll to the bottom to render all icons
            var canScroll = true
            while (canScroll) {
                canScroll = grid.scroll(Direction.DOWN, 2f)
                device.waitForIdle()
            }
        }
    }
}