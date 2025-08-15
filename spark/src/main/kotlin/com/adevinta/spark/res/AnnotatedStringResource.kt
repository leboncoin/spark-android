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
package com.adevinta.spark.res

import android.content.res.Resources
import android.graphics.Typeface
import android.text.Annotation
import android.text.Spanned
import android.text.SpannedString
import android.text.style.AbsoluteSizeSpan
import android.text.style.BulletSpan
import android.text.style.ForegroundColorSpan
import android.text.style.RelativeSizeSpan
import android.text.style.StrikethroughSpan
import android.text.style.StyleSpan
import android.text.style.SubscriptSpan
import android.text.style.SuperscriptSpan
import android.text.style.TypefaceSpan
import android.text.style.UnderlineSpan
import android.util.Log
import androidx.annotation.PluralsRes
import androidx.annotation.StringRes
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.ReadOnlyComposable
import androidx.compose.runtime.remember
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.platform.LocalResources
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.BaselineShift
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Density
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.em
import androidx.core.text.buildSpannedString
import androidx.core.text.getSpans
import androidx.core.text.parseAsHtml
import androidx.core.text.toHtml
import com.adevinta.spark.PreviewTheme
import com.adevinta.spark.R
import com.adevinta.spark.SparkTheme
import com.adevinta.spark.tokens.SparkColors
import com.adevinta.spark.tokens.SparkTypography
import kotlinx.collections.immutable.PersistentMap
import kotlinx.collections.immutable.persistentMapOf

/**
 * Load an annotated string resource with formatting.
 *
 * ```xml
 * <string name="hello">Hello, <annotation variable="who" color="main">.</annotation>!</string>
 * ```
 *
 * ```kotlin
 * annotatedStringResource(
 *     id = R.string.hello,
 *     formatArgs = persistentMapOf("who" to "Bob"),
 * )
 * ```
 *
 * Beware, when using annotations with a `variable` mapping (`<annotation variable="...">`):
 * - The annotation content must not be empty, otherwise it will be stripped by AAPT. You can use a space, a dot, or anything else.
 * - The entire annotation content will be replaced by the provided argument mapping.
 * - If the argument mapping is not found, a [NoSuchElementException] will be thrown!
 *
 * @param id the resource identifier
 * @param formatArgs the format arguments
 * @return the [AnnotatedString] data associated with the resource
 */
@Composable
public fun annotatedStringResource(@StringRes id: Int, formatArgs: PersistentMap<String, String>): AnnotatedString {
    val resources = resources()
    val density = LocalDensity.current
    val colors = SparkTheme.colors
    val typography = SparkTheme.typography
    return remember(id, formatArgs) {
        resources.buildSpannedStringWithArgs(id, formatArgs).asAnnotatedString(density, colors, typography)
    }
}

/**
 * Load a annotated string resource with formatting.
 *
 * Be aware that using this method you'll loose the annotations support.
 *
 * @param id the resource identifier
 * @param formatArgs the format arguments
 * @return the [AnnotatedString] data associated with the resource
 */
@Deprecated(
    message = "Use the annotatedStringResource with PersistentMap overload instead",
    replaceWith = ReplaceWith("annotatedStringResource(id, persistentMapOf(formatArgs))"),
)
@Composable
public fun annotatedStringResource(@StringRes id: Int, vararg formatArgs: Any): AnnotatedString {
    val resources = resources()
    val density = LocalDensity.current
    val colors = SparkTheme.colors
    val typography = SparkTheme.typography
    return remember(id, formatArgs) {
        val text = resources.getText(id, *formatArgs)
        text.asAnnotatedString(density, colors, typography)
    }
}

private fun Resources.buildSpannedStringWithArgs(
    @StringRes id: Int,
    args: PersistentMap<String, String>,
): SpannedString = buildSpannedString {
    append(getText(id))
    getSpans<Annotation>().filter { it.key == "variable" }
        .forEach { replace(getSpanStart(it), getSpanEnd(it), args.getValue(it.value)) }
}

private fun Resources.buildSpannedStringWithArgs(
    @PluralsRes id: Int,
    count: Int,
    args: PersistentMap<String, String>,
): SpannedString = buildSpannedString {
    append(getQuantityText(id, count))
    getSpans<Annotation>().filter { it.key == "variable" }
        .forEach { replace(getSpanStart(it), getSpanEnd(it), args.getValue(it.value)) }
}

/**
 * Load a annotated string resource.
 *
 * @param id the resource identifier
 * @return the [AnnotatedString] data associated with the resource
 */
@Composable
public fun annotatedStringResource(@StringRes id: Int): AnnotatedString {
    val resources = resources()
    val density = LocalDensity.current

    val colors = SparkTheme.colors
    val typography = SparkTheme.typography
    return remember(id) {
        val text = resources.getText(id)
        text.asAnnotatedString(density, colors, typography)
    }
}

/**
 * Load a styled plurals resource.
 *
 * @param id the resource identifier
 * @param count the count
 * @return the pluralized string data associated with the resource
 * @see annotatedStringResource for more details
 */
@Composable
public fun annotatedPluralStringResource(
    @PluralsRes id: Int,
    count: Int,
): AnnotatedString {
    val resources = resources()
    val density = LocalDensity.current
    val colors = SparkTheme.colors
    val typography = SparkTheme.typography
    return remember(id) {
        val text = resources.getQuantityText(id, count)
        text.asAnnotatedString(density, colors, typography)
    }
}

/**
 * Load a styled plurals resource with provided format arguments.
 *
 * @param id the resource identifier
 * @param count the count
 * @param formatArgs arguments used in the format string
 * @return the pluralized string data associated with the resource
 */
@Composable
public fun annotatedPluralStringResource(
    @PluralsRes id: Int,
    count: Int,
    formatArgs: PersistentMap<String, String>,
): AnnotatedString {
    val resources = resources()
    val density = LocalDensity.current
    val colors = SparkTheme.colors
    val typography = SparkTheme.typography
    return remember(id) {
        val text = resources.buildSpannedStringWithArgs(id, count, formatArgs)
        text.asAnnotatedString(density, colors, typography)
    }
}

/**
 * Load a styled plurals resource with provided format arguments.
 *
 * @param id the resource identifier
 * @param count the count
 * @param formatArgs arguments used in the format string
 * @return the pluralized string data associated with the resource
 */
@Deprecated(
    message = "Use the annotatedPluralStringResource with PersistentMap overload instead",
    replaceWith = ReplaceWith("annotatedPluralStringResource(id, count, persistentMapOf(formatArgs))"),
)
@Composable
public fun annotatedPluralStringResource(
    @PluralsRes id: Int,
    count: Int,
    vararg formatArgs: Any,
): AnnotatedString {
    val resources = resources()
    val density = LocalDensity.current
    val colors = SparkTheme.colors
    val typography = SparkTheme.typography
    return remember(id) {
        val text = resources.getQuantityText(id, count, *formatArgs)
        text.asAnnotatedString(density, colors, typography)
    }
}

@Composable
@ReadOnlyComposable
internal fun resources(): Resources = LocalResources.current

/**
 * The framework `getText()` method doesn't support formatting arguments, so we need to do it ourselves.
 *
 * Unfortunately `toHtml()` doesn't support the `<annotation>` tag so we loose this span as we need to convert it to a
 * [String] to be able to use `String.format()`.
 */
private fun Resources.getText(@StringRes id: Int, vararg args: Any): CharSequence {
    val escapedArgs = args.map {
        if (it is Spanned) it.toHtmlWithoutParagraphs() else it
    }.toTypedArray()
    val spannedString = SpannedString(getText(id))
    val htmlResource = spannedString.toHtmlWithoutParagraphs()
    val formattedHtml = String.format(htmlResource, *escapedArgs)
    return formattedHtml.parseAsHtml()
}

internal fun Resources.getQuantityText(@PluralsRes id: Int, quantity: Int, vararg args: Any): CharSequence {
    val escapedArgs = args.map {
        if (it is Spanned) it.toHtmlWithoutParagraphs() else it
    }.toTypedArray()
    val resource = SpannedString(getQuantityText(id, quantity))
    val htmlResource = resource.toHtmlWithoutParagraphs()
    val formattedHtml = String.format(htmlResource, *escapedArgs)
    return formattedHtml.parseAsHtml()
}

/**
 * Convert a [Spanned] to a [String] without the `<p dir="ltr">` and `</p>` tags that are added by `toHtml()` which
 * added a padding at the end of the text.
 */
private fun Spanned.toHtmlWithoutParagraphs(): String = toHtml()
    .substringAfter("<p dir=\"ltr\">")
    .substringBeforeLast("</p>")

private fun CharSequence.asAnnotatedString(
    density: Density,
    colors: SparkColors,
    typography: SparkTypography,
): AnnotatedString {
    if (this !is Spanned) return AnnotatedString(this.toString())
    return buildAnnotatedString {
        append(this@asAnnotatedString.toString())
        getSpans(0, length, Any::class.java).forEach {
            val start = getSpanStart(it)
            val end = getSpanEnd(it)
            buildWithSpan(it, start, end, density, colors, typography)
        }
    }
}

private fun AnnotatedString.Builder.buildWithSpan(
    it: Any,
    start: Int,
    end: Int,
    density: Density,
    colors: SparkColors,
    typography: SparkTypography,
) {
    val span: SpanStyle = when (it) {
        is StyleSpan -> it.toSpanStyle() ?: return
        is TypefaceSpan -> it.toSpanStyle()
        is BulletSpan -> {
            Log.d("StringResources", "BulletSpan not supported yet")
            return
        }

        is AbsoluteSizeSpan -> with(density) {
            SpanStyle(fontSize = if (it.dip) it.size.dp.toSp() else it.size.toSp())
        }

        is RelativeSizeSpan -> SpanStyle(fontSize = it.sizeChange.em)
        is StrikethroughSpan -> SpanStyle(textDecoration = TextDecoration.LineThrough)
        is UnderlineSpan -> SpanStyle(textDecoration = TextDecoration.Underline)
        is SuperscriptSpan -> SpanStyle(baselineShift = BaselineShift.Superscript)
        is SubscriptSpan -> SpanStyle(baselineShift = BaselineShift.Subscript)
        is ForegroundColorSpan -> SpanStyle(color = Color(it.foregroundColor))
        is Annotation -> SparkStringAnnotations.toSpanStyle(annotation = it, colors, typography) ?: return
        else -> return
    }
    addStyle(span, start, end)
}

private fun StyleSpan.toSpanStyle(): SpanStyle? = when (style) {
    Typeface.NORMAL -> FontWeight.Normal to FontStyle.Normal
    Typeface.BOLD -> FontWeight.Bold to FontStyle.Normal
    Typeface.ITALIC -> FontWeight.Normal to FontStyle.Italic
    Typeface.BOLD_ITALIC -> FontWeight.Bold to FontStyle.Italic
    else -> null
}?.let { (fontWeight, fontStyle) ->
    SpanStyle(fontWeight = fontWeight, fontStyle = fontStyle)
}

private fun TypefaceSpan.toSpanStyle() = SpanStyle(
    fontFamily = when (family) {
        FontFamily.SansSerif.name -> FontFamily.SansSerif
        FontFamily.Serif.name -> FontFamily.Serif
        FontFamily.Monospace.name -> FontFamily.Monospace
        FontFamily.Cursive.name -> FontFamily.Cursive
        else -> FontFamily.Default
    },
)

@Preview
@Preview(
    locale = "fr-rFR",
)
@Composable
private fun AnnotatedStringResourcePreview() {
    PreviewTheme {
        Text(
            text = annotatedStringResource(R.string.spark_annotatedStringResource_test),
        )
        Text(
            text = annotatedStringResource(
                R.string.spark_annotatedStringResource_test_args,
                persistentMapOf("who" to "Bob"),
            ),
        )
    }
}
