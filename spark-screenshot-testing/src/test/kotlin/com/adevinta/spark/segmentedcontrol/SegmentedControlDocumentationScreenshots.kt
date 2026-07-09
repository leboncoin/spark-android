/*
 * Copyright (c) 2026 Adevinta
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
package com.adevinta.spark.segmentedcontrol

import androidx.compose.animation.animateColor
import androidx.compose.animation.core.animateDp
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.updateTransition
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Immutable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.isSpecified
import androidx.compose.ui.text.lerp
import androidx.compose.ui.unit.dp
import com.adevinta.spark.DefaultTestDevices
import com.adevinta.spark.SparkTheme
import com.adevinta.spark.components.segmentedcontrol.SegmentedControl
import com.adevinta.spark.components.segmentedcontrol.SegmentedControlScope
import com.adevinta.spark.components.segmentedcontrol.SegmentedControlShape
import com.adevinta.spark.components.text.Text
import com.adevinta.spark.icons.Building
import com.adevinta.spark.icons.House
import com.adevinta.spark.icons.LeboncoinIcons
import com.adevinta.spark.icons.List
import com.adevinta.spark.icons.PaperMapFill
import com.adevinta.spark.icons.ParasolOutline
import com.adevinta.spark.icons.PeopleCriteria
import com.adevinta.spark.icons.ShoppingCartOutline
import com.adevinta.spark.paparazziRule
import com.adevinta.spark.sparkDocSnapshot
import com.adevinta.spark.tokens.dim1
import com.adevinta.spark.tokens.disabled
import com.adevinta.spark.tokens.highlight
import com.adevinta.spark.tokens.transparent
import com.android.ide.common.rendering.api.SessionParams.RenderingMode.SHRINK
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.persistentListOf
import org.junit.Rule
import org.junit.Test

internal class SegmentedControlDocumentationScreenshots {

    @get:Rule
    val paparazzi = paparazziRule(
        renderingMode = SHRINK,
        deviceConfig = DefaultTestDevices.DocPhone,
    )

    @Test
    fun horizontal() = paparazzi.sparkDocSnapshot(color = { SparkTheme.colors.backgroundVariant }) {
        SegmentedControl.Horizontal(selectedIndex = 1) {
            singleLine("All", selected = false, onClick = {})
            singleLine("Active", selected = true, onClick = {})
            singleLine("Done", selected = false, onClick = {})
        }
    }

    @Test
    fun horizontalContentTypes() = paparazzi.sparkDocSnapshot(color = { SparkTheme.colors.backgroundVariant }) {
        SegmentedControl.Horizontal(selectedIndex = 0) {
            singleLine("Text", selected = true, onClick = {})
            twoLine("Title", "Subtitle", selected = false, onClick = {})
            icon(LeboncoinIcons.ShoppingCartOutline, contentDescription = "Cart", selected = false, onClick = {})
            iconText(LeboncoinIcons.ShoppingCartOutline, "Cart", selected = false, iconOnTop = true, onClick = {})
        }
    }

    @Test
    fun verticalRounded() = paparazzi.sparkDocSnapshot(color = { SparkTheme.colors.backgroundVariant }) {
        SegmentedControl.Vertical(selectedIndex = 2, shape = SegmentedControlShape.Rounded) {
            singleLine("Option 1", selected = false, onClick = {})
            singleLine("Option 2", selected = false, onClick = {})
            singleLine("Option 3", selected = true, onClick = {})
            singleLine("Option 4", selected = false, onClick = {})
            singleLine("Option 5", selected = false, onClick = {})
        }
    }

    @Test
    fun verticalPill() = paparazzi.sparkDocSnapshot(color = { SparkTheme.colors.backgroundVariant }) {
        SegmentedControl.Vertical(selectedIndex = 2, shape = SegmentedControlShape.Pill) {
            singleLine("Option 1", selected = false, onClick = {})
            singleLine("Option 2", selected = false, onClick = {})
            singleLine("Option 3", selected = true, onClick = {})
            singleLine("Option 4", selected = false, onClick = {})
            singleLine("Option 5", selected = false, onClick = {})
        }
    }

    @Test
    fun singleLine() = paparazzi.sparkDocSnapshot(color = { SparkTheme.colors.backgroundVariant }) {
        SegmentedControl.Horizontal(selectedIndex = 0) {
            singleLine("Je vends", selected = true, onClick = {})
            singleLine("Je donne", selected = false, onClick = {})
        }
    }

    @Test
    fun twoLine() = paparazzi.sparkDocSnapshot(color = { SparkTheme.colors.backgroundVariant }) {
        SegmentedControl.Horizontal(selectedIndex = 0) {
            twoLine("Offre", "Je vends", selected = true, onClick = {})
            twoLine("Demande", "Je recherche", selected = false, onClick = {})
        }
    }

    @Test
    fun icon() = paparazzi.sparkDocSnapshot(color = { SparkTheme.colors.backgroundVariant }) {
        SegmentedControl.Horizontal(selectedIndex = 0) {
            icon(icon = LeboncoinIcons.List, contentDescription = "List", selected = true, onClick = {})
            icon(icon = LeboncoinIcons.PaperMapFill, contentDescription = "Map", selected = false, onClick = {})
        }
    }

    @Test
    fun iconText() = paparazzi.sparkDocSnapshot(color = { SparkTheme.colors.backgroundVariant }) {
        SegmentedControl.Vertical(selectedIndex = 0) {
            iconText(
                icon = LeboncoinIcons.House,
                text = "Vente immobilière",
                selected = true,
                iconOnTop = true,
                onClick = {},
            )
            iconText(
                icon = LeboncoinIcons.Building,
                text = "Location",
                selected = false,
                iconOnTop = true,
                onClick = {},
            )
            iconText(
                icon = LeboncoinIcons.ParasolOutline,
                text = "Location Saisonnière",
                selected = false,
                iconOnTop = true,
                onClick = {
                },
            )
            iconText(
                icon = LeboncoinIcons.PeopleCriteria,
                text = "Colocation",
                selected = false,
                iconOnTop = true,
                onClick = {},
            )
        }
    }

    @Test
    fun number() = paparazzi.sparkDocSnapshot(color = { SparkTheme.colors.backgroundVariant }) {
        SegmentedControl.Horizontal(selectedIndex = 0) {
            number(1, selected = true, onClick = {})
            number(2, selected = false, onClick = {})
            number(3, selected = false, onClick = {})
        }
    }

    @Immutable
    private data class EnergyRatingData(val text: String, val color: Color, val contentColor: Color)
    private val EnergyRatingDataFake: ImmutableList<EnergyRatingData> = persistentListOf(
        EnergyRatingData("A", Color(0xFF009424), Color.White),
        EnergyRatingData("B", Color(0xFF3ACC31), Color.White),
        EnergyRatingData("C", Color(0xFFCDFD32), Color.Black),
        EnergyRatingData("D", Color(0xFFFBEA49), Color.Black),
        EnergyRatingData("E", Color(0xFFFCCB2F), Color.Black),
        EnergyRatingData("F", Color(0xFFFB9C34), Color.Black),
        EnergyRatingData("G", Color(0xFFFA1C1F), Color.White),
        EnergyRatingData("Vierge", Color.Unspecified, Color.Unspecified),
    )

    @Test
    fun custom() = paparazzi.sparkDocSnapshot(color = { SparkTheme.colors.backgroundVariant }) {
        var selectedIndex by remember { mutableIntStateOf(0) }
        SegmentedControl.Vertical(
            selectedIndex = selectedIndex,
            shape = SegmentedControlShape.Pill,
            indicatorContent = { selectedIndex, enabled ->
                val data = EnergyRatingDataFake[selectedIndex]
                val transition = updateTransition(data, label = "indicator")
                val background = transition.animateColor(label = "indicatorBackground") { d ->
                    if (d.color.isSpecified) {
                        if (enabled) d.color else d.color.disabled
                    } else {
                        if (enabled) SparkTheme.colors.neutralContainer else SparkTheme.colors.surface
                    }
                }
                val borderColor = transition.animateColor(label = "indicatorBorderColor") { d ->
                    if (d.color.isSpecified) {
                        SparkTheme.colors.outlineHigh.transparent
                    } else {
                        if (enabled) SparkTheme.colors.outlineHigh else SparkTheme.colors.outlineHigh.disabled
                    }
                }
                val borderSize = transition.animateDp(label = "indicatorBorderSize") { d ->
                    if (d.color.isSpecified) 0.dp else 2.dp
                }

                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(4.dp)
                        .clip(SegmentedControlShape.Pill.shape)
                        .border(borderSize.value, borderColor.value, SegmentedControlShape.Pill.shape)
                        .drawBehind { drawRect(background.value) },
                )
            },
        ) {
            EnergyRatingDataFake.forEachIndexed { index, data ->
                if (data.color.isSpecified) {
                    DPE(
                        text = data.text,
                        color = data.color,
                        contentColor = data.contentColor,
                        selected = selectedIndex == index,
                        onClick = { selectedIndex = index },
                    )
                } else {
                    singleLine(data.text, selected = selectedIndex == index, onClick = { selectedIndex = index })
                }
            }
        }
    }
}

@Composable
private fun SegmentedControlScope.DPE(
    text: String,
    color: Color,
    contentColor: Color,
    selected: Boolean,
    onClick: () -> Unit,
) {
    custom(
        selected = selected,
        onClick = onClick,
        rippleColor = color,
    ) {
        val transition = updateTransition(selected, label = "dpeLabel")
        val labelColor by transition.animateColor(label = "labelColor") {
            if (it) contentColor else SparkTheme.colors.onSurface.dim1
        }
        val labelProgress by transition.animateFloat(label = "labelProgress") { if (it) 1f else 0f }
        val textStyle = lerp(
            SparkTheme.typography.body2,
            SparkTheme.typography.body2.highlight,
            labelProgress,
        )
        Text(text, color = labelColor, style = textStyle)
    }
}
