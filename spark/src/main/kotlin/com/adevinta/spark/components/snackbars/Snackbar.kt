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
package com.adevinta.spark.components.snackbars

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.SnackbarData
import androidx.compose.material3.SnackbarDefaults
import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarVisuals
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.adevinta.spark.PreviewTheme
import com.adevinta.spark.R
import com.adevinta.spark.SparkTheme
import com.adevinta.spark.components.buttons.BaseSparkButton
import com.adevinta.spark.components.iconbuttons.IconButtonDefaults.ghostIconButtonColorsForSnackbar
import com.adevinta.spark.components.iconbuttons.IconButtonSize
import com.adevinta.spark.components.iconbuttons.SparkIconButton
import com.adevinta.spark.components.icons.Icon
import com.adevinta.spark.icons.Cross
import com.adevinta.spark.icons.FlashlightFill
import com.adevinta.spark.icons.SparkIcon
import com.adevinta.spark.icons.SparkIcons
import com.adevinta.spark.tokens.contentColorFor
import com.adevinta.spark.tools.modifiers.sparkUsageOverlay
import androidx.compose.material3.Snackbar as MaterialSnackBar

public val SnackbarDefaults.style: SnackbarStyle
    get() = SnackbarStyle.Tinted

public val SnackbarDefaults.intent: SnackbarIntent
    get() = SnackbarIntent.Neutral

@Composable
internal fun SparkSnackbar(
    intent: SnackbarIntent,
    style: SnackbarStyle,
    actionOnNewLine: Boolean,
    withDismissAction: Boolean,
    modifier: Modifier = Modifier,
    icon: SparkIcon? = null,
    actionLabel: String? = null,
    onActionClick: (() -> Unit)? = null,
    onDismissClick: (() -> Unit)? = null,
    content: @Composable () -> Unit,
) {
    val backgroundColor = when (style) {
        SnackbarStyle.Filled -> intent.filledColor
        SnackbarStyle.Tinted -> intent.tintedColor
    }
    val contentColor = contentColorFor(backgroundColor = backgroundColor)

    MaterialSnackBar(
        modifier = modifier
            .padding(16.dp)
            .sparkUsageOverlay(),
        shape = SparkTheme.shapes.small,
        actionOnNewLine = actionOnNewLine,
        containerColor = backgroundColor,
        contentColor = contentColor,
        dismissAction = {
            DismissIcon(
                intent = intent,
                style = style,
                onClick = { onDismissClick?.invoke() },
                withDismissAction = withDismissAction,
            )
        },
        action = {
            SnackbarAction(
                intent = intent,
                onClick = { onActionClick?.invoke() },
                actionLabel = actionLabel,
                style = style,
                actionOnNewLine = actionOnNewLine,
            )
        },
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(4.dp),
        ) {
            icon?.let {
                Icon(
                    sparkIcon = it,
                    contentDescription = null, // this is a decorative icon
                    modifier = Modifier.size(24.dp),
                )
            }
            content()
        }
    }
}

@Composable
private fun DismissIcon(
    intent: SnackbarIntent,
    style: SnackbarStyle,
    onClick: () -> Unit,
    withDismissAction: Boolean,
) {
    if (!withDismissAction) return

    SparkIconButton(
        icon = SparkIcons.Cross,
        onClick = { onClick.invoke() },
        size = IconButtonSize.Small,
        modifier = Modifier.padding(end = 8.dp),
        colors = ghostIconButtonColorsForSnackbar(style, intent),
        contentDescription = stringResource(id = R.string.spark_a11y_snackbar_close),
    )
}

@Composable
private fun SnackbarAction(
    intent: SnackbarIntent,
    onClick: () -> Unit,
    style: SnackbarStyle,
    actionLabel: String? = null,
    actionOnNewLine: Boolean = false,
) {
    actionLabel ?: return
    val colors = ButtonDefaults.textButtonColors(
        contentColor = when (style) {
            SnackbarStyle.Filled -> contentColorFor(backgroundColor = intent.filledColor)
            SnackbarStyle.Tinted -> contentColorFor(backgroundColor = intent.tintedColor)
        },
    )
    val buttonModifier = when {
        actionOnNewLine ->
            Modifier
                .fillMaxWidth(0.8f)
                .wrapContentWidth(Alignment.End)
        else -> Modifier
    }
    BaseSparkButton(
        modifier = buttonModifier,
        colors = colors,
        onClick = { onClick.invoke() },
        elevation = null,
        content = { Text(actionLabel) },
    )
}

/**
 * Snackbars inform users of a process that an app has performed or will perform. They appear
 * temporarily, towards the bottom of the screen. They shouldn’t interrupt the user experience,
 * and they don’t require user input to disappear.
 *
 * A Snackbar can contain a single action. Because Snackbar disappears automatically, the action
 * shouldn't be "Dismiss" or "Cancel".
 *
 * If you want to customize appearance of the [Snackbar], you can pass your own version as a child
 * of the [SnackbarHost] to the [Scaffold]
 *
 * @param modifier modifiers for the Snackbar layout
 * @param intent The intent of the Snackbar.
 * @param style The style of the Snackbar.
 * @param withDismissAction Whether the dismiss icon is enabled.
 * @param actionOnNewLine whether or not action should be put on the separate line. Recommended
 * for action with long action text
 * @param icon icon to be shown on the start side of the content when there's no title.
 * @param actionLabel action to add as an action to the snackbar.
 * @param onActionClick callback when the action is clicked.
 * @param onDismissClick Callback for dismiss icon click.
 */
@Composable
public fun Snackbar(
    modifier: Modifier = Modifier,
    intent: SnackbarIntent = SnackbarDefaults.intent,
    style: SnackbarStyle = SnackbarDefaults.style,
    actionOnNewLine: Boolean = false,
    withDismissAction: Boolean = false,
    icon: SparkIcon? = null,
    actionLabel: String? = null,
    onActionClick: (() -> Unit)? = null,
    onDismissClick: (() -> Unit)? = null,
    content: @Composable (() -> Unit),
) {
    SparkSnackbar(
        intent = intent,
        style = style,
        modifier = modifier,
        actionOnNewLine = actionOnNewLine,
        withDismissAction = withDismissAction,
        icon = icon,
        actionLabel = actionLabel,
        onActionClick = onActionClick,
        onDismissClick = onDismissClick,
        content = content,
    )
}

/**
 * Snackbars provide brief messages about app processes at the bottom of the screen.
 *
 * Snackbars inform users of a process that an app has performed or will perform. They appear
 * temporarily, towards the bottom of the screen. They shouldn’t interrupt the user experience,
 * and they don’t require user input to disappear.
 *
 * A Snackbar can contain a single action. Because Snackbar disappears automatically, the action
 * shouldn't be "Dismiss" or "Cancel".
 *
 * If you want to customize appearance of the [Snackbar], you can pass your own version as a child
 * of the [SnackbarHost] to the [Scaffold]
 *
 * @param modifier modifiers for the Snackbar layout
 * @param data data class that contains the necessary information of a particular [Snackbar]
 * have a look at [SnackbarSparkVisuals] , [SnackbarData]
 */
@Composable
public fun Snackbar(
    data: SnackbarData,
    modifier: Modifier = Modifier,
) {
    val visuals = data.visuals
    val sparkVisuals = data.visuals as? SnackbarSparkVisuals

    SparkSnackbar(
        intent = sparkVisuals?.intent ?: SnackbarDefaults.intent,
        style = sparkVisuals?.style ?: SnackbarDefaults.style,
        modifier = modifier,
        actionOnNewLine = sparkVisuals?.actionOnNewLine ?: false,
        withDismissAction = sparkVisuals?.withDismissAction ?: false,
        icon = sparkVisuals?.icon,
        actionLabel = visuals.actionLabel,
        onActionClick = { data.performAction() },
        onDismissClick = { data.dismiss() },
    ) { Text(visuals.message) }
}

/**
 * SnackbarVisuals interface that defines the visuals for a Snackbar.
 *
 * Class that contains the necessary information of a particular [Snackbar]
 * as a piece of the [SnackbarData].
 *
 * @param message text to be shown in the Snackbar
 * @param icon optional icon to show on the start side of the Snackbar
 * @param intent background color, note that the surfaceInverse one is not available with the Tinted style
 * @param style style of the Snackbar, Tinted as a lower emphasis than the Filled style
 * @param actionLabel action label to show as button in the Snackbar
 * @param withDismissAction a boolean to show a dismiss action in the Snackbar. This is
 * recommended to be set to true better accessibility when a Snackbar is set with a
 * [SnackbarDuration.Indefinite]
 * @param duration shown duration of the Snackbar, will adapt for a11y context
 */
public class SnackbarSparkVisuals(
    override val message: String,
    public val icon: SparkIcon? = null,
    public val intent: SnackbarIntent = SnackbarDefaults.intent,
    public val style: SnackbarStyle = SnackbarDefaults.style,
    override val actionLabel: String? = null,
    override val withDismissAction: Boolean = false,
    public val actionOnNewLine: Boolean = false,
    override val duration: SnackbarDuration = SnackbarDuration.Short,
) : SnackbarVisuals

/***
 * Preview
 */
internal const val StubBodyShort = "Lorem ipsum dolor sit amet"
internal const val StubBody = "Lorem ipsum dolor sit amet, consectetur adipiscing elit."
internal const val StubBodyLong = "Lorem ipsum dolor sit amet, consectetur adipiscing elit. Morbi lacus dolor, "
internal const val StubAction = "Action"

@Preview
@Composable
private fun BodySnackbarPreview() {
    PreviewTheme {
        Snackbar {
            Text(StubBodyShort)
        }
    }
}

@Preview
@Composable
private fun BodyLongSnackbarPreview() {
    PreviewTheme {
        Snackbar {
            Text(StubBody, maxLines = 2)
        }
    }
}

@Preview
@Composable
private fun BodyActionSnackbarPreview() {
    PreviewTheme {
        Snackbar(
            style = SnackbarStyle.Filled,
            actionLabel = StubAction,
        ) {
            Text(StubBodyShort)
        }
    }
}

@Preview
@Composable
private fun BodyIconActionSnackbarPreview() {
    PreviewTheme {
        Snackbar(
            style = SnackbarStyle.Filled,
            icon = SparkIcons.FlashlightFill,
            actionLabel = StubAction,
        ) {
            Text(StubBodyShort)
        }
    }
}

@Preview
@Composable
private fun BodyIconSnackbarPreview() {
    PreviewTheme {
        Snackbar(
            intent = SnackbarIntent.Error,
            withDismissAction = true,
            icon = SparkIcons.FlashlightFill,
        ) {
            Text(StubBodyShort)
        }
    }
}

@Preview
@Composable
private fun BodyIconActionNewLineLongSnackbarPreview() {
    PreviewTheme {
        Snackbar(
            intent = SnackbarIntent.SurfaceInverse,
            withDismissAction = true,
            actionOnNewLine = true,
            style = SnackbarStyle.Tinted,
            icon = SparkIcons.FlashlightFill,
            actionLabel = StubBodyLong,
        ) {
            Text(StubBody)
        }
    }
}
