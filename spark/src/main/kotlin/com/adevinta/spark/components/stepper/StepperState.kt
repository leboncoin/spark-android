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

import androidx.compose.foundation.text.input.TextFieldState
import androidx.compose.foundation.text.input.delete
import androidx.compose.foundation.text.input.insert
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Stable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.Saver
import androidx.compose.runtime.saveable.mapSaver
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import com.adevinta.spark.components.stepper.internal.formatInteger
import com.adevinta.spark.components.stepper.internal.stripGroupingSeparators

// region StepperState (Nudger)

/**
 * State holder for [Stepper.Nudger] and [Stepper.NudgerForm].
 *
 * @param initialValue Starting value, or `null` for an empty state.
 * @param range Accepted value bounds. Buttons disable at the limits.
 * @param step Amount added or subtracted per increment/decrement.
 */
@Stable
public class StepperState(initialValue: Int?, public val range: IntRange, public val step: Int) {
    /** Current value, or `null` when the stepper is empty. */
    public var value: Int? by mutableStateOf(initialValue)

    public val canIncrement: Boolean
        get() = canIncrement(value, range)

    public val canDecrement: Boolean
        get() = canDecrement(value, range)

    public fun increment() {
        value = applyStep(value, step, range)
    }

    public fun decrement() {
        value = applyStep(value, -step, range)
    }

    public companion object {
        public val Saver: Saver<StepperState, Any> = mapSaver(
            save = { state ->
                mapOf(
                    "value" to state.value,
                    "rangeFirst" to state.range.first,
                    "rangeLast" to state.range.last,
                    "step" to state.step,
                )
            },
            restore = { map ->
                StepperState(
                    initialValue = map["value"] as? Int,
                    range = (map["rangeFirst"] as Int)..(map["rangeLast"] as Int),
                    step = map["step"] as Int,
                )
            },
        )
    }
}

/**
 * Creates and remembers a [StepperState] that survives recomposition and state restoration.
 *
 * @param initialValue Starting value, or `null` for an empty state.
 * @param range Accepted value bounds.
 * @param step Amount added or subtracted per increment/decrement.
 */
@Composable
public fun rememberStepperState(
    initialValue: Int? = null,
    range: IntRange = 0..10,
    step: Int = 1,
): StepperState = rememberSaveable(saver = StepperState.Saver) {
    StepperState(initialValue = initialValue, range = range, step = step)
}

// endregion

// region StepperInputState (Input)

/**
 * State holder for [Stepper.Input] and [Stepper.InputForm].
 *
 * Wraps a [TextFieldState] and exposes the parsed integer value.
 *
 * @param textFieldState The underlying text field state. Use [rememberStepperInputState] to
 * create one with a saveable initial value.
 */
@Stable
public class StepperInputState(public val textFieldState: TextFieldState) {
    /**
     * The current parsed value, or `null` when the text field is empty or contains non-numeric text.
     */
    public val value: Int?
        get() {
            val raw = textFieldState.text.toString()
            if (raw.isEmpty()) return null
            return raw.stripGroupingSeparators().toIntOrNull()
        }

    /**
     * Clamps [value] to [range] and writes it back to the text field.
     * Clears the field when [value] is `null`.
     *
     * @param range The range to clamp to.
     */
    public fun commitValue(range: IntRange) {
        val parsed = value
        setTextFieldText(if (parsed == null) "" else parsed.coerceIn(range).formatInteger())
    }

    /**
     * Increments [value] by [step], clamped to [range], and writes the result to the text field.
     * When [value] is `null`, snaps to [range].first without applying the step.
     *
     * @param step The amount to add.
     * @param range The range to clamp to.
     */
    public fun increment(step: Int, range: IntRange) {
        setTextFieldText(applyStep(value, step, range).formatInteger())
    }

    /**
     * Decrements [value] by [step], clamped to [range], and writes the result to the text field.
     * When [value] is `null`, snaps to [range].first without applying the step.
     *
     * @param step The amount to subtract.
     * @param range The range to clamp to.
     */
    public fun decrement(step: Int, range: IntRange) {
        setTextFieldText(applyStep(value, -step, range).formatInteger())
    }

    private fun setTextFieldText(text: String) {
        textFieldState.edit {
            delete(0, length)
            insert(0, text)
        }
    }

    public companion object {
        public val Saver: Saver<StepperInputState, Any> = Saver(
            save = { state -> with(TextFieldState.Saver) { save(state.textFieldState) } },
            restore = { saved ->
                val textFieldState = TextFieldState.Saver.restore(saved) ?: return@Saver null
                StepperInputState(textFieldState)
            },
        )
    }
}

/**
 * Creates and remembers a [StepperInputState] that survives recomposition and state restoration.
 *
 * @param initialValue Starting value, or `null` for an empty state (shows the placeholder).
 */
@Composable
public fun rememberStepperInputState(
    initialValue: Int? = null,
): StepperInputState {
    val initialText = initialValue?.formatInteger().orEmpty()
    return rememberSaveable(saver = StepperInputState.Saver) {
        StepperInputState(TextFieldState(initialText))
    }
}

// endregion

/**
 * When current is null, snaps to range.first (no step applied).
 * When current is non-null, applies delta and coerces into range.
 */
internal fun applyStep(current: Int?, delta: Int, range: IntRange): Int =
    if (current == null) range.first else (current + delta).coerceIn(range)

internal fun canIncrement(value: Int?, range: IntRange): Boolean =
    if (value == null) range.first < range.last else value < range.last

internal fun canDecrement(value: Int?, range: IntRange): Boolean =
    if (value == null) range.first < range.last else value > range.first
