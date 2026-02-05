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

import androidx.compose.material3.SnackbarData
import androidx.compose.material3.SnackbarDefaults
import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarResult
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.Stable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalAccessibilityManager
import com.adevinta.spark.icons.SparkIcon
import kotlinx.coroutines.CancellableContinuation
import kotlinx.coroutines.delay
import kotlinx.coroutines.suspendCancellableCoroutine
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock
import kotlin.coroutines.resume

/**
 * Host for [Snackbar]s to be used in [Scaffold] to properly show, hide and dismiss items based
 * on Material specification and the [hostState].
 *
 * This component with default parameters comes build-in with [Scaffold], if you need to show a
 * default [Snackbar], use [SnackbarHostState.showSnackbar].
 *
 * @param hostState state of this component to read and show [Snackbar]s accordingly
 * @param modifier the [Modifier] to be applied to this component
 * @param snackbar the instance of the [Snackbar] to be shown at the appropriate time with
 * appearance based on the [SnackbarData] provided as a param
 */
@Composable
public fun SnackbarHost(
    hostState: SnackbarHostState,
    modifier: Modifier = Modifier,
    snackbar: @Composable (SnackbarData) -> Unit = { Snackbar(it) },
) {
    val currentSnackbarData = hostState.currentSnackbarData
    val accessibilityManager = LocalAccessibilityManager.current
    LaunchedEffect(currentSnackbarData) {
        if (currentSnackbarData != null) {
            val duration = currentSnackbarData.visuals.duration.toMillis(
                currentSnackbarData.visuals.actionLabel != null,
                accessibilityManager,
            )
            delay(duration)
            currentSnackbarData.dismiss()
        }
    }
    FadeInFadeOutWithScale(
        current = hostState.currentSnackbarData,
        modifier = modifier,
        content = snackbar,
    )
}

/**
 * State of the [SnackbarHost], which controls the queue and the current [Snackbar] being shown
 * inside the [SnackbarHost].
 *
 * This state is usually [remember]ed and used to provide a
 * [SnackbarHost] to a [Scaffold].
 */
@Stable
public class SnackbarHostState {
    private val mutex = Mutex()

    public var currentSnackbarData: SnackbarData? by mutableStateOf(null)
        private set

    /**
     * Shows or queues to be shown a [Snackbar] at the bottom of
     * the [Scaffold] to which this state
     * is attached and suspends until the snackbar has disappeared.
     *
     * [SnackbarHostState] guarantees to show at most one snackbar at a time. If this function is
     * called while another snackbar is already visible, it will be suspended until this snackbar is
     * shown and subsequently addressed. If the caller is cancelled, the snackbar will be removed
     * from display and/or the queue to be displayed.
     *
     * To change the Snackbar appearance, change it in 'snackbarHost'
     * on the [Scaffold].
     * @param message The message to display in the Snackbar.
     * @param icon The icon to display in the Snackbar.
     * @param intent The intent of the Snackbar.
     * @param style The style of the Snackbar.
     * @param actionLabel optional action label to show as button in the Snackbar
     * @param withDismissAction a boolean to show a dismiss action in the Snackbar. This is
     * recommended to be set to true for better accessibility when a Snackbar is set with a
     * [SnackbarDuration.Indefinite]
     * @param duration duration to control how long snackbar will be shown in [SnackbarHost], either
     * [SnackbarDuration.Short], [SnackbarDuration.Long] or [SnackbarDuration.Indefinite].
     *
     * @return [SnackbarResult.ActionPerformed] if option action has been clicked or
     * [SnackbarResult.Dismissed] if snackbar has been dismissed via timeout or by the user
     */
    public suspend fun showSnackbar(
        message: String,
        actionLabel: String? = null,
        icon: SparkIcon? = null,
        intent: SnackbarIntent = SnackbarDefaults.intent,
        withDismissAction: Boolean = false,
        actionOnNewLine: Boolean = false,
        duration: SnackbarDuration = if (actionLabel == null) {
            SnackbarDuration.Short
        } else {
            SnackbarDuration.Indefinite
        },
    ): SnackbarResult = showSnackbar(
        SnackbarSparkVisuals(
            message = message,
            intent = intent,
            actionLabel = actionLabel,
            withDismissAction = withDismissAction,
            actionOnNewLine = actionOnNewLine,
            duration = duration,
        ),
    )

    /**
     * Shows or queues to be shown a [Snackbar] at
     * the bottom of the [Scaffold] to which this state
     * is attached and suspends until the snackbar has disappeared.
     *
     * [SnackbarHostState] guarantees to show at most one snackbar at a time. If this function is
     * called while another snackbar is already visible, it will be suspended until this snackbar is
     * shown and subsequently addressed. If the caller is cancelled, the snackbar will be removed
     * from display and/or the queue to be displayed.
     *
     * @param visuals [SnackbarSparkVisuals] that are used to create a Snackbar
     *
     * @return [SnackbarResult.ActionPerformed] if option action has been clicked or
     * [SnackbarResult.Dismissed] if snackbar has been dismissed via timeout or by the user
     */
    public suspend fun showSnackbar(visuals: SnackbarSparkVisuals): SnackbarResult = mutex.withLock {
        try {
            return suspendCancellableCoroutine { continuation ->
                currentSnackbarData = SnackbarDataImpl(visuals, continuation)
            }
        } finally {
            currentSnackbarData = null
        }
    }

    /**
     * An internal class that represents the data of a particular Snackbar.
     * @param visuals Holds the visual representation for a particular [Snackbar]
     * @param continuation handle the coroutine to give back the action resulting on the continuation
     * of the coroutine where the Snackbar has been shown to either notify a action performed or a dismissed
     */
    internal class SnackbarDataImpl(
        override val visuals: SnackbarSparkVisuals,
        private val continuation: CancellableContinuation<SnackbarResult>,
    ) : SnackbarData {

        /**
         *Function to be called when Snackbar action has been performed to notify the listeners
         */
        override fun performAction() {
            if (continuation.isActive) continuation.resume(SnackbarResult.ActionPerformed)
        }

        /**
         * Dismisses the Snackbar.
         * Function to be called when Snackbar is dismissed either by timeout or by the user
         */
        override fun dismiss() {
            if (continuation.isActive) continuation.resume(SnackbarResult.Dismissed)
        }

        override fun equals(other: Any?): Boolean {
            if (this === other) return true
            if (other == null || this::class != other::class) return false

            other as SnackbarDataImpl

            if (visuals != other.visuals) return false
            if (continuation != other.continuation) return false

            return true
        }

        override fun hashCode(): Int {
            var result = visuals.hashCode()
            result = 31 * result + continuation.hashCode()
            return result
        }
    }
}
