# Upgrading Spark Android

Migration guides for breaking changes, newest first. For a full list of changes see [CHANGELOG.md](CHANGELOG.md).

## Upgrading to 5.0

Version 5.0 has one breaking change. The Android font resources of `spark-core` are removed.

### Nunito Sans font resources move from `R.font` to `Res.font`

Spark now builds one Nunito Sans font family in `commonMain`, and Android and desktop both use it. The Android font resources `R.font.nunito_sans_*` are removed from `spark-core`. Use `Res.font.nunito_sans_*` from `com.adevinta.spark.core` instead.

You are affected when your code or your XML references a `nunito_sans_*` font that comes from `spark-core`. If you only use `SparkTheme` and `SparkTheme.typography`, you do not change any code.

#### Compose code

`Res.font.nunito_sans_*` is a `FontResource`, not an Android resource ID. Load it with the Compose Multiplatform `Font` function. That function is `@Composable`.

**Before:**

```kotlin
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import com.adevinta.spark.core.R

val family = FontFamily(
    Font(resId = R.font.nunito_sans_regular, weight = FontWeight.Normal),
    Font(resId = R.font.nunito_sans_bold, weight = FontWeight.Bold),
)
```

**After:**

```kotlin
import androidx.compose.runtime.Composable
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import com.adevinta.spark.core.Res
import com.adevinta.spark.core.nunito_sans_bold
import com.adevinta.spark.core.nunito_sans_regular
import org.jetbrains.compose.resources.Font

@Composable
fun rememberFamily(): FontFamily = FontFamily(
    Font(resource = Res.font.nunito_sans_regular, weight = FontWeight.Normal),
    Font(resource = Res.font.nunito_sans_bold, weight = FontWeight.Bold),
)
```

#### XML and View code

`Res.font` has no XML form. A `@font/nunito_sans_*` reference in a layout or a style no longer builds. Copy the font file that you need into the `res/font` folder of your own module, and keep the same file name.

#### Resource names

| Before (Android `R.font`)       | After (`Res.font`)                 |
|---------------------------------|------------------------------------|
| `nunito_sans_regular`           | `nunito_sans_regular`              |
| `nunito_sans_italic`            | `nunito_sans_italic`               |
| `nunito_sans_semi_bold`         | `nunito_sans_semi_bold`            |
| `nunito_sans_semi_bold_italic`  | `nunito_sans_semi_bold_italic`     |
| `nunito_sans_bold`              | `nunito_sans_bold`                 |
| `nunito_sans_bold_italic`       | `nunito_sans_bold_italic`          |
| `nunito_sans_variable`          | `nunito_sans_variable`             |
| none                            | `nunito_sans_italic_variable` (new)|

#### Text looks different after the upgrade

The call sites do not change, but the rendered text does.

- On Android API 26 and later, SemiBold text (weight 600) now renders as SemiBold. It rendered as Bold before.
- On the same devices, italic text uses the real italic font. Compose faked the italic before.
- On desktop, text now uses the variable fonts at weights 400, 600 and 700. It used the static fonts before.
- Android API 24 and 25, and Xiaomi, Redmi and Poco devices, keep the static fonts. Variable fonts do not render correctly on them. Text on these devices does not change.

Three screenshot references in the Spark repository changed with this release. When you have screenshot tests, expect differences in screens that show text, and record new reference images.

---

## Upgrading to 4.2

### `UserAvatar` deprecated in favour of `Avatar.User` and `Avatar.Company`

`UserAvatar` is deprecated. Use `Avatar.User` for a person or `Avatar.Company` for a company. A later major release removes `UserAvatar`.

#### Pro accounts become `Avatar.Company`

`Avatar.User` and `Avatar.Company` have no `isPro` parameter. Choose `Avatar.User` for a person or `Avatar.Company` for a company instead. The deprecated `UserAvatar` keeps `isPro` until a later major release removes it.

`Avatar.Company` is a rounded square, not a circle. It uses `SparkTheme.shapes.extraSmall` at `XS`, `SM`, and `MD`, and `SparkTheme.shapes.small` at `LG` and above. Its placeholder is the `LeboncoinIcons.BuildingSquare` icon. A loaded logo is clipped to the same rounded square. `Avatar.Company` has no `color` parameter. The placeholder uses the neutral colours.

**Before:**

```kotlin
import com.adevinta.spark.components.image.UserAvatar
import com.adevinta.spark.components.image.UserAvatarStyle

UserAvatar(
    model = account.logoUrl,
    style = UserAvatarStyle.MEDIUM,
    isPro = true,
)
```

**After:**

```kotlin
import com.adevinta.spark.components.image.Avatar
import com.adevinta.spark.components.image.AvatarSize
import com.adevinta.spark.components.image.Company

Avatar.Company(
    model = account.logoUrl,
    size = AvatarSize.MD,
)
```

#### `Avatar.User` requires `username` and has no icon placeholder

`Avatar.User` has no `letter` parameter. It requires a `username` parameter instead, and derives
the letter placeholder from its first non-whitespace character, in upper case. `Avatar.User` has
no icon placeholder. A blank `username` shows an empty neutral circle instead. `Avatar.User` has no
`color` parameter. The placeholder uses the neutral colours.

The deprecated `UserAvatar` gains a `letter` parameter. A `letter` wins over `isPro` and renders as given, with no upper-casing. A later major release removes `UserAvatar`.

**Before:**

```kotlin
import com.adevinta.spark.components.image.UserAvatar
import com.adevinta.spark.components.image.UserAvatarStyle

UserAvatar(
    model = user.avatarUrl,
    style = UserAvatarStyle.MEDIUM,
)
```

**After:**

```kotlin
import com.adevinta.spark.components.image.Avatar
import com.adevinta.spark.components.image.AvatarSize
import com.adevinta.spark.components.image.User

Avatar.User(
    username = user.name, // "Spark" shows "S"
    model = user.avatarUrl,
    size = AvatarSize.MD,
)
```

#### Online indicator moves to the `addon` slot

`Avatar.User` and `Avatar.Company` have no `isOnline` parameter. Pass `addon = { onlineIndicator() }` instead. The deprecated `UserAvatar` keeps `isOnline` and has no `addon` parameter, until a later major release removes it.

`Avatar.User` centres the addon on the circle rim. `Avatar.Company` puts the addon at the bottom-end corner, with 25% of the addon outside the avatar (at the bottom-start corner in a right-to-left layout). At `AvatarSize.XS`, `onlineIndicator()` draws nothing.

**Before:**

```kotlin
import com.adevinta.spark.components.image.UserAvatar
import com.adevinta.spark.components.image.UserAvatarStyle

UserAvatar(
    model = user.avatarUrl,
    style = UserAvatarStyle.MEDIUM,
    isOnline = user.isOnline,
)
```

**After:**

```kotlin
import com.adevinta.spark.components.image.Avatar
import com.adevinta.spark.components.image.AvatarSize
import com.adevinta.spark.components.image.User

Avatar.User(
    username = user.name,
    model = user.avatarUrl,
    size = AvatarSize.MD,
    addon = { if (user.isOnline) onlineIndicator() },
)
```

#### `UserAvatarStyle` replaced by `AvatarSize`

`UserAvatarStyle` is removed. `AvatarSize` has seven values instead of three. `Avatar.User` and `Avatar.Company` take an `AvatarSize` in a `size` parameter. The deprecated `UserAvatar` takes an `AvatarSize` in its `style` parameter, so replace the removed values there too.

| Removed | Use instead |
|---|---|
| `UserAvatarStyle.SMALL` | `AvatarSize.SM` |
| `UserAvatarStyle.MEDIUM` | `AvatarSize.MD` |
| `UserAvatarStyle.LARGE` | `AvatarSize.XL` |

**Before:**

```kotlin
import com.adevinta.spark.components.image.UserAvatar
import com.adevinta.spark.components.image.UserAvatarStyle

UserAvatar(
    model = user.avatarUrl,
    style = UserAvatarStyle.LARGE,
)
```

**After:**

```kotlin
import com.adevinta.spark.components.image.Avatar
import com.adevinta.spark.components.image.AvatarSize
import com.adevinta.spark.components.image.User

Avatar.User(
    username = user.name,
    model = user.avatarUrl,
    size = AvatarSize.XL,
)
```

#### Visual changes on the deprecated `UserAvatar`

`UserAvatar` looks different after the upgrade, even when the call does not change.

- The fallback icon (the silhouette or the building) fills the avatar circle.
- The online dot sits on the circle rim at 45 degrees. The `SMALL` dot (8dp) and the `LARGE` dot (16dp) keep their size. The `MEDIUM` dot changes from 12dp with a 2dp ring to 8dp with a 1dp ring (`AvatarSize.MD`).

---

## Upgrading to 3.0

### `contentSide` parameter removed from toggles

The `contentSide` parameter has been removed from `CheckboxLabelled`, `RadioButtonLabelled`, and `SwitchLabelled` to enforce a consistent label placement across the design system.

### Deprecated `ToggleIntent` APIs removed

The deprecated `ToggleIntent` enum and the `intent` parameter have been removed. Use the `error` parameter to represent validation states.

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

### Snackbar API introduced

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
