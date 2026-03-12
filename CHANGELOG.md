# Changelog

<!-- Don't forget to update links at the end of this page! -->

## [Unreleased]

## [2.1.0]

_2026-03-12_

### Spark

#### 💄 Buttons and Tags have new shapes!

The **buttons** now have a full rounded shape and the **tags** use `SparkTheme.shapes.extraSmall`.
This chanmge can be toggled via the `SparkFeatureFlag.useNewButtonAndTagsShapes` feature flag.

#### 🆕 New Card specs

Card is now defined by clear variants discoverable on the `Card` object. Use `Card.Flat`, `Card.Elevated`, `Card.Outlined`, `Card.HighlightFlat`, or `Card.HighlightElevated` for the different styles.

#### 🆕 Component Generator Script

We now have a helper script to simplify the creation of a new components. To use it invoki it like this `./scripts/generate-component.main.kts [component-name] [package-name] [-v Variant1] [-v Variant2]`

### 📝 Documentation

We're testing newways to better represent the components in our [documentation](https://leboncoin.github.io/spark-android/) by including dedicated screenshots generated from screenshot tests to ensure that you always get the up to date visuals.
We're also tesing including these new screenshots in the kdoc direclty.

## [2.1.0-alpha01]

_2026-03-11_

### Spark

#### 💄 Buttons and Tags have new shapes!

The **buttons** now have a full rounded shape and the **tags** use `SparkTheme.shapes.extraSmall`.
This chanmge can be toggled via the `SparkFeatureFlag.useNewButtonAndTagsShapes` feature flag.

#### 🆕 New Card specs

Card is now defined by clear variants discoverable on the `Card` object. Use `Card.Flat`, `Card.Elevated`, `Card.Outlined`, `Card.HighlightFlat`, or `Card.HighlightElevated` for the different styles.

#### 🆕 Component Generator Script

We now have a helper script to simplify the creation of a new components. To use it invoki it like this `./scripts/generate-component.main.kts [component-name] [package-name] [-v Variant1] [-v Variant2]`

## [2.0.1]

_2026-03-12_

### Icons
- 🐛 Fix an issue with the generation of `LeboncoinIcons` when new icons are merged in spark-tokens.
- 💄 Increase the decimal precisions for paths from 2 to 3 to avoid some paths malformations
- 🆕 Add new stop light icon
- 💄 Fix Cardboard icon missing the safe padding and the malformation of state criteria

## [2.0.0]

_2026-03-05_

### Spark

#### 🆕 Snackbar (breaking changes)

> [!CAUTION]
> `SnackbarStyle` (Filled / Tinted) has been **removed**. Snackbars now use a single visual style with a colored border and elevation. `SnackbarIntent` has been reduced from 10 values to **4**: Success, Alert, Error, Info (Neutral, Main, Basic, Support, Accent, SurfaceInverse are removed).

- **New visuals:** Single style with 2dp border using the intent color. Each intent has a default icon (e.g. Success → CircleCheckFill, Alert → WarningFill).
- **`Snackbar` composable:** `style` and `actionOnNewLine` parameters removed. New optional `title` parameter (displayed above the message). `icon` is now an optional override of the intent's default icon. Dismiss is done via `onDismissClick`.
- **`showSnackbar`:** `style` and `actionOnNewLine` removed; optional `title` added. Default intent changed from `SnackbarIntent.Neutral` to `SnackbarIntent.Info`.

## [2.0.0-alpha01]

_2026-02-24_

### Spark

#### 🆕 Snackbar (breaking changes)

> [!CAUTION]
> `SnackbarStyle` (Filled / Tinted) has been **removed**. Snackbars now use a single visual style with a colored border and elevation. `SnackbarIntent` has been reduced from 10 values to **4**: Success, Alert, Error, Info (Neutral, Main, Basic, Support, Accent, SurfaceInverse are removed).

- **New visuals:** Single style with 2dp border using the intent color. Each intent has a default icon (e.g. Success → CircleCheckFill, Alert → WarningFill).
- **`Snackbar` composable:** `style` and `actionOnNewLine` parameters removed. New optional `title` parameter (displayed above the message). `icon` is now an optional override of the intent’s default icon. Dismiss is done via `onDismissClick`.
- **`showSnackbar`:** `style` and `actionOnNewLine` removed; optional `title` added. Default intent changed from `SnackbarIntent.Neutral` to `SnackbarIntent.Info`.

- 🐛 Fix the missing links from the documentation
- 📝 Add the support for samples inside the documentation



## [1.8.0]

_2026-02-20_

#### 🆕 LeboncoinIcons
Add `LeboncoinIcons` object as namespace for new static icons from Spark for leboncoin. This object provides access to all static vector icons as drawable resources, similar to `SparkIcons` but specifically for leboncoin-branded icons.

### Spark

- 📝 Add model in the exception message when an Image does not have a defined size
- 🐛 `ButtonContrast` and `ButtonGhost` were using `color` instead of `onContainerColor` values for their content colours
- 📝 Improve the message from the crash when using a Spark component outside a `SparkTheme`
- 🎨 Add new SurfaceDark color token to be used when we need a surface/background to remain dark in light and darkmode like the footer in the website or the drawer in dashboard pro
- Apply the MaterialComposableHasSparkReplacement in spark modules

## [1.8.0-alpha06]

_2026-02-20_

### Spark

#### 🆕 LeboncoinIcons
Add `LeboncoinIcons` object as namespace for new static icons from Spark for leboncoin. This object provides access to all static vector icons as drawable resources, similar to `SparkIcons` but specifically for leboncoin-branded icons.

## [1.8.0-alpha05]

_2026-02-18_

### Spark

- Since we have uncertainties regarding the usages of FileKit for the `FileUpload` we marked it as `@InternalSparkApi` until we decide how to handle the file selection

## [1.8.0-alpha04]

_2026-02-05_

### Spark

- 📝 Add model in the exception message when an Image does not have a defined size
- 🐛 `ButtonContrast` and `ButtonGhost` were using `color` instead of `onContainerColor` values for their content colours
- 📝 Improve the message from the crash when using a Spark component outside a `SparkTheme`

## [1.8.0-alpha03]

_2026-01-28_

### Spark

#### 🆕 FileUpload Component

- ✨ Add `FileUpload.Button` and `FileUpload.ButtonSingleSelect` composables for selecting files through a button trigger. Supports single or multiple file selection with images, videos, and generic files (with optional extension filtering). For images, you can choose between camera or gallery selection. Use `FileUploadList` or `PreviewFile` to display selected files with progress indicators, error states, and remove actions. The `FileUploadPattern` allows you to integrate file upload functionality into any custom component.

```kotlin
// Single file selection
var selectedFile by remember { mutableStateOf<UploadedFile?>(null) }

FileUpload.ButtonSingleSelect(
    onResult = { file -> selectedFile = file },
    label = "Select file",
    type = FileUploadType.Image(ImageSource.Gallery)
)
```

> [!NOTE]
> This component is experimental and marked with `@ExperimentalSparkApi`. Feedbacks are welcomed.

## [1.8.0-alpha02]

_2026-01-22_

### Spark
- Add new SurfaceDark color token to be used when we need a surface/background to remain dark in light and darkmode like the footer in the website or the drawer in dashboard pro

## [1.8.0-alpha01]
_2026-01-22_

### Spark
- Apply the MaterialComposableHasSparkReplacement in spark modules
- Add new SurfaceDark color token to be used when we need a surface/background to remain dark in light and darkmode like the footer in the website or the drawer in dashboard pro

### CI
- Run lint task on the `:spark-lint` module

## [1.7.1]

_2026-01-28_

### Spark

- 🐛 `Dropdown` Fix layout used in MultiChoice made the dropdown smaller than intended

## [1.7.0]

_2026-01-14_

### Spark

#### 🆕 SegmentedGauge Component

- ✨ Add `SegmentedGauge` and `SegmentedGaugeShort` composables for level indication
- Support for different sizes (Small, Medium) and custom colors

> [!NOTE]
> This component is experimental and marked with `@ExperimentalSparkApi`. Feedbacks are welcomed.

- 💄 Add intent surface for the filled tag as it was missing
- 📝 Fix wordings in the documentation
- ⚙️ Migrate lint rules that were present in the lbc app into spark

### Catalog

- ✨ Add color selector component for component configurators
- 🚀 Compress vignettes to reduce app weight and improve vignette loading speed

## [1.7.0-alpha01]

_2026-01-08_

### Spark

#### 🆕 SegmentedGauge Component

- ✨ Add `SegmentedGauge` and `SegmentedGaugeShort` composables for level indication
- Support for different sizes (Small, Medium) and custom colors

> [!NOTE]
> This component is experimental and marked with `@ExperimentalSparkApi`. Feedbacks are welcomed.

- 📝 Fix wordings in the documentation
- ⚙️ Migrate lint rules that were present in the lbc app into spark

### Catalog

- ✨ Add color selector component for component configurators
- 🚀 Compress vignettes to reduce app weight and improve vignette loading speed

## [1.6.2]

_2026-01-08_

### Spark

- 🔧 Migrated `bodyWidth()` modifier to use `bodyMaxWidth` based on window size class breakpoints, centering content horizontally with a max width of 840dp for expanded & large screens and 1040dp for extra large screens.

## [1.6.1]

_2026-01-08_

### Spark

- 🐛 Fixed the `Image` Composable to ensure it does not download images twice

#### ⬆️ Dependency Updates

- ⬆️ Bump `androidx.compose:compose-bom` from 2025.09.00 to 2025.12.01
- 🔧 Pin `androidx.compose.material3:material3` to version 1.3.2 (not using BOM version)

## [1.6.0]

_2025-11-12_

### Spark

- ✨ Add `TagHighlight` component for highlighting new features
- ✨ Add `highlight` extension on the `CornerShape` type

> [!NOTE]
> This component is experimental and marked with `@ExperimentalSparkApi`. It provides two variants:
`TagHighlight` for card integration and `TagHighlightBadge` for positioning near highlighted
> elements. Both include localized "New!" labels and should be used with an End-of-Life Remote Config
> to prevent indefinite production presence.

### Catalog

- 🎨 Update the Pro Theme colors to reflect the current one used in production
- ✨ Add new icon picker component to select any icon available in components configurator when available in the api
- ✨ Add Badge component examples
- ✨ Add AlertDialog component examples
- ✨ Add Placeholder/skeletons component examples
- ✨ Add new modal example demonstrating modal with no content to showcase Bottom App Bar behavior
- 💄 Update Backdrop component to use `background` color instead of `surface` for front layer
- 💄Add new vignettes illustrations to represent the components when browsing them in the app

## [1.6.0-alpha01]

_2025-10-30_

### Spark

- ✨ Add TagHighlight component for highlighting new features

> [!NOTE]
> This component is experimental and marked with `@ExperimentalSparkApi`. It provides two variants:
`TagHighlight` for card integration and `TagHighlightBadge` for positioning near highlighted
> elements. Both include localized "New!" labels and should be used with an End-of-Life Remote Config
> to prevent indefinite production presence.

### Catalog

- 💄Add new vignettes illustrations to represent the components when browsing them in the app

## [1.5.1]

_2025-11-26_

### Spark

- 🐛 Fixed the `Image` Composable to ensure it does not download images twice.

## [1.5.0]

_2025-10-29_

### Spark

- 🔧 Replace vector drawable animated icons by their vector painter alternative. They're still
  available through `SparkAnimatedIcons`
- ✨ Add more api to `ProgressTracker`, with a `readonly` mode & the possibility to change the icons
  for each steps.
- 🔧 Updated contrast level threshold in `LeboncoinTheme` from Material Medium to High as Users do
  not expect the drastic change in color

#### Rating

- ♿ `RatingInput` now behaves like a slider for accessibility, improving screen reader support and
  customisable state descriptions.
- 🔧 Added horizontal drag gesture to change the rating value, with haptic feedback for each change.
- ⌨️ Keyboard support: Shift + Arrow keys increment/decrement the rating for enhanced accessibility
  while still maintaining focus on each star for selection.
- 🧪 Added `testTag` parameter for UI testing and automation.

> [!CAUTION]
> If you use custom accessibility semantics or parent components, set `allowSemantics = false` to
> avoid duplicate announcements.

#### 🎨 Improvements

- 🎨 `Tab` now use a rounded shape for its top corners.
- 🎨 `TopAppBar` now supports a `colors` parameter for customisation, however note that tokens other
  than surface will not apply the elevation overlay.
- 🎨 `ProgressTracker` styles and animations enhanced to match new specs.
- 🎨 Added Sticky BottomAppBar examples and improved elevation behaviour.

#### 🐛 Bug Fixes

- 🐛 Fi xed `ModalScaffold` to allow proper scrollable popup behaviour.
- 💄`Scaffold` now correctly applies `containerColor` for its content's background.

#### ⬆️ Dependency Updates

- ⬆️ Bump `androidx.compose:compose-bom` from 2025.08.01 to 2025.09.00.
- ⬆️ Bump `kotlin` from 2.2.10 to 2.2.20.
- ⬆️ Bump `paparazzi` to 2.0.0-alpha02.
- ⬆️ Bump `io.coil-kt.coil3:coil-bom` from 3.2.0 to 3.3.0

## [1.5.0-beta02]

_2025-10-29_

### Spark

- 🐛 `SparkAnimatedIcons.searchIcon` was set to filled in its start state instead of outlined.

- ## [1.5.0-beta01]

_2025-10-28_

### Spark

- 🔧 Replace vector drawable animated icons by their vector painter alternative. They're still
  available through `SparkAnimatedIcons`
- 🔧 Revert the removal of the outline style for the `ProgressTracker`
- ✨ Add more api to `ProgressTracker`, with a `readonly` mode & the possibility to change the icons
  for each steps.

## [1.5.0-alpha03]

_2025-10-16_

### Spark

- 🔧 Updated contrast level threshold in `LeboncoinTheme` from Material Medium to High as Users do not expect the drastic change in color

## [1.5.0-alpha02]

_2025-10-03_

### Spark

#### Dependency Updates
- ⬇️ Revert "chore(deps): bump androidx.core:core-ktx from 1.16.0 to 1.17.0 (#1662)" because the version 1.17.0 require consumers to upgrade their compile sdk version to api 36 but this breaks the unit test for modules where a roboeletric & paparazzi test is present.

#### Scaffold

- `Scaffold` now correctly applies `containerColor` for its content's background.

## [1.5.0-alpha01]

_2025-09-18_

### Spark

#### Rating
- ♿ `RatingInput` now behaves like a slider for accessibility, improving screen reader support and customisable state descriptions.
- 🔧 Added horizontal drag gesture to change the rating value, with haptic feedback for each change.
- ⌨️ Keyboard support: Shift + Arrow keys increment/decrement the rating for enhanced accessibility while still maintaining focus on each star for selection.
- 🧪 Added `testTag` parameter for UI testing and automation.

> [!CAUTION]
> If you use custom accessibility semantics or parent components, set `allowSemantics = false` to avoid duplicate announcements.

#### 🎨 Improvements
- 🎨 `Tab` now use a rounded shape for its top corners.
- 🎨 `TopAppBar` now supports a `colors` parameter for customisation, however note that tokens other than surface will not apply the elevation overlay.
- 🎨 `ProgressTracker` styles and animations enhanced to match new specs.
- 🎨 Added Sticky BottomAppBar examples and improved elevation behaviour.

#### � Bug Fixes
- 🐛 Fixed `ModalScaffold` to allow proper scrollable popup behaviour.

#### ⬆️ Dependency Updates
- ⬆️ Bump `androidx.compose:compose-bom` from 2025.08.01 to 2025.09.00.
- ⬆️ Bump `kotlin` from 2.2.10 to 2.2.20.
- ⬆️ Bump `paparazzi` to 2.0.0-alpha02.
- ⬆️ Bump `io.coil-kt.coil3:coil-bom` from 3.2.0 to 3.3.0

## [1.4.2]

_2025-10-17_

### Spark

#### Scaffold

- `Scaffold` now correctly applies `containerColor` for its content's background.

## [1.4.1]

_2025-10-17_

### Spark

- 🔧 Updated contrast level threshold in `LeboncoinTheme` from Material Medium to High as Users do
  not expect the drastic change in color

## [1.4.0]

_2025-09-05_

### Spark

#### Animations

##### 🆕 Add new `pulse` modifier animation

This animation display a pulsating wave from the component to catch the attention of the user eyes.
THe color, scales, duration and shape are customizable.
```kotlin
ButtonTinted(
    modifier = Modifier
        .pulse(
            shape = SparkTheme.shapes.large,
        ),
    text = "Vibing",
)
```

##### 🆕 Add new `shake` modifier animation

This animation display a transformative animations using a spring animation spec.
It's ideal to indicate validation on user interaction.
```kotlin
 val vibrateController = rememberShakeController()

ButtonTinted(
    modifier = Modifier.shake(vibrateController),
    onClick = {
        vibrateController.shake(
            ShakeConfig(
                iterations = 8,
                intensity = 100_000f,
                translateX = -15f,
            ),
        )
    },
    text = "Vibrate me",
)
```

- 🆕 Add a `pulse` example and configurator
- Pulse now have a `enabled` argument to avoid making a complex modifier chain when we want to control the visibility of this animation

#### Popover

- 🔧 Add a `positionProvider` argument to change the spacing between a `Popover` and its anchor

#### Modal

- 🐛 Fixed ModalScaffold by removing `FLAG_LAYOUT_NO_LIMITS` window flag to allow proper scrollable popup behavior when it's used inside it

#### Checkbox, RadioButton & Switch: API changes

- The `intent` parameter for `Checkbox`, `RadioButton`, and `Switch` is now deprecated and will be removed in a future release. We are keeping only the "Basic" and "Error" colors to ensure better visual consistency.
- Use the new `error: Boolean` parameter to indicate error/validation state for `Checkbox` & `RadioButton` components.
- The `ContentSide` parameter will be deprecated in a future release to improve readability and accessibility (a11y). All content will be aligned to the right/end by default for better screen reader support.

> [!CAUTION]
> - If you use a color other than **"Basic"** or **"Error"**, consider replacing it and use the new `error` parameter.
> - If you use Start/left content side, update your usage to End/right alignment for improved accessibility.

## [1.4.0-beta01]

_2025-09-04_

### Spark

#### Animations

- 🆕 Add a `pulse` example and configurator
- Pulse now have a `enabled` argument to avoid making a complex modifier chain when we want to control the visibility of this animation

## [1.4.0-alpha03]

_2025-08-01_

### Spark

#### Animations

##### 🆕 Add new `pulse` modifier animation

This animation display a pulsating wave from the component to catch the attention of the user eyes.
THe color, scales, duration and shape are customizable.
```kotlin
ButtonTinted(
    modifier = Modifier
        .pulse(
            shape = SparkTheme.shapes.large,
        ),
    text = "Vibing",
)
```

##### 🆕 Add new `shake` modifier animation

This animation display a transformative animations using a spring animation spec.
It's ideal to indicate validation on user interaction.
```kotlin
 val vibrateController = rememberShakeController()

ButtonTinted(
    modifier = Modifier.shake(vibrateController),
    onClick = {
        vibrateController.shake(
            ShakeConfig(
                iterations = 8,
                intensity = 100_000f,
                translateX = -15f,
            ),
        )
    },
    text = "Vibrate me",
)
```

- 🔧 Add a `positionProvider` argument to change the spacing between a `Popover` and its anchor

## [1.4.0-alpha02]

_2025-07-29_

### Spark

#### Switch
- 🔧 Reverted the removal of `contentSide` parameter from `SwitchLabelled` components to maintain backward compatibility

#### Modal
- 🐛 Fixed ModalScaffold by removing `FLAG_LAYOUT_NO_LIMITS` window flag to allow proper scrollable popup behavior when it's used inside it

## [1.4.0-alpha01]

_2025-07-28_

### Spark

#### Checkbox, RadioButton & Switch: API changes

- The `intent` parameter for `Checkbox`, `RadioButton`, and `Switch` is now deprecated and will be removed in a future release. We are keeping only the "Basic" and "Error" colors to ensure better visual consistency.
- Use the new `error: Boolean` parameter to indicate error/validation state for `Checkbox` & `RadioButton` components.
- The `ContentSide` parameter will be deprecated in a future release to improve readability and accessibility (a11y). All content will be aligned to the right/end by default for better screen reader support.

> [!CAUTION]
> - If you use a color other than **"Basic"** or **"Error"**, consider replacing it and use the new `error` parameter.
> - If you use Start/left content side, update your usage to End/right alignment for improved accessibility.

## [1.3.0]

_2025-07-23_

### Spark

#### 🆕 High Color Contrast Support
> [!NOTE]
> This feature requires Android 14 (API level 34) or higher to access system contrast settings.

- ✨ High contrast color themes are now available for both light and dark modes

Call these new methods to get the basic colors in high contrast for light and dark mode:
```kotlin
if (useDarkColors) {
    darkHighContrastSparkColors()
} else {
    lightHighContrastSparkColors()
}
```

#### 🆕 ComboBox Component
> [!TIP]
> ComboBox uses the new TextFieldState API for improved state management.

- ✨ **Major API Upgrade**: ComboBox components now use `TextFieldState` instead of value/onValueChange pattern (#1572)
- ✨ **Enhanced Single Selection**: `SingleChoiceComboBox` with improved filtering and suggestion capabilities
- ✨ **Multi-Selection Support**: `MultiChoiceComboBox` with chip-based selection display and management

**Example: Real-time Filtering ComboBox**
```kotlin
val state = rememberTextFieldState()
var expanded by remember { mutableStateOf(false) }
var searchText by remember { mutableStateOf("") }

val filteredBooks by remember(searchText) {
    derivedStateOf {
        if (searchText.isBlank()) comboBoxSampleValues
        else comboBoxSampleValues.filter {
            it.title.contains(searchText, ignoreCase = true)
        }
    }
}

LaunchedEffect(Unit) {
    snapshotFlow { state.text }
        .debounce(300.milliseconds)
        .onEach { queryText -> searchText = queryText.toString() }
        .collect()
}

SingleChoiceComboBox(
    state = state,
    expanded = expanded,
    onExpandedChange = { expanded = it },
    onDismissRequest = { expanded = false },
    label = "Search books",
    placeholder = "Type to search...",
    helper = "Filter books by typing in the search box"
) {
    filteredBooks.forEach { book ->
        DropdownMenuItem(
            text = { Text(book.title) },
            onClick = {
                state.setTextAndPlaceCursorAtEnd(book.title)
                expanded = false
            },
            selected = book.title == state.text
        )
    }
}
```

**Example: Smart Suggestions ComboBox**
```kotlin
val state = rememberTextFieldState()
var searchText by remember { mutableStateOf("") }

val suggestedBooks by remember(searchText) {
    derivedStateOf {
        if (searchText.isBlank()) emptyList()
        else comboBoxSampleValues.filter {
            it.title.contains(searchText, ignoreCase = true)
        }.take(3) // Limit to top 3 suggestions
    }
}

var showDropdown by remember { mutableStateOf(false) }
val expanded by remember {
    derivedStateOf { showDropdown && suggestedBooks.isNotEmpty() }
}

SingleChoiceComboBox(
    state = state,
    expanded = expanded,
    onExpandedChange = { showDropdown = it },
    onDismissRequest = {
        searchText = ""
        showDropdown = false
    },
    label = "Search with suggestions",
    placeholder = "Type to see suggestions...",
    helper = "Get book suggestions as you type"
) {
    suggestedBooks.forEach { book ->
        DropdownMenuItem(
            text = { Text(book.title) },
            onClick = {
                state.setTextAndPlaceCursorAtEnd(book.title)
                searchText = ""
                showDropdown = false
            },
            selected = book.title == state.text
        )
    }
}
```

#### 🐛 Modal & Dialog Improvements
- 🐛 Modal components now pass correct insets as padding and don't take space when no footer is available (#1579)
- 🔧 Improved edge-to-edge support for modals with proper window flag handling
- 🔧 BottomAppBar now uses `heightIn` instead of fixed height for better responsive behavior

#### 🔧 Component Updates
- 🗑️ Remove deprecation on `IconToggleButton` (#1573)
- 🔧 Dropdown API enhanced to support both single and multi-selection patterns with improved `DropdownMenuItem` overloads thanks to the work done in the combobox
- 🐛 Fixed lint rule stubs used for testing (#1564)
- 🐛 Fixed popover hidden close button issue (#1570) by @amokhefi
- ♿ Removed duplicate content description for tab icons to improve accessibility (#1563)

### Catalog App

- 🗑️ Removed brand theming options to simplify the configuration (#1571)
- 🔧 Improved edge-to-edge modal examples with proper padding and inset handling
- 💬🇫🇷 The catalog app more text translated to french locale
- ✨ New stepper example for custom form usage (#1560)
- ✨ New support for high color contrast themes based on system accessibility settings (#1464)
- 🎨 The catalog app now includes a contrast level slider to test different contrast configurations

#### 🆕 Animation Enhancements
- ✨ New `AnimatedNullableVisibility` overloads that properly handle nullable content during exit animations (#1565)
- ✨ Added `pulse` animation modifier for creating pulsating visual effects
- 🎨 Enhanced animation support in preview environments with new composition locals
- ✨ Added shared transitions throughout the app to showcase how to use them (#1567)

### CI

- 🚀 Added custom job to simplify required status checks (#1586)
- 🚀 Enabled Gradle Configuration Cache on CI workflow for improved build performance (#1585)
- 🚀 Optimized CI workflow by splitting jobs for better parallelization (#1584)
- 🚀 Migrated publishing from OSSRH to Central Portal (#1581)

## [1.2.2]

_2025-04-28_

### Spark
* ✨ Introduce `SparkExceptionHandler` that allow users to control the behavior of some crashable unvalid events/states
* 🐛 Fix `Image` Icon Size leading to crash
* 🐛 `Image` will now signal that it's missing a defined size

## [1.2.1]

_2025-03-27_

### Spark
* 🐛 Ensure Modifiers are applied only once when using conditional Modifiers

## [1.2.0]

_2025-03-19_

### Spark
* ✨ New `Stepper` Component
* 🗑️ `includeFontPadding`  on Spark typographies is not removed since it's no longer needed since Compose 1.6

### Catalog

* 🔗 The catalog app now supports **deeplinks** to any pages! This allows us to redirect our user quickly to a component that has been introduced or changed.
* ✨ Now when a component is being worked on you will see a Work in Progress illustration.
* ✨ A new Catalog component has been created to simplify the uses & selection of enum for configuration.
* ✨ A component can now have more than 1 Configurator. This is to avoid configurators that are too complex and won't fit easily into one screen.
* 🚀 Material transitions can now be tested in the catalog app to showcase & test their behaviour.
* 🕶️ The screen reader navigation has been improved and we'll continue to improve it globally to meet the same standard as lbc

## [1.1.4]

_2025-02-19_

### Spark
- 🛠️ Modal `inEdgeToEdge` parameter was applied even when set to false.
- 🐛 Conditional modifiers were reapplying the chain instead of doing nothing when the predicate was false.

## [1.1.3]

_2025-01-29_

### Spark
- 🛠 Use latest and simpler workaround to display a Dialog in fullscreen with support to edge-to-edge.
- 🛠️ Modal `inEdgeToEdge` parameter now default to false.

## [1.1.2]

_2025-01-29_

### Spark
- 🐛 Conditional modifiers were not working as expected since they returned an empty modifier instead of modifier chain if the condition was not met.


## [1.1.1]

_2025-01-28_

### Spark
- 🐛 Conditional modifiers were not working as expected since they returned an empty.
- 🐛 `Image` no longer use a `BoxWithConstraint`as its root component which forbid intrinsic sizes
- 🐛 Revert `Image` behavior on sizing with empty/loading/error states.


## [1.1.0]

_2025-01-06_

### Animated Icons
> [!CAUTION]
> The api for `Icon`, `IconButton`, `IconToggleButton` , `Button`, `Tag` & `Chip` has their api modified to support animated vector icons

Animated Icons can now be used with spark components!

### Image
> [!IMPORTANT]
> Image component now has it's specs updated to the design ones! with new colors to its error state!

### TextField
> [!CAUTION]
> The position of the TextField status icon has been moved from the trailing addon to the support message which might impact your screens!

Now all textfields (not only the multi line one) can display a character count and their min width has been reduced to allow for side to side Textfield layout to be possible (previously it was too big to allow it)!

### Spark
- ✨ New icons available.
- 🥳 Showkase is no longer used in the catalog app which means it'll also no longer bleed in your code!
- 🎨 A new api allow you to convert a **Material Theme** to a **Spark theme** which could be used to support dynamic theming for ex.
- 🐛 `UserAvatar` badge position is now correctly positioned.
- ⬆️ The Compose BOM version has been upgraded from `2024.10.01` to `2024.12.01`.
- ⬆️  Bump compileSdk & targetSdk to API level 35 (Android 15).
- ⬆️  Bump kotlin from 2.0.21 to 2.1.0.
- ✨ A lint rule to detect wrong string annotation usages has been added by @EliottLujan!
- 🎨 Theme color has been updated.

### Catalog App

- 🥳 Showkase is no longer used to preview some of our caspule components.
- ✨ A `Modal` configurator as been added.
- 💄 IconScreen no longer has clipping with the search bar.
- 🛠️ Migrated from uri to the new Typesafe routes for navigation.
- ✨ New examples for the shape tokens has benn added by @EliottLujan!

## [1.0.2]

_2024-12-11_

### Spark

- ⬆️ Upgrade Compose BOM to `2024.11.00` since it only contains bugfixes changes.
-
- ## [1.0.1]

_2024-11-07_

### Spark

- 🐛 User Avatar presence badge was incorrectly placed, especially in big sizes.

## [1.0.0]

_2024-10-07_

### Spark
> [!CAUTION]
> All the code that was legacy, coming from brikke and was deprecated has been removed from Spark.
> This mean you build with break with this change

> [!CAUTION]
> Material 3 compose & Compose has been upgraded to version 1.3 and 1.7 which introduce compiling & visual breaking changes.
> Be sure to verify your UIs when upgrading.

- 🐛 Add statusBar size on the content padding on BottomSheet content.
- 🐛 One of TextLinkButton overloads was using intent Danger instead of Surface.
- 🐛 Dividers no longer have a minimum width/height of 40.dp.
- 💄 ModalScaffold now has a padding between its buttons when they overflow and stack each others.
- 🔧 Chips now have a leading/trailing icon slots to ensure these content are not hidden when the chip content is too big for the required width.
- 🔧 Components were missing `sparkUsageOverlay` so it has been added to them and remove on Icons to reduce the confusions with the huge amount of icons used in apps and our components.
- 🔧 `SparkTheme` now take a `SparkFeatureFlag` for the activation of the debug features.
- 📝 Documentation for `annotatedStringResource` has been improved with usage examples

### Catalog App

- ♿ A colorbliness filter has been added to improve testing of components for this disability.
- 💄 New examples for the elevation tokens have been added.

### CI
- Icons screenshot are not bound to the theme colors anymore to reduce invalidation not related to the icons themselves.

## 0.11.0

_2024-08-13_

### Spark

#### 🆕 Divider
> [!WARNING]
> The Divider Component has been deprecated to use the `HorizontalDivider`

The divider component now has 2 fixed colors, `outline` and `outlineHigh`. I now accept a slot has a label indicator if you need to place a decorative text when separating your sections.

#### 🆕 New Dropdown specs
> [!WARNING]
> The previous SelectTextField Api has been deprecated but should still be used in Combobox usecases.

The Dropdown replace the existing SelectTextfield in readonly mode. It adds new api to handle item groupings with a proper title and remove the necessity to provide the `onValueChange` callback.

#### 🆕 Snackbar
> [!CAUTION]
> `SnackbarColors` & every colored Snackbar override have been deprecated as error as their api is not compatible with the new one. You'll need to migrate them to use this version.

The new Snackbar loses its title and icon slot to accept only a `SparkIcon`.
It has 2 styles, intents and the new dismiss action that are also available on the `showSnackbar` function.


- ⬆️ Spark now use Kotlin 2.0
- ⬆️ Compose BOM has been increased from 2024.05.00 to 2024.06.00
- 🐛 The Avatar component was using the color icon without tinting it rendering them incompatible with the dark mode
- 🐛 Chip doesn't have a max width anymore.
- 🐛 Textfield doesn't have a max width anymore.
- 🐛 Progress Tracker Indicator size now follow the font scaling
- 🐛 Textfield now correctly show the required indicator when the label fold in multiples lines

### Catalog App

- 🎨 KA theme colors for outline has been changed in light mode and in dark mode it's the background + variants color
- 🆕 Examples on how to make a Button Toggle has been added

## [0.10.1]

_2024-06-18_

### Spark
- 🐛 `ModalScaffold` can now take no actions and will hide the Bottom app Bar.
- 🐛 `ModalScaffold` in dialog layout was not respecting the correct min and max width.

### Catalog App
- `ModalScaffold` Added an example that will show the modal with no actions.

## [0.10.0]

_2024-05-16_

### Spark
- 🆕 `BottomSheetScaffold` now has a new sheetPeekHeight parameter
- 💄 `Chip` Change chip icon default size to be bigger
- 💄 `TextField` Change TextField Icon color from onSurface to neutral
- 💄 `Icons` Update some icons
- 🐛 `ProgressTracker` Fix clipped ProgressTracker indicator

### Catalog App
- 🐛 `Checkbox` Fix intents in CheckboxConfigurator

## [0.9.0]

_2024-04-23_

### Spark

#### 🆕 Chips can now be selectable and closed
> [!CAUTION]
> The `Filled` style has been removed and may break your build if used. You need to see with your ui to know which styles to use instead of this one

> [!WARNING]
> The styles for chips have been deprecated you now need to use either the `Chip` or the `ChipSelectable` components for your need and provide the style in argument

If you want to make your Chip closable then you will need to add a callback action in the new `onClose` parameter.

---

#### 🆕 BottomSheet now use the spark specs
> [!CAUTION]
> This change will most likely break your build since most of the api has changed.
> We now use the M3 `BottomSheet` instead of a fork from a alpha version of it we did when it was only available in M2.

> [!WARNING]
> The `BottomSheet` currently only accept M3 snackbars, you won't be able to display a SparkSnackbar

---

- 🆕 ProgressTracker is now available! it still has a few minor visual bugs but it can be tested by squads on their scope don't hesitate to give us feedbacks!
- 🆕 `TextLinkButton` will now use `LocalContentColor` when using the Surface intent. This will allow you to have a `onSurface` `TextLink` when needed
- 🆕 `Popover` can now take an intent for its surface color
- 🆕 `Image` has its `emptyIcon` and `errorIcon` parameters open now for special cases
- 💬 A11y have been translated to german
- 💄 `Rating` will now have a lisible color when disabled
- 💄 Badge now use surface instead of onColor for its border color
- 🐛 Filled and Contrast `Button` now have a clear disabled state when their content color is dark
- 💄 New icons have been added

### Catalog App

- 🎨 Brand colors has been updated to their latest values
- 🔧 All Configurators are now scrollable

### CI

- 🔧 Decorrelated spotless and ktlint
- 🆕 Added Paparazzi as a manual workflow
- 🆕 Ran Lava Vulnerability Scanner on CI workflow
- 🔧 Moved code formatting tasks first in the contributing list

## [0.8.0]

_2024-02-28_

### Spark

* 🆕 Create the base custom layout for horizontal progress tracker
* 🆕 Add `Slider` component
* 🐛 Fix the `Button` end icon being squished when content is too long

### Catalog App

* 🎨 Update `TextLink` configurator colors
* 🎨 Add Configurator for `Progressbar`
* 🚀 Replace deprecated actionsdesk/lfs-warning with composite action

## [0.7.0]

_2024-02-15_

### Spark

* 🆕 Add slots api to `Buttons` and `Tags`
* 🆕 Add new `TextLink` Component
* 🆕 Add Large `UserAvatarStyle` Component
* 🐛 Add `Info` as `IconButton` intent color
* 🐛 Fix `ConstraintLayout` Constraint not being applied to `IconButtons` & `IconToggleButton`
* 🐛 Fix `annotatedStringResource` with args don't render annotation style
* 🐛 Remove unexpected Compose tooling dependency on runtime classpath
* 🐛 Attempt to reduce letter spacing on callout typo
* 🚀 Update modifiers impl to use Node api


### Catalog App

* 💄 Theme settings are now saved between sessions
* 🎨 Add Configurator for `Popover`
* 🎨 Add Configurator for `Progressbar`
* 📝 We can now specify a group to whom the app is distributed
* 📝 A message can be specified when publishing the app


## [0.6.1]

_2023-12-21_

### Fix Modal reported issues
*  Fix the content padding not being passed to it's children.
* Invert the position of buttons.
* Inset for non edge to edge app was broken and displaying the content bellow the system bars.
*  Add the support for WindowHeightSizeClass.Expanded to show the fullscreen modal in portrait

## [0.6.0]

_2023-12-19_

### Spark

* 🆕 Add `TextField addons` api with premade addons
* 🆕 Add `Popover` component
* 🆕 Add new `Modal` Component
* 🆕 Add `Progressbar` Component
* 🆕 Update rating specs
* 🆕 Add `Rating` Input
* 💄 Add `Button Shapes` and unify the api for all button types
* 🐛 `Checkbox` checkmark was using `onPrimary` instead of `onColor` from intent
* 🎨 Screenshot tests for all `SparkIcon`s


### Catalog App

* 💄 Add `Subito` theme to catalog app
* 💄 Add `Milanuncios` theme to catalog app
* 🎨 Add examples ad configurator for `Rating`
* 🎨 Add a configurator for the `Tab`
* 🎨 Add missing test, examples, configurator for `Tag` component
* 🐛 Fix typos in `TabsExamples` and replace one icon to fit the style
* 📝 Replace `zeroheight` links with `spark.adevinta.com`

## [0.5.0]

_2023-09-26_

### Spark

* 🆕 Add `IconButtons` with all intents, shapes and sizes
* 🆕 Add loading state to the `IconButton` component
* 🆕 Add `IconToggleButton`
* 💄 Use M2 elevation system instead of the M3 one
* ⬆️ Bump `compileSdk` and `targetSdk` to 34
* 🎨 Change colors from LBC & KA
* 🐛 Make the readonly `Textfields` not take the focus look when focused
* 📝 Replace oneliner with two distinct commands
* 🐛 `ModalFullScreenScaffold` top padding issue when no illustration


### Catalog App

* 🆕 Add Icons demo to CatalogApp
* 🆕 Add examples for tab component
* 💄 Add Kleinanzeigen theme
* 🎨 Add illustrations for components item
* 🚀 Fetch icon resources in coroutines
* 🐛 Shorten catalog app name
* 🐛 Add proguard rules to keep the names of spark icons resources
* 🐛 Component Illustrations are too big
* 🐛 Minor fixes in CatalogApp
* 💄 Modify Segmented color to be more visible and make switch take full width
* 💄 Update catalog app to show `ExtraLarge` icons

## [0.4.2]

_2023-08-24_

* 🐛 Some color tokens were not updated on theme change

## [0.4.1]

_2023-08-17_

### Spark
* 🆕 Implement `Spark Tab` and `Tab Group`
* 💄 `Checkbox`, `Switch`, `RadioButton` now has intents support
* 💄 Disabled components now have use `dim3`
* 💄 `Button Outline` border size is now **1dp** instead of **2dp**
* 💄 Update `TextField` background color
* 💄 `TextField` leading content padding is adjusted
* 💄 `TextField` addons content color is always `onSurface` even when unfocused
* 💄 `ButtonContrast` in Surface intent is now readable
* 💄 `ModalFullScreenScaffold` spaces and layout has been adjusted
* 🗑️ Small size for `Button` has been removed
* 🐛 Replace `Modifier.autofill` by `Autofill` Composable


### Catalog App
* 🆕 Now have examples and a configurator for `Button`, `Checkbox`, `Switch`, `RadioButton` `TextField`
* 💄 The launcher icon now has a light and dark variant depending on the theme mode
* 🐛 The app state is saved on configuration change
* 🐛 Fix the link to component documentation

## [0.3.1]

_2023-07-31_

* 🆕 Added `Basic` and `Accent` intents to all released components.
* 💄Updated the default color intents to `Basic` for `Tag`, `Chip`, `Spinner`.
* 🗑️ Deprecated `Primary` and `Secondary` intents, `Main` and `Support`should be used instead.

## [0.3.0]

_2023-07-11_

* 🆕 Migrate `TextField` to the new spark spec with multiple sizes, styles and intents.
* 🔧 Add extensions to make usage of dims and highlights simpler.
* 💄 `Buttons` now have the correct color in disabled state.
* 💄 `Badges` now have all intents instead of `error` and `info`.
* 💄 `Snackbars` now have a bigger shape in new ui.
* 🐛 `Tags` now correctly expose its children with semantics.
* 🗑️ Deprecated `SparkIcons` are now removed.
* Update icon resources

## [0.2.0]

_2023-06-23_

* 🆕 Migrate `Badges` to the new spark spec with multiple sizes, styles and intents.
* 🆕 Migrate `Icons` to the new spark spec with multiple sizes, styles and intents.
* 🆕 Migrate `Chips` to the new spark spec with multiple sizes, styles and intents.
* 🆕 Migrate `Toggles` to the new spark spec with multiple sizes, styles and intents.
* 🆕 Add fullscreen modal component as experimental
* Update icon resources

## [0.1.1]

_2023-05-23_

* Add missing Categories/Family icon #388

## [0.1.0]

_2023-04-18_

### What's new since 0.0.3

* 🆕 Migrate `Button` to the new spark spec with multiple sizes, styles and intents.
* 🆕 Migrate `Tags` to the new spark spec with multiple styles and intents.
* 🆕 Migrate `RadioButton` to the new spark spec with the ability to have content on both side now.
* 🆕 Migrate `Checkbox` to the new spark spec with the ability to have content on both side now.
* 🆕 Add the ability for `PreviewWrapper` to specify a different background color
  than `SparkTheme.colors.background` which is useful to test custom surfaces for example.
* 💄Fix `callout` Typo having a size of `14.sp` instead of `16.sp`

## [0.0.3]

_2023-04-05_

* Modify the `Image` fallback states background and icon colors #306
* Integrate the new typography tokens #298
* Add Legacy option to use previous DS style #310

## [0.0.2]

_2023-03-30_

## [0.0.1]

_2023-03-29_

<!-- Links -->

[Unreleased]: https://github.com/leboncoin/spark-android/compare/2.1.0...HEAD

[2.1.0]: https://github.com/leboncoin/spark-android/compare/2.0.1...2.1.0

[2.1.0-alpha01]: https://github.com/leboncoin/spark-android/compare/2.0.1...2.1.0-alpha01 https://github.com/leboncoin/spark-android/compare/2.0.1...2.1.0-alpha01

[2.0.1]: https://github.com/leboncoin/spark-android/compare/2.0.0...2.0.1

[2.0.0]: https://github.com/leboncoin/spark-android/compare/2.0.0-alpha01...2.0.0

[2.0.0-alpha01]: https://github.com/leboncoin/spark-android/releases/tag/2.0.0-alpha01

[1.8.0]: https://github.com/leboncoin/spark-android/releases/tag/1.8.0

[1.8.0-alpha06]: https://github.com/leboncoin/spark-android/releases/tag/1.8.0-alpha06

[1.8.0-alpha05]: https://github.com/leboncoin/spark-android/releases/tag/1.8.0-alpha05

[1.8.0-alpha04]: https://github.com/leboncoin/spark-android/releases/tag/1.8.0-alpha04

[1.8.0-alpha03]: https://github.com/leboncoin/spark-android/releases/tag/1.8.0-alpha03

[1.8.0-alpha02]: https://github.com/leboncoin/spark-android/releases/tag/1.8.0-alpha02

[1.8.0-alpha01]: https://github.com/leboncoin/spark-android/releases/tag/1.8.0-alpha01

[1.7.1]: https://github.com/leboncoin/spark-android/releases/tag/1.7.1

[1.7.0]: https://github.com/leboncoin/spark-android/releases/tag/1.7.0

[1.7.0-alpha01]: https://github.com/leboncoin/spark-android/releases/tag/1.7.0-alpha01

[1.6.2]: https://github.com/leboncoin/spark-android/releases/tag/1.6.2

[1.6.1]: https://github.com/leboncoin/spark-android/releases/tag/1.6.1

[1.6.0]: https://github.com/leboncoin/spark-android/releases/tag/1.6.0

[1.6.0-alpha01]: https://github.com/leboncoin/spark-android/releases/tag/1.6.0-alpha01

[1.5.1]: https://github.com/leboncoin/spark-android/releases/tag/1.5.1

[1.5.0]: https://github.com/leboncoin/spark-android/releases/tag/1.5.0

[1.5.0-beta02]: https://github.com/leboncoin/spark-android/releases/tag/1.5.0-beta02

[1.5.0-beta01]: https://github.com/leboncoin/spark-android/releases/tag/1.5.0-beta01

[1.5.0-alpha03]: https://github.com/leboncoin/spark-android/releases/tag/1.5.0-alpha03

[1.5.0-alpha02]: https://github.com/leboncoin/spark-android/releases/tag/1.5.0-alpha02

[1.5.0-alpha01]: https://github.com/leboncoin/spark-android/releases/tag/1.5.0-alpha01

[1.4.2]: https://github.com/leboncoin/spark-android/releases/tag/1.4.2

[1.4.1]: https://github.com/leboncoin/spark-android/releases/tag/1.4.1

[1.4.0]: https://github.com/leboncoin/spark-android/releases/tag/1.4.0

[1.4.0-beta01]: https://github.com/leboncoin/spark-android/releases/tag/1.4.0-beta01

[1.4.0-alpha03]: https://github.com/leboncoin/spark-android/releases/tag/1.4.0-alpha03

[1.4.0-alpha02]: https://github.com/leboncoin/spark-android/releases/tag/1.4.0-alpha02

[1.4.0-alpha01]: https://github.com/leboncoin/spark-android/releases/tag/1.4.0-alpha01

[1.3.0]: https://github.com/leboncoin/spark-android/releases/tag/1.3.0

[1.2.2]: https://github.com/leboncoin/spark-android/releases/tag/1.2.2

[1.2.1]: https://github.com/leboncoin/spark-android/releases/tag/1.2.1

[1.2.0]: https://github.com/leboncoin/spark-android/releases/tag/1.2.0

[1.1.4]: https://github.com/leboncoin/spark-android/releases/tag/1.1.4

[1.1.3]: https://github.com/leboncoin/spark-android/releases/tag/1.1.3

[1.1.2]: https://github.com/leboncoin/spark-android/releases/tag/1.1.2

[1.1.1]: https://github.com/leboncoin/spark-android/releases/tag/1.1.1

[1.1.0]: https://github.com/leboncoin/spark-android/releases/tag/1.1.0

[1.0.2]: https://github.com/leboncoin/spark-android/releases/tag/1.0.2

[1.0.1]: https://github.com/leboncoin/spark-android/releases/tag/1.0.1

[1.0.0]: https://github.com/leboncoin/spark-android/releases/tag/1.0.0

[0.11.0]: https://github.com/leboncoin/spark-android/releases/tag/0.11.0

[0.10.1]: https://github.com/leboncoin/spark-android/releases/tag/0.10.1

[0.10.0]: https://github.com/leboncoin/spark-android/releases/tag/0.10.0

[0.9.0]: https://github.com/leboncoin/spark-android/releases/tag/0.9.0

[0.8.0]: https://github.com/leboncoin/spark-android/releases/tag/0.8.0

[0.7.0]: https://github.com/leboncoin/spark-android/releases/tag/0.7.0

[0.6.1]: https://github.com/leboncoin/spark-android/releases/tag/0.6.1

[0.6.0]: https://github.com/leboncoin/spark-android/releases/tag/0.6.0

[0.5.0]: https://github.com/leboncoin/spark-android/releases/tag/0.5.0

[0.4.2]: https://github.com/leboncoin/spark-android/releases/tag/0.4.2

[0.4.1]: https://github.com/leboncoin/spark-android/releases/tag/0.4.1

[0.3.1]: https://github.com/leboncoin/spark-android/releases/tag/0.3.1

[0.3.0]: https://github.com/leboncoin/spark-android/releases/tag/0.3.0

[0.2.0]: https://github.com/leboncoin/spark-android/releases/tag/0.2.0

[0.1.1]: https://github.com/leboncoin/spark-android/releases/tag/0.1.1

[0.1.0]: https://github.com/leboncoin/spark-android/releases/tag/0.1.0

[0.0.3]: https://github.com/leboncoin/spark-android/releases/tag/0.0.3

[0.0.2]: https://github.com/leboncoin/spark-android/releases/tag/0.0.2

[0.0.1]: https://github.com/leboncoin/spark-android/releases/tag/0.0.1
