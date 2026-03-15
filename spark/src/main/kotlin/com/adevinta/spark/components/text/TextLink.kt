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
package com.adevinta.spark.components.text

import android.annotation.SuppressLint
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.Interaction
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.text.InlineTextContent
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.LocalContentColor
import androidx.compose.material3.LocalTextStyle
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.SnackbarDuration
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.TextLayoutResult
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.TextUnit
import com.adevinta.spark.PreviewTheme
import com.adevinta.spark.R
import com.adevinta.spark.SparkTheme
import com.adevinta.spark.components.buttons.BaseSparkButton
import com.adevinta.spark.components.buttons.ButtonIntent
import com.adevinta.spark.components.buttons.ButtonSize
import com.adevinta.spark.components.buttons.IconSide
import com.adevinta.spark.components.buttons.SparkButtonDefaults
import com.adevinta.spark.components.scaffold.Scaffold
import com.adevinta.spark.components.snackbars.SnackbarHost
import com.adevinta.spark.components.snackbars.SnackbarHostState
import com.adevinta.spark.icons.InfoOutline
import com.adevinta.spark.icons.SparkIcon
import com.adevinta.spark.icons.LeboncoinIcons
import com.adevinta.spark.res.annotatedStringResource
import com.adevinta.spark.tools.modifiers.sparkUsageOverlay
import kotlinx.collections.immutable.ImmutableMap
import kotlinx.collections.immutable.persistentMapOf
import kotlinx.coroutines.launch

/**
 * Component that displays an underlined text link
 *
 * The default [style] uses the [LocalTextStyle] provided by the [MaterialTheme] / components. If
 * you are setting your own style, you may want to consider first retrieving [LocalTextStyle],
 * and using [TextStyle.copy] to keep any theme defined attributes, only modifying the specific
 * attributes you want to override.
 *
 * For ease of use, commonly used parameters from [TextStyle] are also present here. The order of
 * precedence is as follows:
 * - If a parameter is explicitly set here (i.e, it is _not_ `null` or [TextUnit.Unspecified]),
 * then this parameter will always be used.
 * - If a parameter is _not_ set, (`null` or [TextUnit.Unspecified]), then the corresponding value
 * from [style] will be used instead.
 *
 *
 * @param text the text to be displayed that has link, should be provided as AnnotatedStringResource, with colors & underlined link parts
 * We support Spark color intents & Typography to be added within annotations
 *     <string name="example"><annotation typography="display3" color="success"><u><b>link Sample</b></u></annotation></string>
 * @param onClickLabel label to be used for accessibility screen readers
 * @param modifier the [Modifier] to be applied to this layout node
 * @param overflow how visual overflow should be handled.
 * @param softWrap whether the text should break at soft line breaks. If false, the glyphs in the
 * text will be positioned as if there was unlimited horizontal space. If [softWrap] is false,
 * [overflow] and TextAlign may have unexpected effects.
 * @param maxLines an optional maximum number of lines for the text to span, wrapping if
 * necessary. If the text exceeds the given number of lines, it will be truncated according to
 * [overflow] and [softWrap]. If it is not null, then it must be greater than zero.
 * @param minLines The minimum height in terms of minimum number of visible lines. It is required
 * that 1 <= [minLines] <= [maxLines].
 * @param onTextLayout callback that is executed when a new text layout is calculated. A
 * [TextLayoutResult] object that callback provides contains paragraph information, size of the
 * text, baselines and other details. The callback can be used to add additional decoration or
 * functionality to the text. For example, to draw selection around the text.
 * @param style style configuration for the text such as color, font, line height etc.
 * @param onClick callback when textLink container is clicked.
 */

@Composable
public fun TextLink(
    text: AnnotatedString,
    onClickLabel: String,
    modifier: Modifier = Modifier,
    color: Color = Color.Unspecified,
    fontSize: TextUnit = TextUnit.Unspecified,
    fontStyle: FontStyle? = null,
    fontWeight: FontWeight? = null,
    fontFamily: FontFamily? = null,
    letterSpacing: TextUnit = TextUnit.Unspecified,
    textDecoration: TextDecoration? = null,
    textAlign: TextAlign = TextAlign.Unspecified,
    lineHeight: TextUnit = TextUnit.Unspecified,
    overflow: TextOverflow = TextOverflow.Clip,
    softWrap: Boolean = true,
    maxLines: Int = Int.MAX_VALUE,
    minLines: Int = 1,
    inlineContent: ImmutableMap<String, InlineTextContent> = persistentMapOf(),
    onTextLayout: (TextLayoutResult) -> Unit = {},
    style: TextStyle = LocalTextStyle.current,
    onClick: () -> Unit,
) {
    SparkText(
        text = text,
        modifier = modifier.clickable(
            onClickLabel = onClickLabel,
            onClick = onClick,
        ).sparkUsageOverlay(),
        style = style,
        overflow = overflow,
        softWrap = softWrap,
        lineHeight = lineHeight,
        maxLines = maxLines,
        minLines = minLines,
        inlineContent = inlineContent,
        color = color,
        fontSize = fontSize,
        fontStyle = fontStyle,
        fontWeight = fontWeight,
        fontFamily = fontFamily,
        letterSpacing = letterSpacing,
        textDecoration = textDecoration,
        textAlign = textAlign,
        onTextLayout = { onTextLayout(it) },
    )
}

/**
 * Component that displays an underlined text link Button
 * @param text the text to be displayed as a underlined link
 * @param onClick callback when textLink button is clicked.
 * @param modifier the [Modifier] to be applied to this layout node
 * @param size The size of the button
 * @param intent The intent color for the button. Note that [ButtonIntent.Surface] will use the the [LocalContentColor].
 * @param enabled Controls the enabled state of the button. When `false`, this button will not be clickable
 * @param icon The optional icon to be displayed at the start or the end of the button container.
 * @param iconSide If an icon is added, you can configure the side where is should be displayed, at the start
 * or end of the button
 * @param isLoading show or hide a CircularProgressIndicator at the start that push the content to indicate a
 * loading state
 * @param interactionSource the [MutableInteractionSource] representing the stream of [Interaction]s
 * for this button. You can create and pass in your own `remember`ed instance to observe
 * [Interaction]s and customize the appearance / behavior of this button in different states.
 */
@Composable
public fun TextLinkButton(
    text: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    size: ButtonSize = ButtonSize.Medium,
    intent: ButtonIntent = ButtonIntent.Surface,
    enabled: Boolean = true,
    icon: SparkIcon? = null,
    iconSide: IconSide = IconSide.START,
    isLoading: Boolean = false,
    interactionSource: MutableInteractionSource = remember { MutableInteractionSource() },
) {
    val colors = ButtonDefaults.textButtonColors(
        contentColor = if (intent != ButtonIntent.Surface) intent.colors().color else LocalContentColor.current,
    )

    BaseSparkButton(
        onClick = onClick,
        modifier = modifier,
        size = size,
        enabled = enabled,
        elevation = ButtonDefaults.buttonElevation(),
        colors = colors,
        icon = icon,
        iconSide = iconSide,
        isLoading = isLoading,
        contentPadding = SparkButtonDefaults.textlinkButtonContentPadding(size),
        interactionSource = interactionSource,
    ) {
        Text(
            text = text,
            style = LocalTextStyle.current.copy(textDecoration = TextDecoration.Underline),
        )
    }
}

/**
 * Component that displays an underlined text link Button
 * @param onClick callback when textLink button is clicked.
 * @param modifier the [Modifier] to be applied to this layout node
 * @param size The size of the button
 * @param intent The intent color for the button.
 * @param enabled Controls the enabled state of the button. When `false`, this button will not be clickable
 * @param icon The optional icon to be displayed at the start or the end of the button container.
 * @param iconSide If an icon is added, you can configure the side where is should be displayed, at the start
 * or end of the button
 * @param isLoading show or hide a CircularProgressIndicator at the start that push the content to indicate a
 * loading state
 * @param interactionSource the [MutableInteractionSource] representing the stream of [Interaction]s
 * for this button. You can create and pass in your own `remember`ed instance to observe
 * [Interaction]s and customize the appearance / behavior of this button in different states.
 */
@SuppressLint("VisibleForTests")
@Composable
public fun TextLinkButton(
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    size: ButtonSize = ButtonSize.Medium,
    intent: ButtonIntent = ButtonIntent.Surface,
    enabled: Boolean = true,
    icon: SparkIcon? = null,
    iconSide: IconSide = IconSide.START,
    isLoading: Boolean = false,
    interactionSource: MutableInteractionSource = remember { MutableInteractionSource() },
    content: @Composable RowScope.() -> Unit,
) {
    val colors = ButtonDefaults.textButtonColors(contentColor = intent.colors().color)

    BaseSparkButton(
        onClick = onClick,
        modifier = modifier,
        size = size,
        enabled = enabled,
        elevation = ButtonDefaults.buttonElevation(),
        colors = colors,
        icon = icon,
        iconSide = iconSide,
        isLoading = isLoading,
        contentPadding = SparkButtonDefaults.textlinkButtonContentPadding(size),
        interactionSource = interactionSource,
    ) {
        content()
    }
}

@Preview(
    group = "Text",
    name = "TextLink",
)
@Composable
private fun SparkTextLinkPreview() {
    PreviewTheme(
        color = { SparkTheme.colors.backgroundVariant },
    ) {
        val snackbarHostState = remember { SnackbarHostState() }
        val scope = rememberCoroutineScope()

        Scaffold(
            modifier = Modifier.fillMaxSize(),
            snackbarHost = {
                SnackbarHost(snackbarHostState)
            },
        ) {
            Column {
                Column {
                    TextLink(
                        style = SparkTheme.typography.subhead,
                        text = annotatedStringResource(id = R.string.spark_annotatedStringResource_test_args),
                        onClickLabel = "Privacy & Policy",
                        onClick = {
                            scope.launch {
                                snackbarHostState.showSnackbar(
                                    message = "Privacy & Policy Clicked",
                                    actionLabel = "Action",
                                    duration = SnackbarDuration.Short,
                                )
                            }
                        },
                    )
                    TextLinkButton(
                        text = "Click me",
                        icon = LeboncoinIcons.InfoOutline,
                        onClick = {
                            scope.launch {
                                snackbarHostState.showSnackbar(
                                    message = "Privacy & Policy Clicked",
                                    actionLabel = "Action",
                                    duration = SnackbarDuration.Short,
                                )
                            }
                        },
                    )
                }
            }
        }
    }
}
