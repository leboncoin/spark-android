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
package com.adevinta.spark.catalog.examples.samples.dialog

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement.spacedBy
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.WindowInsetsSides
import androidx.compose.foundation.layout.displayCutout
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.only
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.systemBars
import androidx.compose.foundation.layout.windowInsetsEndWidth
import androidx.compose.foundation.layout.windowInsetsStartWidth
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.BlendMode
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Paint
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.PathMeasure
import androidx.compose.ui.graphics.PointMode
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.VertexMode
import androidx.compose.ui.graphics.Vertices
import androidx.compose.ui.graphics.drawscope.drawIntoCanvas
import androidx.compose.ui.graphics.drawscope.scale
import androidx.compose.ui.graphics.lerp
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.adevinta.spark.SparkTheme
import com.adevinta.spark.catalog.examples.samples.dialog.modal.ModalSample
import com.adevinta.spark.catalog.model.Example
import com.adevinta.spark.catalog.util.SampleSourceUrl
import com.adevinta.spark.components.buttons.ButtonFilled
import com.adevinta.spark.components.dialog.ModalScaffold
import com.adevinta.spark.components.icons.Icon
import com.adevinta.spark.components.icons.IconButton
import com.adevinta.spark.components.spacer.VerticalSpacer
import com.adevinta.spark.components.surface.Surface
import com.adevinta.spark.components.text.Text
import com.adevinta.spark.icons.ImageFill
import com.adevinta.spark.icons.MoreMenuVertical
import com.adevinta.spark.icons.SparkIcons

private const val ModalsExampleSourceUrl = "$SampleSourceUrl/ModalExamples.kt"
public val DialogsExamples: List<Example> = listOf(
    Example(
        id = "modal",
        name = "Modal",
        description = "Showcase the modal component with different",
        sourceUrl = ModalsExampleSourceUrl,
    ) {
        ModalSample()
    },
    Example(
        id = "no-padding",
        name = "Modal with no content padding",
        description = "Showcase the modal component with no start and end padding on the content placeholder",
        sourceUrl = ModalsExampleSourceUrl,
    ) {
        ModalSample(PaddingValues(0.dp))
    },
    Example(
        id = "no-buttons",
        name = "Modal with no buttons",
        description = "Showcase the modal component with no main and support button. \n This will hide the Bottom " +
            "App Bar and add a close button in the dialog layout",
        sourceUrl = ModalsExampleSourceUrl,
    ) {
        ModalSample(withButtons = false)
    },

    Example(
        id = "edge to edge test",
        name = "Edge to edge",
        description = "Showcase the modal component with no main and support button. \n This will hide the Bottom " +
            "App Bar and add a close button in the dialog layout",
        sourceUrl = ModalsExampleSourceUrl,
    ) {
        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center,
        ) {
            var showDialog by rememberSaveable { mutableStateOf(false) }

            ButtonFilled(
                onClick = { showDialog = true },
                text = "Show Modal",
            )

            if (showDialog) {
                val displayCutout = WindowInsets.displayCutout.only(
                    WindowInsetsSides.Horizontal,
                )
                ModalScaffold(
                    inEdgeToEdge = true,
                    onClose = { showDialog = false },
                    contentPadding = PaddingValues(0.dp),
                    title = {
                        Text(
                            modifier = Modifier
                                .fillMaxWidth()
                                .background(SparkTheme.colors.error.copy(alpha = 0.5f)),
                            textAlign = TextAlign.Center,
                            text = "ModalScaffold",
                            style = SparkTheme.typography.display3,
                        )
                    },
                    actions = {
                        IconButton(onClick = { /*TODO*/ }) {
                            Icon(sparkIcon = SparkIcons.ImageFill, contentDescription = "")
                        }
                        IconButton(onClick = { /*TODO*/ }) {
                            Icon(sparkIcon = SparkIcons.ImageFill, contentDescription = "")
                        }
                        IconButton(onClick = { /*TODO*/ }) {
                            Icon(sparkIcon = SparkIcons.MoreMenuVertical, contentDescription = "")
                        }
                    },
                ) { innerPadding ->
                    Box(
                        Modifier
                            .fillMaxSize()
                            .meshGradient(
                                points = listOf(
                                    listOf(
                                        Offset(0f, 0f) to SparkTheme.colors.alert,
                                        Offset(.5f, 0f) to SparkTheme.colors.info,
                                        Offset(1f, 0f) to SparkTheme.colors.main,
                                    ),
                                    listOf(
                                        Offset(0f, .5f) to SparkTheme.colors.alert,
                                        Offset(.5f, .5f) to SparkTheme.colors.main,
                                        Offset(1f, .5f) to SparkTheme.colors.info,
                                    ),
                                    listOf(
                                        Offset(0f, 1f) to SparkTheme.colors.info,
                                        Offset(.5f, 1f) to SparkTheme.colors.main,
                                        Offset(1f, 1f) to SparkTheme.colors.alert,
                                    ),
                                ),
                            ),
                    ) {
                        Column(
                            modifier = Modifier
                                .verticalScroll(rememberScrollState())
                                .padding(innerPadding)
//                                .windowInsetsPadding(displayCutout)
                                .background(SparkTheme.colors.accent.copy(alpha = 0.8f)),
                        ) {
                            Text(
                                text = "content",
                                textAlign = TextAlign.Center,
                                style = SparkTheme.typography.display3,
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .height(2000.dp),
                            )
                            LazyRow(
                                contentPadding = PaddingValues(horizontal = 16.dp),
                                horizontalArrangement = spacedBy(8.dp),
                                verticalAlignment = Alignment.CenterVertically,
                            ) {
                                item {
                                    Spacer(modifier = Modifier.windowInsetsStartWidth(displayCutout))
                                }
                                repeat(25) {
                                    item {
                                        Surface(
                                            modifier = Modifier
                                                .size(150.dp)
                                                .meshGradient(
                                                    points = listOf(
                                                        listOf(
                                                            Offset(0f, 0f) to SparkTheme.colors.success,
                                                            Offset(.5f, 0f) to SparkTheme.colors.info,
                                                            Offset(1f, 0f) to SparkTheme.colors.error,
                                                        ),
                                                        listOf(
                                                            Offset(0f, .5f) to SparkTheme.colors.error,
                                                            Offset(.5f, .5f) to SparkTheme.colors.success,
                                                            Offset(1f, .5f) to SparkTheme.colors.info,
                                                        ),
                                                        listOf(
                                                            Offset(0f, 1f) to SparkTheme.colors.info,
                                                            Offset(.5f, 1f) to SparkTheme.colors.error,
                                                            Offset(1f, 1f) to SparkTheme.colors.success,
                                                        ),
                                                    ),
                                                ),
                                        ) {
                                        }
                                    }
                                }
                                item {
                                    Spacer(modifier = Modifier.windowInsetsEndWidth(WindowInsets.systemBars))
                                }
                            }
                            VerticalSpacer(100.dp)
                        }
                    }
                }
            }
        }
    },
)

@Composable
public fun Modifier.meshGradient(
    points: List<List<Pair<Offset, Color>>>,
    resolutionX: Int = 1,
    resolutionY: Int = 1,
    showPoints: Boolean = false,
    indicesModifier: (List<Int>) -> List<Int> = { it },
): Modifier {
    val pointData by remember(points, resolutionX, resolutionY) {
        derivedStateOf {
            PointData(points, resolutionX, resolutionY)
        }
    }

    return drawBehind {
        drawIntoCanvas { canvas ->
            scale(
                scaleX = size.width,
                scaleY = size.height,
                pivot = Offset.Zero,
            ) {
                canvas.drawVertices(
                    vertices = Vertices(
                        vertexMode = VertexMode.Triangles,
                        positions = pointData.offsets,
                        textureCoordinates = pointData.offsets,
                        colors = pointData.colors,
                        indices = indicesModifier(pointData.indices),
                    ),
                    blendMode = BlendMode.Dst,
                    paint = paint,
                )
            }

            if (showPoints) {
                val flattenedPaint = Paint()
                flattenedPaint.color = Color.White.copy(alpha = .9f)
                flattenedPaint.strokeWidth = 4f * .001f
                flattenedPaint.strokeCap = StrokeCap.Round
                flattenedPaint.blendMode = BlendMode.SrcOver

                scale(
                    scaleX = size.width,
                    scaleY = size.height,
                    pivot = Offset.Zero,
                ) {
                    canvas.drawPoints(
                        pointMode = PointMode.Points,
                        points = pointData.offsets,
                        paint = flattenedPaint,
                    )
                }
            }
        }
    }
}

public class PointData(
    private val points: List<List<Pair<Offset, Color>>>,
    private val stepsX: Int,
    private val stepsY: Int,
) {
    public val offsets: MutableList<Offset>
    public val colors: MutableList<Color>
    public val indices: List<Int>
    private val xLength: Int = (points[0].size * stepsX) - (stepsX - 1)
    private val yLength: Int = (points.size * stepsY) - (stepsY - 1)
    private val measure = PathMeasure()

    private val indicesBlocks: List<IndicesBlock>

    init {
        offsets = buildList {
            repeat((xLength - 0) * (yLength - 0)) {
                add(Offset(0f, 0f))
            }
        }.toMutableList()

        colors = buildList {
            repeat((xLength - 0) * (yLength - 0)) {
                add(Color.Transparent)
            }
        }.toMutableList()

        indicesBlocks =
            buildList {
                for (y in 0..yLength - 2) {
                    for (x in 0..xLength - 2) {

                        val a = (y * xLength) + x
                        val b = a + 1
                        val c = ((y + 1) * xLength) + x
                        val d = c + 1

                        add(
                            IndicesBlock(
                                indices = buildList {
                                    add(a)
                                    add(c)
                                    add(d)

                                    add(a)
                                    add(b)
                                    add(d)
                                },
                                x = x,
                                y = y,
                            ),
                        )
                    }
                }
            }

        indices = indicesBlocks.flatMap { it.indices }
        generateInterpolatedOffsets()
    }

    private fun generateInterpolatedOffsets() {
        for (y in 0..points.lastIndex) {
            for (x in 0..points[y].lastIndex) {
                this[x * stepsX, y * stepsY] = points[y][x].first
                this[x * stepsX, y * stepsY] = points[y][x].second

                if (x != points[y].lastIndex) {
                    val path = cubicPathX(
                        point1 = points[y][x].first,
                        point2 = points[y][x + 1].first,
                        when (x) {
                            0 -> 0
                            points[y].lastIndex - 1 -> 2
                            else -> 1
                        },
                    )
                    measure.setPath(path, false)

                    for (i in 1..<stepsX) {
                        measure.getPosition(i / stepsX.toFloat() * measure.length).let {
                            this[(x * stepsX) + i, (y * stepsY)] = Offset(it.x, it.y)
                            this[(x * stepsX) + i, (y * stepsY)] =
                                lerp(
                                    points[y][x].second,
                                    points[y][x + 1].second,
                                    i / stepsX.toFloat(),
                                )
                        }
                    }
                }
            }
        }

        for (y in 0..<points.lastIndex) {
            for (x in 0..<this.xLength) {
                val path = cubicPathY(
                    point1 = this[x, y * stepsY].let { Offset(it.x, it.y) },
                    point2 = this[x, (y + 1) * stepsY].let { Offset(it.x, it.y) },
                    when (y) {
                        0 -> 0
                        points[y].lastIndex - 1 -> 2
                        else -> 1
                    },
                )
                measure.setPath(path, false)
                for (i in (1..<stepsY)) {
                    val point3 = measure.getPosition(i / stepsY.toFloat() * measure.length).let {
                        Offset(it.x, it.y)
                    }

                    this[x, ((y * stepsY) + i)] = point3

                    this[x, ((y * stepsY) + i)] = lerp(
                        this.getColor(x, y * stepsY),
                        this.getColor(x, (y + 1) * stepsY),
                        i / stepsY.toFloat(),
                    )
                }
            }
        }
    }

    public data class IndicesBlock(val indices: List<Int>, val x: Int, val y: Int)

    public operator fun get(x: Int, y: Int): Offset {
        val index = (y * xLength) + x
        return offsets[index]
    }

    private fun getColor(x: Int, y: Int): Color {
        val index = (y * xLength) + x
        return colors[index]
    }

    private operator fun set(x: Int, y: Int, offset: Offset) {
        val index = (y * xLength) + x
        offsets[index] = Offset(offset.x, offset.y)
    }

    private operator fun set(x: Int, y: Int, color: Color) {
        val index = (y * xLength) + x
        colors[index] = color
    }
}

private fun cubicPathX(point1: Offset, point2: Offset, position: Int): Path {
    val path = Path().apply {
        moveTo(point1.x, point1.y)
        val delta = (point2.x - point1.x) * .5f
        when (position) {
            0 -> cubicTo(
                point1.x,
                point1.y,
                point2.x - delta,
                point2.y,
                point2.x,
                point2.y,
            )

            2 -> cubicTo(
                point1.x + delta,
                point1.y,
                point2.x,
                point2.y,
                point2.x,
                point2.y,
            )

            else -> cubicTo(
                point1.x + delta,
                point1.y,
                point2.x - delta,
                point2.y,
                point2.x,
                point2.y,
            )
        }

        lineTo(point2.x, point2.y)
    }
    return path
}

private fun cubicPathY(point1: Offset, point2: Offset, position: Int): Path {
    val path = Path().apply {
        moveTo(point1.x, point1.y)
        val delta = (point2.y - point1.y) * .5f
        when (position) {
            0 -> cubicTo(
                point1.x,
                point1.y,
                point2.x,
                point2.y - delta,
                point2.x,
                point2.y,
            )

            2 -> cubicTo(
                point1.x,
                point1.y + delta,
                point2.x,
                point2.y,
                point2.x,
                point2.y,
            )

            else -> cubicTo(
                point1.x,
                point1.y + delta,
                point2.x,
                point2.y - delta,
                point2.x,
                point2.y,
            )
        }

        lineTo(point2.x, point2.y)
    }
    return path
}

private val paint = Paint()
