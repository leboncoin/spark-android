/*
 * Copyright (c) 2024 Adevinta
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */
package com.adevinta.spark.catalog.ui.shaders.colorblindness

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.systemGestureExclusion
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.adevinta.spark.SparkTheme
import com.adevinta.spark.catalog.ui.ButtonGroup
import com.adevinta.spark.catalog.util.PreviewTheme
import com.adevinta.spark.tokens.highlight
import org.intellij.lang.annotations.Language
import kotlin.math.roundToInt
import com.adevinta.spark.components.text.Text
import com.adevinta.spark.components.slider.Slider

@Language("agsl")
public val shader: String = ColorBlindness + """
uniform float severity;
uniform int colorblindType;
uniform shader composable;

half4 main(float2 coord) {
    float4 col = composable.eval(coord);

    int p1 = int(min(10, floor(severity * 10.0)));
    int p2 = int(min(10, floor((severity + 0.1) * 10.0)));
    float weight = fract(severity * 10.0);

    float3x3 matrix1 = getColorBlindnessMatrix(colorblindType, p1);
    float3x3 matrix2 = getColorBlindnessMatrix(colorblindType, p2);

    float3 newCB1 = mix(matrix1[0], matrix2[0], weight);
    float3 newCB2 = mix(matrix1[1], matrix2[1], weight);
    float3 newCB3 = mix(matrix1[2], matrix2[2], weight);

    float3x3 blindness = float3x3(newCB1, newCB2, newCB3);

    float3 cb = saturate(col.rgb * blindness);

    return float4(cb, 1.0);
}
"""

@RequiresApi(Build.VERSION_CODES.TIRAMISU)
@Composable
public fun ColumnScope.ColorBlindSetting(
    severity: Float = 0.5f,
    colorBlindNessType: ColorBlindNessType = ColorBlindNessType.None,
    onSeverityChange: (Float) -> Unit,
    onTypeChange: (ColorBlindNessType) -> Unit,
) {
    val severityLabel by remember(severity) {
        derivedStateOf {
            (severity * 10).roundToInt()
        }
    }

    Text(
        text = "Severity: $severityLabel",
        style = SparkTheme.typography.body2.highlight,
        modifier = Modifier.padding(bottom = 8.dp),
    )
    Slider(
        modifier = Modifier.systemGestureExclusion(),
        value = severity,
        valueRange = 0f..1f,
        steps = 9,
        onValueChange = onSeverityChange,
    )

    ButtonGroup(
        title = "Color blindness type",
        selectedOption = colorBlindNessType,
        onOptionSelect = onTypeChange,
    )
}

public enum class ColorBlindNessType {
    Deuteranomaly,
    Protanomaly,
    Tritanomaly,
    None,
}

@RequiresApi(Build.VERSION_CODES.TIRAMISU)
@Preview
@Composable
private fun ColorBlindScreenPreview() {
    PreviewTheme {
        ColorBlindSetting(
            onSeverityChange = { },
            onTypeChange = { },
        )
    }
}
