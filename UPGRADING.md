# Upgrading Spark Android

Migration guides for breaking changes, newest first. For a full list of changes see [CHANGELOG.md](CHANGELOG.md).

---

## Upgrading to 3.0 (unreleased)

### `Basic` intent removed across all components

> **Status: in progress** - `Basic` entries currently compile with `DeprecationLevel.ERROR`. A follow-up release will delete them entirely.

`Basic` was identical in value to `Support` and has been removed from the rebranding. The IDE provides an automatic quick-fix via `ReplaceWith`.

Affected intent enums: `ButtonIntent`, `BadgeIntent`, `ChipIntent`, `TagIntent`, `ToggleIntent`, and any other intent enum in the `spark` module.

**Before:**

```kotlin
ButtonFilled(intent = ButtonIntent.Basic)
Chip(intent = ChipIntent.Basic)
Tag(intent = TagIntent.Basic)
```

**After:**

```kotlin
ButtonFilled(intent = ButtonIntent.Support)
Chip(intent = ChipIntent.Support)
Tag(intent = TagIntent.Support)
```

Apply all instances at once: in Android Studio, place the cursor on any red `Basic` reference and choose **Replace with 'Support'**, or run **Code > Inspect Code** to batch-apply all `ReplaceWith` suggestions.

---

## Upgrading to 2.0

### Snackbar API redesigned

The Snackbar visual style was consolidated: `SnackbarStyle` (`Filled`/`Tinted`) is removed, the `actionOnNewLine` parameter is removed, and `SnackbarIntent` is reduced from 10 values to 4 (`Success`, `Alert`, `Error`, `Info`). Removed intents: `Neutral`, `Main`, `Basic`, `Support`, `Accent`, `SurfaceInverse`.

The default intent changed from `SnackbarIntent.Neutral` to `SnackbarIntent.Info`.

#### Composable call-site

**Before:**

```kotlin
Snackbar(
    style = SnackbarStyle.Tinted,
    intent = SnackbarIntent.Success,
    actionLabel = "Retry",
    actionOnNewLine = true,
) {
    Text("Upload complete")
}
```

**After:**

```kotlin
Snackbar(
    intent = SnackbarIntent.Success,
    actionLabel = "Retry",
    onActionClick = { /* handle */ },
) {
    Text("Upload complete")
}
```

The `style` and `actionOnNewLine` parameters are removed. The action is now always placed below the message when present. Pass `onDismissClick` to show a dismiss icon.

#### Optional title

A new `title` parameter displays a bold heading above the message body.

```kotlin
Snackbar(
    intent = SnackbarIntent.Error,
    title = "Upload failed",
    onDismissClick = { /* handle */ },
) {
    Text("Check your connection and try again.")
}
```

#### `showSnackbar` / `SnackbarHostState`

**Before:**

```kotlin
snackbarHostState.showSnackbar(
    message = "Saved",
    style = SnackbarStyle.Filled,
    actionOnNewLine = false,
)
```

**After:**

```kotlin
snackbarHostState.showSnackbar(
    message = "Saved",
    intent = SnackbarIntent.Info,
)
```

For full control, pass a `SnackbarSparkVisuals` directly:

```kotlin
snackbarHostState.showSnackbar(
    SnackbarSparkVisuals(
        message = "Saved",
        intent = SnackbarIntent.Success,
        title = "Done",
        withDismissAction = true,
    )
)
```

#### Removed intent values

Replace removed `SnackbarIntent` values as follows:

| Removed | Use instead |
|---|---|
| `Neutral` | `Info` |
| `Main` | `Info` |
| `Basic` | `Info` |
| `Support` | `Info` |
| `Accent` | `Info` |
| `SurfaceInverse` | `Info` |

---

## Upgrading to 1.4

### `Checkbox` and `RadioButton` `intent` parameter deprecated

The `intent: ToggleIntent` parameter on `Checkbox` and `RadioButton` is deprecated. Only `Support` (default) and error styling are kept to improve visual consistency. Use the new `error: Boolean` parameter for validation state.

**Before:**

```kotlin
Checkbox(
    checked = checked,
    onClick = { checked = !checked },
    intent = ToggleIntent.Danger,
)
```

**After:**

```kotlin
Checkbox(
    checked = checked,
    onClick = { checked = !checked },
    error = true,
)
```

If you were using any intent other than `Basic`/`Support` or `Danger`/`Error`, replace it with `error = false` (the default) and align your design with the Spark team.

### `ToggleIntent` enum deprecated

`ToggleIntent` itself is deprecated alongside the `intent` parameter. The `ToggleIntent.Basic` entry additionally carries `DeprecationLevel.ERROR` - replace it with `ToggleIntent.Support` while the enum is still accessible, then migrate away from `ToggleIntent` entirely.

---

## Upgrading to 1.0

### Legacy and Brikke-era APIs removed

All code marked deprecated in the pre-1.0 releases (originating from the Brikke design system) was deleted. This is a hard compile break.

Common removals:

- Deprecated `SparkIcons` aliases removed - use the current `SparkIcons.*` names.
- `Button` small size removed - use `ButtonSize.Medium` or `ButtonSize.Large`.
- `Divider` deprecated in 0.11 - use `HorizontalDivider`.
- `Primary` and `Secondary` intents removed (deprecated since 0.3.1) - use `Main` and `Support`.
- `SnackbarColors` and per-color Snackbar overrides removed (deprecated as errors in 0.11) - migrate to the `SnackbarStyle` + `SnackbarIntent` API introduced in 0.11 before upgrading to 1.0, then proceed to the 2.0 migration above.

### Compose BOM upgraded to Material3 1.3 / Compose 1.7

Material3 1.3 introduces its own compile-level and visual breaking changes. Verify your UI after upgrading, particularly any screens using `Scaffold`, dialogs, or bottom sheets.

---

## Upgrading to 0.11

### Snackbar API introduced (first migration step toward 2.0)

`SnackbarColors` and all per-color Snackbar overrides were deprecated at `DeprecationLevel.ERROR` in 0.11. Migrate to `SnackbarStyle` and `SnackbarIntent` here before upgrading to 2.0, where `SnackbarStyle` itself is removed.

### `Divider` deprecated

`Divider` is deprecated in favour of `HorizontalDivider`. The new component accepts `outline` and `outlineHigh` color tokens and an optional label slot.

```kotlin
// Before
Divider()

// After
HorizontalDivider()
```

---

## Upgrading to 0.9

### `BottomSheet` API replaced

The Spark fork of the alpha Material2 `BottomSheet` was replaced by the Material3 `BottomSheet`. Most of the API has changed. Consult the [M3 BottomSheet documentation](https://m3.material.io/components/bottom-sheets) alongside the Spark KDoc.

> **Note:** `BottomSheet` in 0.9 accepts only M3 snackbars. Spark `Snackbar` inside a `BottomSheet` is not supported until this restriction is lifted in a later release.

### Chip `Filled` style removed

`ChipStyle.Filled` was removed. Use `Chip` or `ChipSelectable` and pass the appropriate style argument.

---

## `TextFieldState` / `FormFieldStatus` naming (all versions)

> **Status: in progress** - the rename is not yet complete.

Compose introduced its own `TextFieldState` class, which collides with `com.adevinta.spark.components.textfields.TextFieldState`. A `typealias FormFieldStatus = TextFieldState` was added to provide a migration path. The `@Deprecated` annotation on `TextFieldState` itself is currently commented out pending completion of the rename across the codebase.

**Recommended action now:** use `FormFieldStatus` in new code to avoid the name collision with the Compose `TextFieldState`.

```kotlin
// Prefer this in new code
var status: FormFieldStatus? = FormFieldStatus.Error

TextField(
    value = text,
    onValueChange = { text = it },
    state = status,
)
```

Using `TextFieldState` directly still compiles, but risks ambiguity with `androidx.compose.foundation.text.input.TextFieldState` if both are imported.
