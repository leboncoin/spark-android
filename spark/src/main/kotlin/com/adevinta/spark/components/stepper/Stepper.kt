/*
 * Copyright (c) 2025 Adevinta
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
package com.adevinta.spark.components.stepper

import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.VisibilityThreshold
import androidx.compose.animation.core.spring
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.progressSemantics
import androidx.compose.foundation.text.input.TextFieldState
import androidx.compose.foundation.text.input.delete
import androidx.compose.foundation.text.input.insert
import androidx.compose.material3.LocalContentColor
import androidx.compose.material3.ProvideTextStyle
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.key.Key
import androidx.compose.ui.input.key.KeyEventType
import androidx.compose.ui.input.key.isAltPressed
import androidx.compose.ui.input.key.isCtrlPressed
import androidx.compose.ui.input.key.isMetaPressed
import androidx.compose.ui.input.key.isShiftPressed
import androidx.compose.ui.input.key.key
import androidx.compose.ui.input.key.onKeyEvent
import androidx.compose.ui.input.key.type
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.disabled
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.semantics.setProgress
import androidx.compose.ui.semantics.stateDescription
import androidx.compose.ui.semantics.text
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import com.adevinta.spark.PreviewTheme
import com.adevinta.spark.R
import com.adevinta.spark.SparkTheme
import com.adevinta.spark.components.stepper.Stepper.Input
import com.adevinta.spark.components.stepper.Stepper.InputForm
import com.adevinta.spark.components.stepper.Stepper.Nudger
import com.adevinta.spark.components.stepper.Stepper.NudgerForm
import com.adevinta.spark.components.stepper.internal.SparkNudger
import com.adevinta.spark.components.stepper.internal.SparkStepperInput
import com.adevinta.spark.components.stepper.internal.formatInteger
import com.adevinta.spark.components.stepper.internal.stepperInputValidator
import com.adevinta.spark.components.stepper.internal.stepperStateDescription
import com.adevinta.spark.components.stepper.internal.stripGroupingSeparators
import com.adevinta.spark.components.stepper.internal.supportText
import com.adevinta.spark.components.text.Text
import com.adevinta.spark.components.textfields.FormFieldStatus
import com.adevinta.spark.components.textfields.TextFieldDefault
import com.adevinta.spark.components.textfields.sparkOutlinedTextFieldColors
import com.adevinta.spark.tokens.EmphasizeDim3
import com.adevinta.spark.tokens.dim1
import com.adevinta.spark.tokens.dim5
import com.adevinta.spark.tokens.transparent
import com.adevinta.spark.tools.modifiers.invisibleSemantic
import com.adevinta.spark.tools.modifiers.sparkUsageOverlay
import kotlin.math.roundToInt

/**
 * Stepper variants for quantity picking with decrease and increase buttons.
 *
 * Variants: [Nudger], [NudgerForm], [Input], [InputForm].
 */
public object Stepper {

    /**
     * Nudger stepper with decrease and increase buttons on either side of the selected value.
     *
     * ![Nudger stepper](https://leboncoin.github.io/spark-android/images/com.adevinta.spark.components.stepper_StepperDocumentationScreenshots_nudger.png)
     *
     * @param value Value of the quantity picker, or `null` for an empty state
     * @param onValueChange The callback to be called when [value] has been incremented or decremented
     * @param modifier The [Modifier] to be applied to the component
     * @param range The min/max accepted value by the stepper until it blocks increments and decrements
     * @param suffix optional string displayed after [value]
     * @param placeholder text shown in the empty state when [value] is `null`
     * @param step the quantity to be increased/decreased on each increment/decrement
     * @param enabled True controls the enabled state of the stepper. When `false`, the stepper will
     * be neither editable nor focusable, visually stepper will appear in the disabled UI state
     * @param flexible if true, component will fill max width, otherwise get default width
     * @param testTag A test tag to find the internal stepper in a test
     * @param allowSemantics dictate if the specific stepper semantics should be applied or not
     */
    @Composable
    public fun Nudger(
        value: Int?,
        onValueChange: (Int) -> Unit,
        modifier: Modifier = Modifier,
        range: IntRange = 0..10,
        suffix: String = "",
        placeholder: String = "-",
        step: Int = 1,
        enabled: Boolean = true,
        flexible: Boolean = false,
        testTag: String? = null,
        allowSemantics: Boolean = true,
    ) {
        SparkNudger(
            value = value,
            onValueChange = onValueChange,
            modifier = modifier.sparkUsageOverlay(),
            range = range,
            suffix = suffix,
            placeholder = placeholder,
            step = step,
            enabled = enabled,
            flexible = flexible,
            testTag = testTag,
            allowSemantics = allowSemantics,
        )
    }

    /**
     * Nudger stepper driven by a [StepperState] holder.
     *
     * ![Nudger stepper](https://leboncoin.github.io/spark-android/images/com.adevinta.spark.components.stepper_StepperDocumentationScreenshots_nudger.png)
     *
     * @param state The [StepperState] that holds the current value, range, and step
     * @param modifier The [Modifier] to be applied to the component
     * @param suffix optional string displayed after the value
     * @param placeholder text shown in the empty state
     * @param enabled True controls the enabled state of the stepper
     * @param flexible if true, component will fill max width, otherwise get default width
     * @param testTag A test tag to find the internal stepper in a test
     * @param allowSemantics dictate if the specific stepper semantics should be applied or not
     */
    @Composable
    public fun Nudger(
        state: StepperState,
        modifier: Modifier = Modifier,
        suffix: String = "",
        placeholder: String = "-",
        enabled: Boolean = true,
        flexible: Boolean = false,
        testTag: String? = null,
        allowSemantics: Boolean = true,
    ) {
        SparkNudger(
            value = state.value,
            onValueChange = { state.value = it },
            modifier = modifier.sparkUsageOverlay(),
            range = state.range,
            suffix = suffix,
            placeholder = placeholder,
            step = state.step,
            enabled = enabled,
            flexible = flexible,
            testTag = testTag,
            allowSemantics = allowSemantics,
        )
    }

    /**
     * Nudger stepper with a label and helper text.
     *
     * ![Nudger form stepper](https://leboncoin.github.io/spark-android/images/com.adevinta.spark.components.stepper_StepperDocumentationScreenshots_nudgerForm.png)
     *
     * @param value Value of the quantity picker, or `null` for an empty state
     * @param onValueChange The callback to be called when [value] has been incremented or decremented
     * @param label the label to be displayed
     * @param helper The optional helper text to be displayed at the bottom outside the text input container
     * @param modifier The [Modifier] to be applied to the component
     * @param range The min/max accepted value by the stepper until it blocks increments and decrements
     * @param suffix optional string displayed after [value]
     * @param placeholder text shown in the empty state when [value] is `null`
     * @param step the quantity to be increased/decreased on each increment/decrement
     * @param enabled True controls the enabled state of the stepper
     * @param required add an asterisk to the label to indicate that this field is required
     * @param status indicates the validation state of the stepper
     * @param statusMessage the optional state text to be displayed at the helper position
     * @param flexible if true, component will fill max width, otherwise get default width
     * @param testTag A test tag to find the internal stepper in a test
     */
    @Composable
    public fun NudgerForm(
        value: Int?,
        onValueChange: (Int) -> Unit,
        label: String,
        helper: String?,
        modifier: Modifier = Modifier,
        range: IntRange = 0..10,
        suffix: String = "",
        placeholder: String = "-",
        step: Int = 1,
        enabled: Boolean = true,
        required: Boolean = false,
        status: FormFieldStatus? = null,
        statusMessage: String? = null,
        flexible: Boolean = false,
        testTag: String? = null,
    ) {
        StepperFormScaffold(
            label = label,
            helper = helper,
            required = required,
            status = status,
            statusMessage = statusMessage,
            enabled = enabled,
            modifier = modifier
                .stepperSemantics(value, onValueChange, range, step, suffix, enabled)
                .sparkUsageOverlay(overlayColor = Color.Green),
        ) {
            SparkNudger(
                value = value,
                onValueChange = onValueChange,
                range = range,
                enabled = enabled,
                placeholder = placeholder,
                suffix = suffix,
                step = step,
                flexible = flexible,
                testTag = testTag,
                allowSemantics = false,
            )
        }
    }

    /**
     * Nudger stepper with a label and helper text, driven by a [StepperState] holder.
     *
     * ![Nudger form stepper](https://leboncoin.github.io/spark-android/images/com.adevinta.spark.components.stepper_StepperDocumentationScreenshots_nudgerForm.png)
     *
     * @param state The [StepperState] that holds the current value, range, and step
     * @param label the label to be displayed
     * @param helper The optional helper text to be displayed at the bottom outside the text input container
     * @param modifier The [Modifier] to be applied to the component
     * @param suffix optional string displayed after the value
     * @param placeholder text shown in the empty state
     * @param enabled True controls the enabled state of the stepper
     * @param required add an asterisk to the label to indicate that this field is required
     * @param status indicates the validation state of the stepper
     * @param statusMessage the optional state text to be displayed at the helper position
     * @param flexible if true, component will fill max width, otherwise get default width
     * @param testTag A test tag to find the internal stepper in a test
     */
    @Composable
    public fun NudgerForm(
        state: StepperState,
        label: String,
        helper: String?,
        modifier: Modifier = Modifier,
        suffix: String = "",
        placeholder: String = "-",
        enabled: Boolean = true,
        required: Boolean = false,
        status: FormFieldStatus? = null,
        statusMessage: String? = null,
        flexible: Boolean = false,
        testTag: String? = null,
    ) {
        NudgerForm(
            value = state.value,
            onValueChange = { state.value = it },
            label = label,
            helper = helper,
            modifier = modifier.sparkUsageOverlay(),
            range = state.range,
            suffix = suffix,
            placeholder = placeholder,
            step = state.step,
            enabled = enabled,
            required = required,
            status = status,
            statusMessage = statusMessage,
            flexible = flexible,
            testTag = testTag,
        )
    }

    /**
     * Input stepper with an editable text field between decrease and increase buttons.
     *
     * ![Input stepper](https://leboncoin.github.io/spark-android/images/com.adevinta.spark.components.stepper_StepperDocumentationScreenshots_input.png)
     *
     * @param value Value of the stepper, or `null` for an empty state
     * @param onValueChange Called when the value changes (button press or blur commit)
     * @param modifier The [Modifier] to be applied to the component
     * @param range The min/max accepted value by the stepper
     * @param suffix optional string displayed after the value
     * @param placeholder text shown in the empty state when [value] is `null`
     * @param step the quantity to be increased/decreased on each increment/decrement
     * @param enabled True controls the enabled state of the stepper
     * @param status indicates the validation state of the stepper
     * @param flexible if true, component will fill max width
     * @param testTag A test tag to find the internal stepper in a test
     */
    @Composable
    public fun Input(
        value: Int?,
        onValueChange: (Int?) -> Unit,
        modifier: Modifier = Modifier,
        range: IntRange = 0..10,
        suffix: String = "",
        placeholder: String = "-",
        step: Int = 1,
        enabled: Boolean = true,
        status: FormFieldStatus? = null,
        flexible: Boolean = false,
        testTag: String? = null,
    ) {
        val textFieldState = remember { TextFieldState(value?.formatInteger().orEmpty()) }
        val currentInput = { textFieldState.text.toString().stripGroupingSeparators().toIntOrNull() }

        LaunchedEffect(value) {
            if (currentInput() != value) {
                textFieldState.edit {
                    delete(0, length)
                    insert(0, value?.formatInteger().orEmpty())
                }
            }
        }

        SparkStepperInput(
            textFieldState = textFieldState,
            value = value,
            onIncrement = { onValueChange(applyStep(currentInput(), step, range)) },
            onDecrement = { onValueChange(applyStep(currentInput(), -step, range)) },
            onCommit = { onValueChange(currentInput()?.coerceIn(range)) },
            modifier = modifier.sparkUsageOverlay(),
            range = range,
            suffix = suffix,
            placeholder = placeholder,
            step = step,
            enabled = enabled,
            status = status,
            flexible = flexible,
            testTag = testTag,
        )
    }

    /**
     * Input stepper driven by a [StepperInputState] holder.
     *
     * ![Input stepper](https://leboncoin.github.io/spark-android/images/com.adevinta.spark.components.stepper_StepperDocumentationScreenshots_input.png)
     *
     * @param state The [StepperInputState] that holds the text field state
     * @param modifier The [Modifier] to be applied to the component
     * @param range The min/max accepted value by the stepper
     * @param suffix optional string displayed after the value
     * @param placeholder text shown in the empty state
     * @param step the quantity to be increased/decreased on each increment/decrement
     * @param enabled True controls the enabled state of the stepper
     * @param status indicates the validation state of the stepper
     * @param flexible if true, component will fill max width
     * @param testTag A test tag to find the internal stepper in a test
     */
    @Composable
    public fun Input(
        state: StepperInputState,
        modifier: Modifier = Modifier,
        range: IntRange = 0..10,
        suffix: String = "",
        placeholder: String = "-",
        step: Int = 1,
        enabled: Boolean = true,
        status: FormFieldStatus? = null,
        flexible: Boolean = false,
        testTag: String? = null,
    ) {
        SparkStepperInput(
            textFieldState = state.textFieldState,
            value = state.value,
            onIncrement = { state.increment(step, range) },
            onDecrement = { state.decrement(step, range) },
            onCommit = { state.commitValue(range) },
            modifier = modifier.sparkUsageOverlay(),
            range = range,
            suffix = suffix,
            placeholder = placeholder,
            step = step,
            enabled = enabled,
            status = status,
            flexible = flexible,
            testTag = testTag,
        )
    }

    /**
     * Input stepper with a label and helper text.
     *
     * ![Input form stepper](https://leboncoin.github.io/spark-android/images/com.adevinta.spark.components.stepper_StepperDocumentationScreenshots_inputForm.png)
     *
     * @param value Value of the stepper, or `null` for an empty state
     * @param onValueChange Called when the value changes (button press or blur commit)
     * @param label the label to be displayed
     * @param helper The optional helper text to be displayed below the stepper
     * @param modifier The [Modifier] to be applied to the component
     * @param range The min/max accepted value by the stepper
     * @param suffix optional string displayed after the value
     * @param placeholder text shown in the empty state when [value] is `null`
     * @param step the quantity to be increased/decreased on each increment/decrement
     * @param enabled True controls the enabled state of the stepper
     * @param required add an asterisk to the label to indicate that this field is required
     * @param status indicates the validation state of the stepper
     * @param statusMessage the optional state text to be displayed at the helper position
     * @param flexible if true, component will fill max width
     * @param testTag A test tag to find the internal stepper in a test
     */
    @Composable
    public fun InputForm(
        value: Int?,
        onValueChange: (Int?) -> Unit,
        label: String,
        helper: String?,
        modifier: Modifier = Modifier,
        range: IntRange = 0..10,
        suffix: String = "",
        placeholder: String = "-",
        step: Int = 1,
        enabled: Boolean = true,
        required: Boolean = false,
        status: FormFieldStatus? = null,
        statusMessage: String? = null,
        flexible: Boolean = false,
        testTag: String? = null,
    ) {
        StepperFormScaffold(
            label = label,
            helper = helper,
            required = required,
            status = status,
            statusMessage = statusMessage,
            enabled = enabled,
            modifier = modifier.sparkUsageOverlay(),
        ) {
            Input(
                value = value,
                onValueChange = onValueChange,
                range = range,
                enabled = enabled,
                status = status,
                suffix = suffix,
                placeholder = placeholder,
                step = step,
                flexible = flexible,
                testTag = testTag,
            )
        }
    }

    /**
     * Input stepper with a label and helper text, driven by a [StepperInputState] holder.
     *
     * ![Input form stepper](https://leboncoin.github.io/spark-android/images/com.adevinta.spark.components.stepper_StepperDocumentationScreenshots_inputForm.png)
     *
     * @param state The [StepperInputState] that holds the text field state
     * @param label the label to be displayed
     * @param helper The optional helper text to be displayed below the stepper
     * @param modifier The [Modifier] to be applied to the component
     * @param range The min/max accepted value by the stepper
     * @param suffix optional string displayed after the value
     * @param placeholder text shown in the empty state
     * @param step the quantity to be increased/decreased on each increment/decrement
     * @param enabled True controls the enabled state of the stepper
     * @param required add an asterisk to the label to indicate that this field is required
     * @param status indicates the validation state of the stepper
     * @param statusMessage the optional state text to be displayed at the helper position
     * @param flexible if true, component will fill max width
     * @param testTag A test tag to find the internal stepper in a test
     */
    @Composable
    public fun InputForm(
        state: StepperInputState,
        label: String,
        helper: String?,
        modifier: Modifier = Modifier,
        range: IntRange = 0..10,
        suffix: String = "",
        placeholder: String = "-",
        step: Int = 1,
        enabled: Boolean = true,
        required: Boolean = false,
        status: FormFieldStatus? = null,
        statusMessage: String? = null,
        flexible: Boolean = false,
        testTag: String? = null,
    ) {
        StepperFormScaffold(
            label = label,
            helper = helper,
            required = required,
            status = status,
            statusMessage = statusMessage,
            enabled = enabled,
            modifier = modifier.sparkUsageOverlay(),
        ) {
            Input(
                state = state,
                range = range,
                enabled = enabled,
                status = status,
                suffix = suffix,
                placeholder = placeholder,
                step = step,
                flexible = flexible,
                testTag = testTag,
            )
        }
    }
}

/**
 * Shared form layout for the stepper form variants: a label row, the stepper [content], and a
 * helper/status line. The caller supplies [modifier] with the accessibility semantics and overlay
 * already applied; the scaffold adds the label text semantics.
 */
@Composable
private fun StepperFormScaffold(
    label: String,
    helper: String?,
    required: Boolean,
    status: FormFieldStatus?,
    statusMessage: String?,
    enabled: Boolean,
    modifier: Modifier = Modifier,
    content: @Composable () -> Unit,
) {
    val colors = StepperDefaults.stepperColors()
    val mandatoryDescription = if (required) {
        stringResource(id = R.string.spark_textfield_mandatory_content_description)
    } else {
        null
    }
    val interactionSource = remember { MutableInteractionSource() }

    Column(
        modifier = modifier.semantics {
            text = listOfNotNull(label, mandatoryDescription, helper)
                .joinToString(separator = " ")
                .let(::AnnotatedString)
        },
    ) {
        Row(modifier = Modifier.invisibleSemantic()) {
            Text(
                text = label,
                modifier = Modifier.weight(weight = 1f, fill = false),
                style = SparkTheme.typography.body2,
                color = colors.labelColor(enabled, interactionSource).value,
            )
            if (required) {
                EmphasizeDim3 {
                    Text(
                        text = "*",
                        modifier = Modifier.padding(start = 4.dp),
                        style = SparkTheme.typography.caption,
                        color = SparkTheme.colors.onSurface.dim1,
                    )
                }
            }
        }

        content()

        val stateIcon = TextFieldDefault.getStatusIcon(state = status)
        val color by colors.supportingTextColor(enabled, status, interactionSource)
        ProvideTextStyle(SparkTheme.typography.caption) {
            CompositionLocalProvider(
                LocalContentColor provides color,
            ) {
                supportText(
                    helper = helper,
                    status = status,
                    stateMessage = statusMessage,
                    stateIcon = stateIcon,
                )?.invoke()
            }
        }
    }
}

// region Deprecated top-level functions

/**
 * @deprecated Use [Stepper.Nudger] instead.
 */
@Deprecated(
    message = "Use Stepper.Nudger instead",
    replaceWith = ReplaceWith(
        "Stepper.Nudger(value = value, onValueChange = onValueChange, modifier = modifier, range = range, " +
                "suffix = suffix, step = step, enabled = enabled, flexible = flexible, " +
                "testTag = testTag, allowSemantics = allowSemantics)",
        "com.adevinta.spark.components.stepper.Stepper",
    ),
    level = DeprecationLevel.WARNING,
)
@Composable
public fun Stepper(
    value: Int,
    onValueChange: (Int) -> Unit,
    modifier: Modifier = Modifier,
    range: IntRange = 0..10,
    suffix: String = "",
    step: Int = 1,
    enabled: Boolean = true,
    @Suppress("UNUSED_PARAMETER") status: FormFieldStatus? = null,
    flexible: Boolean = false,
    testTag: String? = null,
    allowSemantics: Boolean = true,
) {
    Stepper.Nudger(
        value = value,
        onValueChange = onValueChange,
        modifier = modifier,
        range = range,
        suffix = suffix,
        step = step,
        enabled = enabled,
        flexible = flexible,
        testTag = testTag,
        allowSemantics = allowSemantics,
    )
}

/**
 * @deprecated Use [Stepper.NudgerForm] instead.
 */
@Deprecated(
    message = "Use Stepper.NudgerForm instead",
    replaceWith = ReplaceWith(
        "Stepper.NudgerForm(value = value, onValueChange = onValueChange, label = label, helper = helper, " +
                "modifier = modifier, range = range, suffix = suffix, step = step, enabled = enabled, " +
                "required = required, status = status, statusMessage = statusMessage, flexible = flexible, " +
                "testTag = testTag)",
        "com.adevinta.spark.components.stepper.Stepper",
    ),
    level = DeprecationLevel.WARNING,
)
@Composable
public fun StepperForm(
    value: Int,
    onValueChange: (Int) -> Unit,
    label: String,
    helper: String?,
    modifier: Modifier = Modifier,
    range: IntRange = 0..10,
    suffix: String = "",
    step: Int = 1,
    enabled: Boolean = true,
    required: Boolean = false,
    status: FormFieldStatus? = null,
    statusMessage: String? = null,
    flexible: Boolean = false,
    testTag: String? = null,
) {
    Stepper.NudgerForm(
        value = value,
        onValueChange = onValueChange,
        label = label,
        helper = helper,
        modifier = modifier,
        range = range,
        suffix = suffix,
        step = step,
        enabled = enabled,
        required = required,
        status = status,
        statusMessage = statusMessage,
        flexible = flexible,
        testTag = testTag,
    )
}

// endregion

/**
 * Adds semantics to a [Stepper] component, enabling accessibility features from TalkBack.
 *
 * This modifier configures the component to behave like a slider, allowing users to adjust the `value` within
 * the specified `range` using accessibility features from TalkBack.
 *
 * @param value The current value of the stepper.
 * @param onValueChange When the value has been incremented or decremented.
 * @param range The same range used with the [Stepper] or [StepperForm].
 * @param enabled Whether the stepper is enabled or disabled. Disabled steppers cannot be interacted with and will
 * be announced as disabled
 *
 * Usage Example:
 *
 * ```kotlin
 *  var stepperValue by remember { mutableStateOf(50) }
 *
 *  Row(
 *     Modifier
 *        .fillMaxWidth()
 *        .semantics { text = label }
 *        .stepperSemantics(
 *            value = stepperValue,
 *            onValueChange = { stepperValue = it },
 *            range = 0..100,
 *            enabled = true
 *         )
 *   ) {
 *     Text(
 *       text = label,
 *       modifier = Modifier.invisibleSemantic()
 *     )
 *     Stepper(
 *       ...
 *          allowSemantics = false // Important otherwise the semantics will be duplicated
 *       ...
 *      )
 *   }
 *
 * ```
 */
// TODO-scott.rayapoulle.ext (30-01-2025): Move to spark a11y lib once it's initiated
public fun Modifier.stepperSemantics(
    value: Int?,
    onValueChange: (Int) -> Unit,
    range: IntRange,
    step: Int,
    suffix: String?,
    enabled: Boolean,
): Modifier = semantics(mergeDescendants = true) {
    stepperInputValidator(step = step, range = range)
    // this is needed to use volume keys or slide up / down
    setProgress { targetValue ->
        // without this rounding the values will only decrease
        val newValue = targetValue
            .roundToInt()
            .coerceIn(range)
        if (newValue != value) {
            onValueChange(newValue)
            true
        } else {
            false
        }
    }

    // override describing percents
    stateDescription = stepperStateDescription(value, suffix, emptyText = "")

    if (!enabled) disabled()
}
    .progressSemantics(
        // this is needed to use volume keys or slide up / down
        value = (value ?: range.first).toFloat(),
        valueRange = range.first.toFloat()..range.last.toFloat(),
        steps = (range.last - range.first) / step,
    )
    .onKeyEvent {
        // Should not be possible with Stepper & StepperForm but could happen with custom impl
        if (!enabled) return@onKeyEvent false

        val isUpKey = it.key == Key.DirectionUp
        val isDownKey = it.key == Key.DirectionDown
        val isShiftOnlyPressed = it.isShiftPressed && !it.isCtrlPressed && !it.isAltPressed && !it.isMetaPressed
        if (it.type == KeyEventType.KeyDown && isShiftOnlyPressed) {
            when {
                isUpKey -> onValueChange(applyStep(value, step, range))
                isDownKey -> onValueChange(applyStep(value, -step, range))
                else -> return@onKeyEvent false
            }
            true
        } else {
            false
        }
    }

public object StepperDefaults {
    @Composable
    internal fun stepperColors() = sparkOutlinedTextFieldColors(
        unfocusedBorderColor = SparkTheme.colors.outline,
        containerColor = SparkTheme.colors.onSurface.transparent,
        disabledContainerColor = SparkTheme.colors.onSurface.dim5,
    )

    internal val textAnimationSpec = spring(
        stiffness = Spring.StiffnessMediumLow,
        visibilityThreshold = IntOffset.VisibilityThreshold,
    )
}

@Suppress("DEPRECATION")
@Preview
@Composable
private fun StepperPreview() {
    PreviewTheme {
        Stepper(
            value = 1234,
            onValueChange = {},
        )
        StepperForm(
            value = 1,
            onValueChange = {},
            status = FormFieldStatus.Error,
            label = "Label",
            helper = "helper message",
        )
    }
}

@Preview
@Composable
private fun NudgerPreview() {
    PreviewTheme {
        Stepper.Nudger(
            value = 1234,
            onValueChange = {},
        )
        Stepper.Nudger(
            value = null,
            onValueChange = {},
        )
        Stepper.NudgerForm(
            value = 1,
            onValueChange = {},
            status = FormFieldStatus.Error,
            label = "Label",
            helper = "helper message",
        )
        Stepper.NudgerForm(
            value = null,
            onValueChange = {},
            status = FormFieldStatus.Alert,
            label = "Label",
            helper = "helper message",
        )
        Stepper.NudgerForm(
            value = -1234,
            onValueChange = {},
            status = FormFieldStatus.Success,
            label = "Label",
            helper = "helper message",
        )
    }
}

@Preview
@Composable
private fun InputPreview() {
    PreviewTheme {
        Stepper.Input(
            value = 1234,
            onValueChange = {},
        )
        Stepper.Input(
            value = null,
            onValueChange = {},
        )
        Stepper.InputForm(
            value = 5,
            onValueChange = {},
            label = "Quantity",
            helper = "Enter a value",
        )
        Stepper.InputForm(
            value = null,
            onValueChange = {},
            status = FormFieldStatus.Error,
            statusMessage = "Required field",
            label = "Quantity",
            helper = "Enter a value",
        )
    }
}
