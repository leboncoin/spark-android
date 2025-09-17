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
package com.adevinta.spark.components.dialog

import androidx.annotation.DrawableRes
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.calculateEndPadding
import androidx.compose.foundation.layout.calculateStartPadding
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.sizeIn
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.BottomAppBarDefaults
import androidx.compose.material3.BottomAppBarScrollBehavior
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ScaffoldDefaults
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalView
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.paneTitle
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.tooling.preview.PreviewScreenSizes
import androidx.compose.ui.unit.LayoutDirection
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import androidx.compose.ui.window.DialogWindowProvider
import androidx.window.core.layout.WindowSizeClass.Companion.HEIGHT_DP_MEDIUM_LOWER_BOUND
import androidx.window.core.layout.WindowSizeClass.Companion.WIDTH_DP_MEDIUM_LOWER_BOUND
import com.adevinta.spark.ExperimentalSparkApi
import com.adevinta.spark.LocalSparkFeatureFlag
import com.adevinta.spark.PreviewTheme
import com.adevinta.spark.R
import com.adevinta.spark.SparkTheme
import com.adevinta.spark.components.appbar.BottomAppBar
import com.adevinta.spark.components.appbar.BottomAppBarSparkDefaults
import com.adevinta.spark.components.appbar.TopAppBar
import com.adevinta.spark.components.buttons.ButtonFilled
import com.adevinta.spark.components.buttons.ButtonOutlined
import com.adevinta.spark.components.dialog.ModalDefault.DialogPadding
import com.adevinta.spark.components.icons.Icon
import com.adevinta.spark.components.icons.IconButton
import com.adevinta.spark.components.image.Illustration
import com.adevinta.spark.components.scaffold.Scaffold
import com.adevinta.spark.components.surface.Surface
import com.adevinta.spark.components.text.Text
import com.adevinta.spark.icons.Close
import com.adevinta.spark.icons.ImageFill
import com.adevinta.spark.icons.MoreMenuVertical
import com.adevinta.spark.icons.SparkIcons
import com.adevinta.spark.tokens.Layout
import com.adevinta.spark.tokens.LocalWindowSizeClass

/**
 * A composable function that creates a full-screen modal scaffold, adapting its layout based on the device's screen
 * size and orientation.
 *
 * The scaffold provides different layouts for phone portrait, phone landscape, and other
 * devices (e.g., tablets or foldables) where it'll show a modal instead.
 *
 * IF you don't want the Bottom App Bar to appear then provide null to both [mainButton] & [supportButton]
 *
 * @param onClose callback that will be invoked when the modal is dismissed
 * @param snackbarHost Component to host Snackbars that are pushed to be shown
 * @param modifier applied to the root Scaffold
 * @param mainButton the main actions for this modal (should be a [ButtonFilled] most of the time)
 * @param supportButton the support or alternative actions for this modal (should any other button than [ButtonFilled])
 * portrait mode
 * @param title the title of the modal
 * @param actions the actions displayed at the end of the top app bar. This should typically be
 *  * [IconButton]s. The default layout here is a [Row], so icons inside will be placed horizontally.
 * @param inEdgeToEdge tel the component that the activity where it's being displayed on is a edege to edge screen.
 * This has to be explicitly specified as no api wan reliably tel us
 * @param content the center custom Composable for modal content
 */
@ExperimentalSparkApi
@Composable
public fun ModalScaffold(
    onClose: () -> Unit,
    modifier: Modifier = Modifier,
    contentPadding: PaddingValues = DialogPadding,
    contentWindowInsets: WindowInsets = ScaffoldDefaults.contentWindowInsets,
    snackbarHost: @Composable () -> Unit = {},
    mainButton: (@Composable (Modifier) -> Unit)? = null,
    supportButton: (@Composable (Modifier) -> Unit)? = null,
    title: @Composable () -> Unit = {},
    actions: @Composable RowScope.() -> Unit = {},
    inEdgeToEdge: Boolean = LocalSparkFeatureFlag.current.isContainingActivityEdgeToEdge,
    content: @Composable (PaddingValues) -> Unit,
) {
    val size = LocalWindowSizeClass.current
    val isPhoneLandscape =
        !size.isHeightAtLeastBreakpoint(HEIGHT_DP_MEDIUM_LOWER_BOUND)
    val isFoldableOrTablet = size.isAtLeastBreakpoint(WIDTH_DP_MEDIUM_LOWER_BOUND, HEIGHT_DP_MEDIUM_LOWER_BOUND)

    val properties = DialogProperties(
        usePlatformDefaultWidth = false,
        decorFitsSystemWindows = !inEdgeToEdge,
    )

    val dialogPaneDescription = stringResource(R.string.spark_dialog_pane_a11y)
    val dialogModifier = modifier.then(Modifier.semantics { paneTitle = dialogPaneDescription })

    when {
        isFoldableOrTablet -> DialogScaffold(
            modifier = dialogModifier,
            onClose = onClose,
            contentPadding = contentPadding,
            snackbarHost = snackbarHost,
            mainButton = mainButton,
            supportButton = supportButton,
            title = title,
            actions = actions,
            content = content,
        )

        isPhoneLandscape -> PhoneLandscapeModalScaffold(
            modifier = dialogModifier,
            properties = properties,
            contentPadding = contentPadding,
            onClose = onClose,
            snackbarHost = snackbarHost,
            mainButton = mainButton,
            supportButton = supportButton,
            title = title,
            actions = actions,
            contentWindowInsets = contentWindowInsets,
            inEdgeToEdge = inEdgeToEdge,
            content = content,
        )

        else ->
            PhonePortraitModalScaffold(
                modifier = dialogModifier,
                properties = properties,
                contentPadding = contentPadding,
                onClose = onClose,
                snackbarHost = snackbarHost,
                mainButton = mainButton,
                supportButton = supportButton,
                title = title,
                actions = actions,
                contentWindowInsets = contentWindowInsets,
                inEdgeToEdge = inEdgeToEdge,
                content = content,
            )
    }
}

@OptIn(ExperimentalMaterial3Api::class, ExperimentalLayoutApi::class)
@Composable
private fun DialogScaffold(
    onClose: () -> Unit,
    contentPadding: PaddingValues,
    snackbarHost: @Composable () -> Unit,
    modifier: Modifier = Modifier,
    mainButton: (@Composable (Modifier) -> Unit)? = null,
    supportButton: (@Composable (Modifier) -> Unit)? = null,
    title: @Composable () -> Unit = {},
    actions: @Composable RowScope.() -> Unit = {},
    content: @Composable (PaddingValues) -> Unit,
) {
    Dialog(
        onDismissRequest = onClose,
    ) {
        val scrollBehavior = TopAppBarDefaults.pinnedScrollBehavior()
        val bottomAppBarScrollBehavior = BottomAppBarSparkDefaults.bottomAppBarScrollBehavior()

        Surface(
            modifier = modifier
                .sizeIn(minWidth = 280.dp, maxWidth = 560.dp)
                .nestedScroll(scrollBehavior.nestedScrollConnection)
                .nestedScroll(bottomAppBarScrollBehavior.nestedScrollConnection),
            elevation = 6.dp,
            shape = SparkTheme.shapes.large,
        ) {
            Column {
                TopAppBar(
                    navigationIcon = {
                        // As a fallback when to action button is displayed to easily close the dialog
                        if (supportButton == null && mainButton == null) {
                            CloseIconButton(onClose = onClose)
                        }
                    },
                    title = title,
                    actions = actions,
                    scrollBehavior = scrollBehavior,
                )
                snackbarHost()
                Box(modifier = Modifier.weight(1f, fill = false)) {
                    val padding = contentPadding +
                        if (supportButton == null && mainButton == null) {
                            PaddingValues(bottom = 16.dp)
                        } else {
                            PaddingValues(0.dp)
                        }
                    content(padding)
                }
                if (supportButton == null && mainButton == null) return@Column
                BottomAppBar(
                    scrollBehavior = bottomAppBarScrollBehavior,
                ) {
                    FlowRow(
                        modifier = Modifier
                            .fillMaxWidth()
                            .heightIn(min = 76.dp)
                            .padding(vertical = 16.dp, horizontal = 24.dp),
                        horizontalArrangement = Arrangement.spacedBy(16.dp, Alignment.End),
                        verticalArrangement = Arrangement.spacedBy(8.dp),
                    ) {
                        supportButton?.invoke(Modifier)
                        mainButton?.invoke(Modifier)
                    }
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun PhonePortraitModalScaffold(
    onClose: () -> Unit,
    properties: DialogProperties,
    contentPadding: PaddingValues,
    snackbarHost: @Composable () -> Unit,
    modifier: Modifier = Modifier,
    title: @Composable () -> Unit = {},
    actions: @Composable RowScope.() -> Unit = {},
    mainButton: (@Composable (Modifier) -> Unit)? = null,
    supportButton: (@Composable (Modifier) -> Unit)? = null,
    contentWindowInsets: WindowInsets = ScaffoldDefaults.contentWindowInsets,
    inEdgeToEdge: Boolean = false,
    content: @Composable (PaddingValues) -> Unit,
) {
    Dialog(
        onDismissRequest = onClose,
        properties = properties,
    ) {
        // Work around for b/246909281 as for now Dialog doesn't pass the drawing insets to its content
        if (inEdgeToEdge) SetUpEdgeToEdgeDialog()

        val scrollBehavior = TopAppBarDefaults.pinnedScrollBehavior()
        val bottomAppBarScrollBehavior = BottomAppBarSparkDefaults.bottomAppBarScrollBehavior()

        Scaffold(
            modifier = modifier
                .fillMaxSize()
                .nestedScroll(scrollBehavior.nestedScrollConnection)
                .nestedScroll(bottomAppBarScrollBehavior.nestedScrollConnection),
            snackbarHost = snackbarHost,
            contentWindowInsets = contentWindowInsets,
            topBar = {
                TopAppBar(
                    navigationIcon = {
                        CloseIconButton(onClose = onClose)
                    },
                    title = title,
                    actions = actions,
                    scrollBehavior = scrollBehavior,
                )
            },
            bottomBar = { BottomBarPortrait(mainButton, supportButton, bottomAppBarScrollBehavior) },
        ) { innerPadding ->
            Box(Modifier.padding(contentPadding)) {
                content(innerPadding)
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun BottomBarPortrait(
    mainButton: @Composable ((Modifier) -> Unit)?,
    supportButton: @Composable ((Modifier) -> Unit)?,
    scrollBehavior: BottomAppBarScrollBehavior,
) {
    if (supportButton == null && mainButton == null) return
    BottomAppBar(
        scrollBehavior = scrollBehavior,
    ) {
        val buttonsLayoutModifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 24.dp)
            .padding(vertical = 16.dp)
        if (!Layout.windowSize.isWidthAtLeastBreakpoint(WIDTH_DP_MEDIUM_LOWER_BOUND)) {
            Column(
                modifier = buttonsLayoutModifier,
                verticalArrangement = Arrangement.spacedBy(16.dp, Alignment.Bottom),
            ) {
                val buttonModifier = Modifier.fillMaxWidth()
                mainButton?.invoke(buttonModifier)
                supportButton?.invoke(buttonModifier)
            }
        } else {
            Row(
                modifier = buttonsLayoutModifier,
                horizontalArrangement = Arrangement.spacedBy(16.dp, Alignment.End),
            ) {
                supportButton?.invoke(Modifier)
                mainButton?.invoke(Modifier)
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun PhoneLandscapeModalScaffold(
    onClose: () -> Unit,
    properties: DialogProperties,
    contentPadding: PaddingValues,
    snackbarHost: @Composable () -> Unit,
    modifier: Modifier = Modifier,
    @DrawableRes illustration: Int? = null,
    mainButton: (@Composable (Modifier) -> Unit)? = null,
    supportButton: (@Composable (Modifier) -> Unit)? = null,
    title: @Composable () -> Unit = {},
    actions: @Composable RowScope.() -> Unit = {},
    contentWindowInsets: WindowInsets = ScaffoldDefaults.contentWindowInsets,
    illustrationContentScale: ContentScale = ContentScale.Fit,
    inEdgeToEdge: Boolean = false,
    content: @Composable (PaddingValues) -> Unit,
) {
    Dialog(
        onDismissRequest = onClose,
        properties = properties,
    ) {
        // Work around for b/246909281 as for now Dialog doesn't pass the drawing insets to its content
        if (inEdgeToEdge) SetUpEdgeToEdgeDialog()
        val scrollBehavior = TopAppBarDefaults.pinnedScrollBehavior()
        val bottomAppBarScrollBehavior = BottomAppBarSparkDefaults.bottomAppBarScrollBehavior()

        Scaffold(
            modifier = modifier
                .fillMaxSize()
                .nestedScroll(scrollBehavior.nestedScrollConnection)
                .nestedScroll(bottomAppBarScrollBehavior.nestedScrollConnection),
            snackbarHost = snackbarHost,
            contentWindowInsets = contentWindowInsets,
            bottomBar = {
                if (supportButton == null && mainButton == null) return@Scaffold
                BottomAppBar(
                    scrollBehavior = bottomAppBarScrollBehavior,
                ) {
                    Row(
                        modifier = Modifier
                            .padding(bottom = 16.dp, top = 8.dp)
                            .padding(horizontal = 24.dp)
                            .windowInsetsPadding(BottomAppBarDefaults.windowInsets)
                            .fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(16.dp, Alignment.End),
                    ) {
                        val buttonModifier = Modifier.weight(1f)
                        supportButton?.invoke(buttonModifier)
                        mainButton?.invoke(buttonModifier)
                    }
                }
            },
            topBar = {
                TopAppBar(
                    navigationIcon = {
                        CloseIconButton(onClose = onClose)
                    },
                    title = title,
                    actions = actions,
                    scrollBehavior = scrollBehavior,
                )
            },
        ) { innerPadding ->
            Row(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(contentPadding),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(Layout.gutter),
            ) {
                illustration?.let {
                    Box(
                        modifier = Modifier.weight(.8f),
                        contentAlignment = Alignment.Center,
                    ) {
                        Illustration(
                            drawableRes = it,
                            contentDescription = null,
                            modifier = Modifier.fillMaxWidth(.8f),
                            contentScale = illustrationContentScale,
                        )
                    }
                }
                Box(
                    modifier = Modifier
                        .weight(1f)
                        .fillMaxHeight(),
                ) {
                    content(innerPadding)
                }
            }
        }
    }
}

@Composable
private fun CloseIconButton(onClose: () -> Unit) {
    IconButton(onClick = onClose) {
        Icon(
            sparkIcon = SparkIcons.Close,
            modifier = Modifier.size(24.dp),
            contentDescription = stringResource(id = R.string.spark_a11y_modal_fullscreen_close),
        )
    }
}

@Suppress("DEPRECATION")
@Composable
private fun SetUpEdgeToEdgeDialog() {
    val window = (LocalView.current.parent as DialogWindowProvider).window
    window.statusBarColor = Color.Transparent.toArgb()
    window.navigationBarColor = Color.Transparent.toArgb()
    window.navigationBarColor = Color.Transparent.toArgb()
    // Displaying a popup with this flag will make it un scrollable if it's bigger that the screen
    // window.addFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS)
    window.setDimAmount(0f)
}

/**
 * Merge 2 padding values with each others.
 * This was not provided by Google so we're making our own
 */
private operator fun PaddingValues.plus(other: PaddingValues): PaddingValues = PaddingValues(
    start = this.calculateStartPadding(LayoutDirection.Ltr) +
        other.calculateStartPadding(LayoutDirection.Ltr),
    top = this.calculateTopPadding() + other.calculateTopPadding(),
    end = this.calculateEndPadding(LayoutDirection.Ltr) +
        other.calculateEndPadding(LayoutDirection.Ltr),
    bottom = this.calculateBottomPadding() + other.calculateBottomPadding(),
)

public object ModalDefault {
    public val DialogPadding: PaddingValues = PaddingValues(horizontal = 24.dp)
}

// @DevicePreviews
@PreviewScreenSizes
@Composable
private fun ModalPreview() {
    PreviewTheme(
        padding = PaddingValues(),
    ) {
        Box(
            Modifier.fillMaxSize(),
        ) {
            ModalScaffold(
                onClose = { /*TODO*/ },
                mainButton = {
                    ButtonFilled(modifier = it, onClick = { /*TODO*/ }, text = "Main Action")
                },
                supportButton = {
                    ButtonOutlined(modifier = it, onClick = { /*TODO*/ }, text = "Alternative Action")
                },
                title = {
                    Text(text = "Title")
                },
                actions = {
                    Icon(sparkIcon = SparkIcons.ImageFill, contentDescription = "")
                    Icon(sparkIcon = SparkIcons.ImageFill, contentDescription = "")
                    Icon(sparkIcon = SparkIcons.MoreMenuVertical, contentDescription = "")
                },
            ) { innerPadding ->
                Text(
                    modifier = Modifier
                        .padding(innerPadding)
                        .verticalScroll(rememberScrollState()),
                    text =
                    "Lorem ipsum dolor sit amet, consectetuer adipiscing elit. Aenean commodo ligula eget dolor. " +
                        "Aenean massa. Cum sociis natoque penatibus et magnis dis parturient montes, nascetur " +
                        "ridiculus mus. Donec quam felis, ultricies nec, pellentesque eu, pretium quis, sem. " +
                        "\n\nNulla consequat massa quis enim. Donec pede justo, fringilla vel, aliquet nec, " +
                        "vulputate eget, arcu. In enim justo, rhoncus ut, imperdiet a, venenatis vitae, justo. " +
                        "Nullam dictum felis eu pede mollis pretium. Integer tincidunt. Cras dapibus. " +
                        "Vivamus elementum semper nisi. Aenean vulputate eleifend tellus. " +
                        "\n\nAenean leo ligula, porttitor eu, consequat vitae, eleifend ac, enim. Aliquam lorem ante," +
                        " dapibus in, viverra quis, feugiat a, tellus. Phasellus viverra nulla ut metus varius " +
                        "laoreet. Quisque rutrum. Aenean imperdiet. Etiam ultricies nisi vel augue. " +
                        "Curabitur ullamcorper ultricies nisi. Nam eget dui. Etiam rhoncus. " +
                        "\n\nMaecenas tempus, tellus eget condimentum rhoncus, sem quam semper libero, sit amet " +
                        "adipiscing sem neque sed ipsum. Nam quam nunc, blandit vel, luctus pulvinar, hendrerit id, " +
                        "lorem. Maecenas nec odio et ante tincidunt tempus. Donec vitae sapien ut libero venenatis " +
                        "faucibus. Nullam quis ante. Etiam sit amet orci eget eros faucibus tincidunt. " +
                        "Duis leo. Sed fringilla mauris sit amet nibh. Donec sodales sagittis magna. " +
                        "Sed consequat, leo eget bibendum sodales, augue velit cursus nunc,",
                )
            }
        }
    }
}
