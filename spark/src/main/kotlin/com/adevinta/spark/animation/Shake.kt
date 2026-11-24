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
package com.adevinta.spark.animation

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.spring
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.TransformOrigin
import androidx.compose.ui.layout.Measurable
import androidx.compose.ui.layout.MeasureResult
import androidx.compose.ui.layout.MeasureScope
import androidx.compose.ui.node.LayoutModifierNode
import androidx.compose.ui.node.ModifierNodeElement
import androidx.compose.ui.node.invalidatePlacement
import androidx.compose.ui.platform.InspectorInfo
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Constraints
import androidx.compose.ui.unit.LayoutDirection
import com.adevinta.spark.PreviewTheme
import com.adevinta.spark.components.buttons.Button
import com.adevinta.spark.components.buttons.Danger
import com.adevinta.spark.components.buttons.Success
import com.adevinta.spark.components.buttons.Tertiary
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.launch
import kotlin.math.roundToInt

/**
 * Creates and remembers a [ShakeController] instance.
 *
 * This function is used to create a controller that can be used to trigger shake animations
 * on a composable. The controller is remembered across recompositions.
 *
 * @return A [ShakeController] instance.
 * @see ShakeController
 * @see Modifier.shake
 */
@Composable
public fun rememberShakeController(): ShakeController = remember { ShakeController() }

/**
 * Controller for managing shake animations.
 *
 * This class allows you to trigger shake animations with a specific [ShakeConfig].
 * You can create an instance of this controller using [rememberShakeController].
 *
 * @see ShakeConfig
 * @see Modifier.shake
 */
public class ShakeController {
    public var shakeConfig: ShakeConfig? by mutableStateOf(null)
        private set

    public fun shake(shakeConfig: ShakeConfig) {
        this.shakeConfig = shakeConfig
    }
}

/**
 * Represent the configuration for a shake animation.
 *
 * @property iterations The number of times the animation should repeat.
 * @property intensity The intensity of the shake animation. Higher values result in a more pronounced shake.
 * @property rotate The amount of rotation to apply around the Z-axis during the shake.
 * @property rotateX The amount of rotation to apply around the X-axis during the shake.
 * @property rotateY The amount of rotation to apply around the Y-axis during the shake.
 * @property scaleX The amount to scale the composable along the X-axis during the shake.
 * @property scaleY The amount to scale the composable along the Y-axis during the shake.
 * @property translateX The amount to translate the composable along the X-axis during the shake.
 * @property translateY The amount to translate the composable along the Y-axis during the shake.
 * @property trigger A timestamp used to trigger the animation. Defaults to the current system time.
 * This is useful for re-triggering the animation with the same configuration.
 */
public data class ShakeConfig(
    val iterations: Int,
    val intensity: Float = 100_000f,
    val rotate: Float = 0f,
    val rotateX: Float = 0f,
    val rotateY: Float = 0f,
    val scaleX: Float = 0f,
    val scaleY: Float = 0f,
    val translateX: Float = 0f,
    val translateY: Float = 0f,
    val trigger: Long = System.currentTimeMillis(),
)

/**
 * Applies a shake animation to the composable when triggered by the [shakeController].
 *
 * This modifier uses the [ShakeConfig] provided by the [shakeController] to determine
 * the behavior of the shake animation. The animation includes rotation, scaling, and translation
 * effects based on the configuration.
 *
 * @param shakeController The controller that manages the shake animation.
 * @return A [Modifier] that applies the shake animation.
 * @see ShakeController
 * @see ShakeConfig
 * @see rememberShakeController
 */
public fun Modifier.shake(shakeController: ShakeController): Modifier = this then ShakeElement(shakeController)

private data class ShakeElement(val shakeController: ShakeController) : ModifierNodeElement<ShakeNode>() {
    override fun create(): ShakeNode = ShakeNode(shakeController)

    override fun update(node: ShakeNode) {
        node.update(shakeController)
    }

    override fun InspectorInfo.inspectableProperties() {
        name = "shake"
        properties["shakeController"] = shakeController
    }
}

/**
 * One layout node does the work of the former `graphicsLayer { }.offset { }` chain. It places the content
 * with a layer at the shake offset, and moves the layer pivot back by that offset. The rotation and the
 * scale then use the centre of the unmoved content as their pivot, as the outer `graphicsLayer` did.
 */
private class ShakeNode(private var shakeController: ShakeController) :
    Modifier.Node(),
    LayoutModifierNode {

    private var shake = Animatable(0f)
    private var shakeJob: Job? = null

    // The config of the last started animation. A new controller with an equal config does not restart it,
    // as the former LaunchedEffect key did not change.
    private var lastConfig: ShakeConfig? = null

    override val shouldAutoInvalidate: Boolean = false

    fun update(shakeController: ShakeController) {
        if (this.shakeController === shakeController) return
        this.shakeController = shakeController
        if (isAttached) launchShake()
        invalidatePlacement()
    }

    override fun onAttach() {
        launchShake()
    }

    override fun onDetach() {
        shakeJob = null
        lastConfig = null
    }

    override fun onReset() {
        shake = Animatable(0f)
    }

    private fun launchShake() {
        shakeJob?.cancel()
        val controller = shakeController
        shakeJob = coroutineScope.launch {
            snapshotFlow { controller.shakeConfig }
                .filterNotNull()
                .filter { it != lastConfig }
                .collectLatest { shakeConfig ->
                    lastConfig = shakeConfig
                    for (i in 0..shakeConfig.iterations) {
                        when (i % 2) {
                            0 -> shake.animateTo(1f, spring(stiffness = shakeConfig.intensity))
                            else -> shake.animateTo(-1f, spring(stiffness = shakeConfig.intensity))
                        }
                    }
                    shake.animateTo(0f)
                }
        }
    }

    override fun MeasureScope.measure(measurable: Measurable, constraints: Constraints): MeasureResult {
        val placeable = measurable.measure(constraints)
        return layout(placeable.width, placeable.height) {
            val shakeConfig = shakeController.shakeConfig
            if (shakeConfig == null) {
                placeable.place(0, 0)
                return@layout
            }
            val value = shake.value
            val translationX = (value * shakeConfig.translateX).roundToInt()
            val translationY = (value * shakeConfig.translateY).roundToInt()
            // The former offset { } placed the content relative to the layout direction.
            val x = if (layoutDirection == LayoutDirection.Rtl) -translationX else translationX
            val width = placeable.width
            val height = placeable.height
            placeable.placeWithLayer(x, translationY) {
                rotationX = value * shakeConfig.rotateX
                rotationY = value * shakeConfig.rotateY
                scaleX = 1f + (value * shakeConfig.scaleX)
                scaleY = 1f + (value * shakeConfig.scaleY)
                rotationZ = value * shakeConfig.rotate
                transformOrigin = TransformOrigin(
                    pivotFractionX = if (width > 0) 0.5f - x.toFloat() / width else 0.5f,
                    pivotFractionY = if (height > 0) 0.5f - translationY.toFloat() / height else 0.5f,
                )
            }
        }
    }
}

@Composable
@Preview
private fun Shaker() {
    PreviewTheme {
        Column(
            modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.SpaceEvenly,
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            var isValid by remember { mutableStateOf(true) }
            val shakeController = rememberShakeController()
            val validShakeConfig = if (isValid) {
                ShakeConfig(
                    iterations = 4,
                    intensity = 2_000f,
                    rotateY = 15f,
                    translateX = 40f,
                )
            } else {
                ShakeConfig(
                    iterations = 4,
                    intensity = 1_000f,
                    rotateX = -20f,
                    translateY = 20f,
                )
            }

            val onShakeClick = {
                shakeController.shake(validShakeConfig)
                isValid = !isValid
            }
            Box(
                modifier = Modifier.shake(shakeController),
            ) {
                if (isValid) {
                    Button.Success(
                        onClick = onShakeClick,
                        text = "Shake me",
                    )
                } else {
                    Button.Danger(
                        onClick = onShakeClick,
                        text = "Shake me",
                    )
                }
            }

            val vibrateController = rememberShakeController()

            Button.Danger(
                onClick = {
                    vibrateController.shake(
                        ShakeConfig(
                            iterations = 8,
                            intensity = 100_000f,
                            translateX = -15f,
//                            translateY = 5f,
                        ),
                    )
                },
                text = "Vibrate me",
                modifier = Modifier
                    .shake(vibrateController),
            )

            val scaleController = rememberShakeController()

            Button.Tertiary(
                onClick = {
                    scaleController.shake(
                        ShakeConfig(
                            iterations = 1,
                            intensity = 1_00f,
                            scaleX = 1.5f,
                        ),
                    )
                },
                text = "Scale me",
                modifier = Modifier
                    .shake(scaleController),
            )

            val rotateController = rememberShakeController()

            Button.Tertiary(
                onClick = {
                    rotateController.shake(
                        ShakeConfig(
                            iterations = 2,
                            intensity = 1_000f,
                            rotate = 90f,
                        ),
                    )
                },
                text = "Rotate me",
                modifier = Modifier
                    .shake(rotateController),
            )
        }
    }
}
