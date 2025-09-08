/*
 * Copyright (c) 2023 Adevinta
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
package com.adevinta.spark.components.progress.tracker

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.animateColor
import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.updateTransition
import androidx.compose.foundation.LocalIndication
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.paddingFromBaseline
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.selection.selectable
import androidx.compose.foundation.selection.selectableGroup
import androidx.compose.material3.LocalContentColor
import androidx.compose.material3.ProvideTextStyle
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.layout.Layout
import androidx.compose.ui.layout.MultiContentMeasurePolicy
import androidx.compose.ui.layout.layoutId
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.lerp
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewScreenSizes
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.util.fastForEachIndexed
import com.adevinta.spark.ExperimentalSparkApi
import com.adevinta.spark.PreviewTheme
import com.adevinta.spark.SparkTheme
import com.adevinta.spark.components.IntentColor
import com.adevinta.spark.components.icons.Icon
import com.adevinta.spark.components.progress.tracker.LayoutOrientation.Horizontal
import com.adevinta.spark.components.progress.tracker.LayoutOrientation.Vertical
import com.adevinta.spark.components.surface.Surface
import com.adevinta.spark.components.text.Text
import com.adevinta.spark.components.textfields.LabelId
import com.adevinta.spark.icons.Check
import com.adevinta.spark.icons.SparkIcons
import com.adevinta.spark.tokens.dim1
import com.adevinta.spark.tokens.disabled
import com.adevinta.spark.tokens.highlight
import com.adevinta.spark.tools.modifiers.sparkUsageOverlay
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.persistentListOf
import androidx.compose.material3.HorizontalDivider as MaterialHorizontalDivider
import androidx.compose.material3.VerticalDivider as MaterialVerticalDivider

/**
 * [ProgressTrackerRow] is a visual navigation element typically used to display progress or guide user through a multi-step process.
 * It displays a linear progress as steps, accepts between 2 and 6 steps maximum.
 *
 * For the vertical use [ProgressTrackerColumn] instead.
 *
 * Each step displays its label via stepLabel. Selection and click are handled.
 * A step indicator is displayed via StepIndicator for each step. Its state is updated according to selection.
 *
 * @property items The list of steps to be displayed in the progress tracker.
 * @property modifier The modifier to be applied to the progress tracker.
 * @property intent The intent of the progress tracker, which determines its color scheme. Basic being the default.
 * @property size The size of the progress tracker, either large (default), medium, or small.
 * @property hasIndicatorContent A boolean value indicating whether the step indicator should show the step index.
 * @property onStepClick Callback with the step index selected.
 * @property selectedStep The index of the currently selected step. All steps before this index will be marked as done.
 *
 * @see [ProgressTrackerColumn]
 */
@Composable
@ExperimentalSparkApi
public fun ProgressTrackerRow(
    items: ImmutableList<ProgressStep>,
    modifier: Modifier = Modifier,
    intent: ProgressTrackerIntent = ProgressTrackerIntent.Basic,
    size: ProgressSizes = ProgressSizes.Large,
    hasIndicatorContent: Boolean = true,
    onStepClick: ((index: Int) -> Unit)? = null,
    selectedStep: Int = 0,
) {
    ProgressTracker(
        items = items,
        orientation = Horizontal,
        modifier = modifier,
        intent = intent,
        size = size,
        hasIndicatorContent = hasIndicatorContent,
        onStepClick = onStepClick,
        selectedStep = selectedStep,
    )
}

/**
 * [ProgressTrackerColumn] is a visual navigation element typically used to display progress or guide user through a multi-step process.
 * It displays a linear progress as steps, accepts between 2 and 6 steps maximum.
 *
 * For the horizontal use [ProgressTrackerColumn] instead.
 *
 * Each step displays its label via stepLabel. Selection and click are handled.
 * A step indicator is displayed via StepIndicator for each step. Its state is updated according to selection.
 *
 * @property items The list of steps to be displayed in the progress tracker.
 * @property modifier The modifier to be applied to the progress tracker.
 * @property intent The intent of the progress tracker, which determines its color scheme. Basic being the default.
 * @property size The size of the progress tracker, either large (default), medium, or small.
 * @property hasIndicatorContent A boolean value indicating whether the step indicator should show the step index.
 * @property onStepClick Callback with the step index selected.
 * @property selectedStep The index of the currently selected step. All steps before this index will be marked as done.
 *
 * @see [ProgressTrackerRow]
 */
@Composable
@ExperimentalSparkApi
public fun ProgressTrackerColumn(
    items: ImmutableList<ProgressStep>,
    modifier: Modifier = Modifier,
    intent: ProgressTrackerIntent = ProgressTrackerIntent.Basic,
    size: ProgressSizes = ProgressSizes.Large,
    hasIndicatorContent: Boolean = true,
    onStepClick: ((index: Int) -> Unit)? = null,
    selectedStep: Int = 0,
) {
    require(items.all { it.label.isNotBlank() }) {
        "All steps in the vertical layout should have a label otherwise it'll have render issues." +
                "This is a known bug, if you need to make this layout please report it there" +
                " https://github.com/leboncoin/spark-android/issues/1080"
    }
    ProgressTracker(
        items = items,
        orientation = Vertical,
        modifier = modifier,
        intent = intent,
        size = size,
        hasIndicatorContent = hasIndicatorContent,
        onStepClick = onStepClick,
        selectedStep = selectedStep,
    )
}

@Composable
private fun ProgressTracker(
    items: ImmutableList<ProgressStep>,
    orientation: LayoutOrientation,
    modifier: Modifier = Modifier,
    intent: ProgressTrackerIntent = ProgressTrackerIntent.Basic,
    size: ProgressSizes = ProgressSizes.Large,
    hasIndicatorContent: Boolean = true,
    onStepClick: ((index: Int) -> Unit)? = null,
    selectedStep: Int = 0,
) {
    require(items.size in 2..6) {
        val baseMessage = if (items.size < 2) {
            "At least two progress indicators should be displayed"
        } else {
            "If a process needs more than six steps, consider simplifying the process or breaking it up " +
                    "into multiple tasks"
        }
        baseMessage + " Found ${items.size} steps."
    }
    val colors = intent.colors()

    val progressTracks = @Composable {
        items.fastForEachIndexed { index, progressStep ->
            // Since the track is layouted after the step, we need to check the next step to know if the track
            // should be enabled
            val nextIndex = (index + 1).coerceAtMost(items.size - 1)
            val nextStep = items[nextIndex]
            ProgressTrack(
                enabled = progressStep.enabled && nextStep.enabled,
                orientation = orientation,
                index = index,
            )
        }
    }

    val interactionSources = remember(items.size) { items.map { MutableInteractionSource() } }

    val stepLabels = @Composable {
        items.fastForEachIndexed { index, progressStep ->
            StepLabel(
                label = progressStep.label,
                enabled = progressStep.enabled,
                orientation = orientation,
                index = index,
                selected = index == selectedStep,
                interactionSource = interactionSources[index],
                onClick = { onStepClick?.invoke(index) },
            )
        }
    }
    val stepIndicators = @Composable {
        items.forEachIndexed { index, progressStep ->
            val isDone = index < selectedStep
            StepIndicator(
                colors = colors,
                size = size,
                index = index,
                enabled = progressStep.enabled,
                selected = index == selectedStep,
                done = isDone,
                hasIndicatorContent = hasIndicatorContent,
                onClick = onStepClick?.let {
                    { onStepClick(index) }
                },
                interactionSource = interactionSources[index],
            )
        }
    }
    val measurePolicy = progressTrackerMeasurePolicy(orientation, size.size)
    ProvideTextStyle(value = SparkTheme.typography.body2.highlight) {
        Layout(
            modifier = modifier
                .selectableGroup()
                .sparkUsageOverlay(),
            measurePolicy = measurePolicy,
            contents = listOf(progressTracks, stepLabels, stepIndicators),
        )
    }
}

/**
 * [Row] will be [Horizontal], [Column] is [Vertical].
 */
internal enum class LayoutOrientation {
    Horizontal,
    Vertical,
}

@Composable
internal fun progressTrackerMeasurePolicy(
    orientation: LayoutOrientation,
    indicatorSize: TextUnit,
): MultiContentMeasurePolicy {
    val size = with(LocalDensity.current) {
        indicatorSize.toDp()
    }
    return ProgressTrackerMeasurePolicy(
        orientation = orientation,
        arrangementSpacing = 8.dp,
        indicatorSize = size,
    )
}

@Composable
private fun StepLabel(
    onClick: () -> Unit,
    orientation: LayoutOrientation,
    modifier: Modifier = Modifier,
    label: CharSequence = "",
    index: Int = 0,
    selected: Boolean = false,
    enabled: Boolean = true,
    interactionSource: MutableInteractionSource = remember { MutableInteractionSource() },
) {
    if (label.isBlank()) return Spacer(modifier = Modifier.size(0.dp))
    val transition = updateTransition(selected, label = "ProgressTrackerSelectedState")
    val labelColor by transition.animateColor(
        label = "Label color $index",
    ) {
        if (it) SparkTheme.colors.onSurface else SparkTheme.colors.onSurface.dim1
    }
    val labelProgress by transition.animateFloat(
        label = "LabelProgress $index",
    ) {
        if (it) 1f else 0f
    }
    val textStyle = lerp(
        SparkTheme.typography.body2,
        SparkTheme.typography.body2.highlight,
        labelProgress,
    )
    CompositionLocalProvider(
        LocalContentColor provides labelColor,
    ) {
        val labelModifier = modifier
            .layoutId(LabelId)
            .paddingFromBaseline(top = 16.dp)
            .selectable(
                selected = selected,
                interactionSource = interactionSource,
                indication = LocalIndication.current,
                enabled = enabled,
                role = Role.Tab,
            ) {
                onClick()
            }
        val textAlign = if (orientation == Horizontal) TextAlign.Center else TextAlign.Start
        when (label) {
            is AnnotatedString -> Text(
                text = label,
                textAlign = textAlign,
                style = textStyle,
                modifier = labelModifier,
            )

            else -> Text(
                text = label.toString(),
                textAlign = textAlign,
                style = textStyle,
                modifier = labelModifier,
            )
        }
    }
}

@Composable
private fun ProgressTrack(
    orientation: LayoutOrientation,
    modifier: Modifier = Modifier,
    index: Int = 0,
    enabled: Boolean = true,
) {
    val color = SparkTheme.colors.outline
    val trackColor by animateColorAsState(
        targetValue = if (enabled) color else color.disabled,
        label = "Track color $index",
    )
    if (orientation == Horizontal) {
        MaterialHorizontalDivider(modifier = modifier, color = trackColor)
    } else {
        MaterialVerticalDivider(modifier = modifier, color = trackColor)
    }
}

@Composable
private fun StepIndicator(
    colors: IntentColor,
    size: ProgressSizes,
    index: Int,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    selected: Boolean = true,
    done: Boolean = false,
    hasIndicatorContent: Boolean = true,
    onClick: (() -> Unit)? = null,
    interactionSource: MutableInteractionSource = remember { MutableInteractionSource() },
) {
    val elevation = animateStepElevation(enabled, interactionSource).value
    val indicatorColor by animateColorAsState(
        targetValue = if (selected) colors.color else colors.containerColor,
        label = "Indicator color",
    )

    val indicatorContentColor by animateColorAsState(
        targetValue = if (selected) colors.onColor else colors.onContainerColor,
        label = "Indicator color $index",
    )
    val indicatorAlpha by animateFloatAsState(
        targetValue = if (enabled) 1f else SparkTheme.colors.dim3,
        label = "Indicator color $index",
    )
    val indicatorSize = with(LocalDensity.current) {
        size.size.toDp()
    }

    Surface(
        shape = SparkTheme.shapes.full,
        modifier = modifier
            .size(indicatorSize)
            .aspectRatio(1f)
            .graphicsLayer {
                alpha = indicatorAlpha
            },
        color = indicatorColor,
        contentColor = indicatorContentColor,
        elevation = elevation,
        enabled = enabled,
        onClick = { onClick?.invoke() },
        interactionSource = interactionSource,
    ) {
        AnimatedVisibility(visible = size != ProgressSizes.Small && hasIndicatorContent) {
            AnimatedContent(
                targetState = done,
                contentAlignment = Alignment.Center,
                label = "Step status indicator",
            ) { isStepDone ->
                if (isStepDone) {
                    Icon(
                        sparkIcon = SparkIcons.Check,
                        modifier = Modifier
                            .size(indicatorSize)
                            .wrapContentSize(Alignment.Center),
                        contentDescription = null, // content description is handle on the Layout
                    )
                } else {
                    val stepPosition = index + 1
                    Text(
                        modifier = Modifier.wrapContentSize(Alignment.Center),
                        text = "$stepPosition",
                        style = SparkTheme.typography.body2.highlight.copy(lineHeight = 16.sp),
                    )
                }
            }
        }
    }
}

@PreviewScreenSizes
@Composable
private fun PreviewProgressTracker() {
    PreviewTheme(padding = PaddingValues(0.dp)) {
        var selectedStep by remember { mutableIntStateOf(0) }
        val size = ProgressSizes.Medium
        ProgressTrackerRow(
            items = persistentListOf(
                ProgressStep("Lorem ipsume", false),
                ProgressStep("Lorem ipsume dolar sit amet", true),
                ProgressStep("Lorem ipsume", false),
                ProgressStep("Lorem ipsume", true),
                ProgressStep("Lorem ipsume", false),
            ),
            size = size,
            onStepClick = {
                selectedStep = it
            },
            selectedStep = selectedStep,
        )
        ProgressTrackerColumn(
            items = persistentListOf(
                ProgressStep(
                    "Lorem ipsum dolor sit amet, consectetur adipiscing elit, sed do eiusmod tempor incididunt " +
                            "ut labore et dolore magna aliqua.Ut enim ad minim veniam, quis nostrud exercitation.",
                    true,
                ),
                ProgressStep(
                    "Lorem ipsum dolor sit amet, consectetur adipiscing elit, sed do eiusmod tempor incididunt " +
                            "ut labore et dolore magna aliqua.",
                    true,
                ),
                ProgressStep("Lorem ipsume dolar sit amet", true),
            ),
            size = size,
            onStepClick = {
                selectedStep = it
            },
            selectedStep = selectedStep,
        )
    }
}

@Composable
@Preview(
    fontScale = 2f,
)
private fun PreviewProgressSizes() {
    PreviewTheme(padding = PaddingValues(0.dp)) {
        var selectedStep by remember { mutableIntStateOf(1) }
        val items = persistentListOf(
            ProgressStep("Lorem ipsume", true),
            ProgressStep("Lorem ipsume dolar sit amet", true),
            ProgressStep("Lorem ipsume", false),
        )
        ProgressTrackerRow(
            items = items,
            size = ProgressSizes.Large,
            onStepClick = {
                selectedStep = it
            },
            selectedStep = selectedStep,
        )
        ProgressTrackerRow(
            items = items,
            size = ProgressSizes.Medium,
            onStepClick = {
                selectedStep = it
            },
            selectedStep = selectedStep,
        )
        ProgressTrackerRow(
            items = items,
            size = ProgressSizes.Small,
            onStepClick = {
                selectedStep = it
            },
            selectedStep = selectedStep,
        )
    }
}

@Composable
@Preview
private fun PreviewProgressWithNoLabel() {
    PreviewTheme(padding = PaddingValues(0.dp), contentPadding = 0.dp) {
        val selectedStep by remember { mutableIntStateOf(1) }
        val items = persistentListOf(
            ProgressStep("", true),
            ProgressStep("", true),
            ProgressStep("", false),
        )
        for (size in ProgressSizes.entries) {
            ProgressTrackerRow(
                items = items,
                size = size,
                selectedStep = selectedStep,
            )
        }
        Row {
            for (size in ProgressSizes.entries) {
                ProgressTrackerColumn(
                    items = persistentListOf(
                        ProgressStep("qzd", false),
                        ProgressStep("qzd", false),
                        ProgressStep("zdq", false),
                    ),
                    size = size,
                    selectedStep = selectedStep,
                )
            }
        }
    }
}

@Composable
@Preview(
    group = "ProgressIndicator",
)
private fun PreviewProgressIndicator() {
    PreviewTheme {
        val selectedStep by remember { mutableIntStateOf(1) }
        val items = persistentListOf(
            ProgressStep("", true),
            ProgressStep("", true),
            ProgressStep("", false),
        )
        items.forEachIndexed { index, progressStep ->
            val isDone = index < selectedStep
            StepIndicator(
                colors = ProgressTrackerIntent.Basic.colors(),
                size = ProgressSizes.Large,
                index = index,
                enabled = progressStep.enabled,
                selected = index == selectedStep,
                done = isDone,
                hasIndicatorContent = true,
                onClick = {},
                interactionSource = MutableInteractionSource(),
            )
        }
    }
}
