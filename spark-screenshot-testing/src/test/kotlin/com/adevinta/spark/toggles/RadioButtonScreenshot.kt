package com.adevinta.spark.toggles

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.unit.dp
import com.adevinta.spark.DefaultTestDevices
import com.adevinta.spark.components.toggles.RadioButtonLabelled
import com.adevinta.spark.paparazziRule
import com.adevinta.spark.sparkSnapshotNightMode
import org.junit.Rule
import org.junit.Test

class RadioButtonScreenshot {
    @get:Rule
    val paparazzi = paparazziRule(
        deviceConfig = DefaultTestDevices.Foldable,
    )

    @Test
    fun all_states() {
        paparazzi.sparkSnapshotNightMode {
            Row {
                RadioButtonStates(isError = false)
                RadioButtonStates(isError = true)
            }
        }
    }
}

@Composable
private fun RadioButtonStates(isError: Boolean) {
    Column(
        verticalArrangement = Arrangement.spacedBy(16.dp),
    ) {
        RadioButtonLabelled(
            enabled = true,
            selected = true,
            onClick = {},
            error = isError,
        ) { Text("RadioButton On") }
        RadioButtonLabelled(
            enabled = false,
            selected = true,
            onClick = {},
            error = isError,
        ) { Text("RadioButton On") }
        RadioButtonLabelled(
            enabled = true,
            selected = false,
            onClick = {},
            error = isError,
        ) { Text("RadioButton Off") }
        RadioButtonLabelled(
            enabled = false,
            selected = false,
            onClick = {},
            error = isError,
        ) { Text("RadioButton Off") }
    }
} 
