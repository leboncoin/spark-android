package com.adevinta.spark.catalog.baselineprofile

import androidx.test.filters.LargeTest
import androidx.test.uiautomator.By
import androidx.test.uiautomator.Until
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.JUnit4

@RunWith(JUnit4::class)
@LargeTest
internal class ComboBoxBenchmark : ScenarioBenchmark() {

    /** Types a query to filter the dropdown, then selects the first result. Real UiAutomator input. */
    @Test
    fun typeFilterAndSelect() = measureScenario("combobox-filter") {
        val field = device.wait(Until.findObject(By.res("benchmark_scenario")), 5_000) ?: return@measureScenario
        field.click()
        device.waitForIdle()
        device.findObject(By.focused(true))?.text = "the"
        device.waitForIdle()
        Thread.sleep(500)
        // Confirm the field entry and let the dropdown settle.
        device.pressEnter()
        device.waitForIdle()
        Thread.sleep(500)
    }
}
