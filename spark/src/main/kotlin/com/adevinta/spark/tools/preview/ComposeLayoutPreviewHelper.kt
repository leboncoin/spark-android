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
package com.adevinta.spark.tools.preview

import android.os.Bundle
import android.view.View
import androidx.compose.runtime.MonotonicFrameClock
import androidx.compose.runtime.PausableMonotonicFrameClock
import androidx.compose.runtime.Recomposer
import androidx.compose.ui.platform.AbstractComposeView
import androidx.compose.ui.platform.AndroidUiDispatcher
import androidx.compose.ui.platform.compositionContext
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.LifecycleRegistry
import androidx.lifecycle.ViewModelStore
import androidx.lifecycle.ViewModelStoreOwner
import androidx.lifecycle.setViewTreeLifecycleOwner
import androidx.lifecycle.setViewTreeViewModelStoreOwner
import androidx.savedstate.SavedStateRegistry
import androidx.savedstate.SavedStateRegistryController
import androidx.savedstate.SavedStateRegistryOwner
import androidx.savedstate.setViewTreeSavedStateRegistryOwner
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.CoroutineStart
import kotlinx.coroutines.DelicateCoroutinesApi
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.android.asCoroutineDispatcher
import kotlinx.coroutines.launch
import kotlin.coroutines.EmptyCoroutineContext

/**
 * Call this in onAttachedToWindow() like this:
 * ```kotlin
 *  override fun onAttachedToWindow() {
 *      if (isInEditMode) {
 *          setupEditMode()
 *      }
 *
 *      super.onAttachedToWindow()
 *  }
 * ```
 */
public fun AbstractComposeView.setupEditMode() {
    ComposeLayoutPreviewHelper(this).createAndInstallWindowRecomposer()
}

/**
 * Initializes a barebone Recomposer with a fake SavedStateRegisterOwner
 * and a fake ViewModelStoreOwner.
 *
 * Most of this code is taken from [ComposeViewAdapter] and [ComposeView].
 */
private class ComposeLayoutPreviewHelper(val view: AbstractComposeView) {

    private val fakeSavedStateRegistryOwner = object : SavedStateRegistryOwner {
        override val lifecycle = LifecycleRegistry(this)
        private val controller = SavedStateRegistryController.create(this).apply {
            performRestore(Bundle())
        }

        init {
            // Starts the recomposition.
            lifecycle.currentState = Lifecycle.State.RESUMED
        }

        override val savedStateRegistry: SavedStateRegistry
            get() = controller.savedStateRegistry
    }

    private val fakeViewModelStoreOwner = object : ViewModelStoreOwner {
        override val viewModelStore = ViewModelStore()
    }

    init {
        val stateRegistryOwner = fakeSavedStateRegistryOwner
        val viewModelStoreOwner = fakeViewModelStoreOwner
        view.setViewTreeLifecycleOwner(stateRegistryOwner)
        view.setViewTreeSavedStateRegistryOwner(stateRegistryOwner)
        view.setViewTreeViewModelStoreOwner(viewModelStoreOwner)
    }

    @OptIn(DelicateCoroutinesApi::class)
    fun createAndInstallWindowRecomposer(
        rootView: View = view,
        lifecycleOwner: LifecycleOwner = fakeSavedStateRegistryOwner,
    ): Recomposer {
        val newRecomposer = createViewTreeRecomposer(lifecycleOwner = lifecycleOwner)
        rootView.compositionContext = newRecomposer

        // If the Recomposer shuts down, unregister it so that a future request for a window
        // recomposer will consult the factory for a new one.
        val unsetJob = GlobalScope.launch(
            rootView.handler.asCoroutineDispatcher("windowRecomposer cleanup").immediate,
        ) {
            try {
                newRecomposer.join()
            } finally {
                // Unset if the view is detached. (See below for the attach state change listener.)
                // Since this is in a finally in this coroutine, even if this job is cancelled we
                // will resume on the window's UI thread and perform this manipulation there.
                val viewTagRecomposer = rootView.compositionContext
                if (viewTagRecomposer === newRecomposer) {
                    rootView.compositionContext = null
                }
            }
        }

        // If the root view is detached, cancel the await for recomposer shutdown above.
        // This will also unset the tag reference to this recomposer during its cleanup.
        rootView.addOnAttachStateChangeListener(
            object : View.OnAttachStateChangeListener {
                override fun onViewAttachedToWindow(v: View) {}
                override fun onViewDetachedFromWindow(v: View) {
                    v.removeOnAttachStateChangeListener(this)
                    // cancel the job to clean up the view tags.
                    // this will happen immediately since unsetJob is on an immediate dispatcher
                    // for this view's UI thread instead of waiting for the recomposer to join.
                    // NOTE: This does NOT cancel the returned recomposer itself, as it may be
                    // a shared-instance recomposer that should remain running/is reused elsewhere.
                    unsetJob.cancel()
                }
            },
        )
        return newRecomposer
    }

    private fun createViewTreeRecomposer(
        lifecycleOwner: LifecycleOwner,
    ): Recomposer {
        val currentThreadContext = AndroidUiDispatcher.CurrentThread
        val pausableClock = currentThreadContext[MonotonicFrameClock]?.let {
            PausableMonotonicFrameClock(it).apply { pause() }
        }
        val contextWithClock = currentThreadContext + (pausableClock ?: EmptyCoroutineContext)
        val recomposer = Recomposer(contextWithClock)
        val runRecomposeScope = CoroutineScope(contextWithClock)

        lifecycleOwner.lifecycle.addObserver(
            LifecycleEventObserver { _, event ->
                when (event) {
                    Lifecycle.Event.ON_CREATE ->
                        // Undispatched launch since we've configured this scope
                        // to be on the UI thread
                        runRecomposeScope.launch(start = CoroutineStart.UNDISPATCHED) {
                            recomposer.runRecomposeAndApplyChanges()
                        }

                    Lifecycle.Event.ON_START -> pausableClock?.resume()

                    Lifecycle.Event.ON_STOP -> pausableClock?.pause()

                    Lifecycle.Event.ON_DESTROY -> recomposer.cancel()

                    Lifecycle.Event.ON_RESUME,
                    Lifecycle.Event.ON_PAUSE,
                    Lifecycle.Event.ON_ANY,
                    -> {
                        // Do nothing.
                    }
                }
            },
        )

        return recomposer
    }
}
