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
package com.adevinta.spark.catalog

import android.app.UiModeManager
import android.app.assist.AssistContent
import android.os.Bundle
import android.util.Log
import android.view.KeyEvent
import android.view.KeyboardShortcutGroup
import android.view.KeyboardShortcutInfo
import android.view.Menu
import androidx.activity.SystemBarStyle
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.core.content.getSystemService
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.metrics.performance.JankStats
import androidx.navigation.NavController
import com.adevinta.spark.catalog.datastore.theme.ThemePropertiesHandler
import com.adevinta.spark.catalog.datastore.theme.collectAsStateWithDefault
import com.adevinta.spark.catalog.model.Components
import com.adevinta.spark.catalog.themes.ThemeMode
import com.adevinta.spark.catalog.ui.navigation.provideAssistContent
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

public class MainActivity : AppCompatActivity() {

    internal var activeNavController: NavController? = null

    // JankStats usage taken from NIA but it'll help detect issues in the catalog app like the backdrop front layer.
    internal val jankStats: JankStats by lazy(LazyThreadSafetyMode.NONE) {
        JankStats.createAndTrack(window) { frameData ->
            // Make sure to only log janky frames.
            if (frameData.isJank) {
                // We're currently logging this but would better report it to a backend.
                Log.v("Catalog Jank", frameData.toString())
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // Turn off the decor fitting system windows, which allows us to handle insets,
        // including IME animations, and go edge-to-edge
        // This also sets up the initial system bar style based on the platform theme
//        enableEdgeToEdge()
        val uiModeManager = getSystemService<UiModeManager>()
        val propertiesHandler = ThemePropertiesHandler(context = this@MainActivity)

        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.CREATED) {
                propertiesHandler
                    .properties
                    .collectLatest { theme ->
                        val isSystemDark = uiModeManager?.nightMode == UiModeManager.MODE_NIGHT_AUTO
                        val useDark =
                            (theme.themeMode == ThemeMode.System && isSystemDark) || theme.themeMode == ThemeMode.Dark
                        enableEdgeToEdge(
                            statusBarStyle = SystemBarStyle.auto(
                                android.graphics.Color.TRANSPARENT,
                                android.graphics.Color.TRANSPARENT,
                            ) { useDark },
                            navigationBarStyle = SystemBarStyle.auto(
                                lightScrim,
                                darkScrim,
                            ) { useDark },
                        )
                        if (theme.themeMode != ThemeMode.System) {
                            AppCompatDelegate.setDefaultNightMode(
                                if (useDark) {
                                    AppCompatDelegate.MODE_NIGHT_YES
                                } else {
                                    AppCompatDelegate.MODE_NIGHT_NO
                                },
                            )
                        }
                    }
            }
        }

        setContent {
            val coroutineScope = rememberCoroutineScope()
            val theme by propertiesHandler
                .properties
                .collectAsStateWithDefault(this@MainActivity)

            CatalogApp(
                theme = theme,
                components = Components,
                onThemeChange = {
                    coroutineScope.launch {
                        propertiesHandler.updateProperties(it)
                    }
                },
            )
        }
    }

    override fun onResume() {
        super.onResume()
        jankStats.isTrackingEnabled = true
    }

    override fun onPause() {
        super.onPause()
        jankStats.isTrackingEnabled = false
    }

    override fun onProvideKeyboardShortcuts(
        data: MutableList<KeyboardShortcutGroup>?,
        menu: Menu?,
        deviceId: Int,
    ) {
        if (data != null) {
            val shortcutGroup = KeyboardShortcutGroup(
                "Stepper",
                listOf(
                    KeyboardShortcutInfo("Increase", KeyEvent.KEYCODE_DPAD_UP, KeyEvent.META_SHIFT_ON),
                    KeyboardShortcutInfo("Decrease", KeyEvent.KEYCODE_DPAD_DOWN, KeyEvent.META_SHIFT_ON),
                ),
            )
            data.add(shortcutGroup)
        }
    }

    override fun onProvideAssistContent(outContent: AssistContent) {
        super.onProvideAssistContent(outContent)
        activeNavController?.provideAssistContent(outContent = outContent)
    }
}

/**
 * The default light scrim, as defined by androidx and the platform:
 * https://cs.android.com/androidx/platform/frameworks/support/+/androidx-main:activity/activity/src/main/java/androidx/activity/EdgeToEdge.kt;l=35-38;drc=27e7d52e8604a080133e8b842db10c89b4482598
 */
private val lightScrim = android.graphics.Color.argb(0xe6, 0xFF, 0xFF, 0xFF)

/**
 * The default dark scrim, as defined by androidx and the platform:
 * https://cs.android.com/androidx/platform/frameworks/support/+/androidx-main:activity/activity/src/main/java/androidx/activity/EdgeToEdge.kt;l=40-44;drc=27e7d52e8604a080133e8b842db10c89b4482598
 */
private val darkScrim = android.graphics.Color.argb(0x80, 0x1b, 0x1b, 0x1b)
