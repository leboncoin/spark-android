# Theming and Tokens

Spark exposes its design tokens through `SparkTheme.colors`, `SparkTheme.shapes`, and
`SparkTheme.typography`. Read any token inside a composable; pass custom values to `SparkTheme` to
override them.

---

## Colour intents

Each intent is a semantic role, not a raw colour. Components accept an intent enum whose value
resolves to a `SparkColors` property at runtime, so swapping palettes requires no component changes.

| Intent | Property family | Use |
|---|---|---|
| Main | `main` / `onMain` / `mainContainer` / `onMainContainer` / `mainVariant` / `onMainVariant` | Primary brand colour, most-used surfaces |
| Support | `support` / `onSupport` / `supportContainer` / `onSupportContainer` / `supportVariant` / `onSupportVariant` | Secondary accent, FABs, selection controls, links |
| Accent | `accent` / `onAccent` / `accentContainer` / `onAccentContainer` / `accentVariant` / `onAccentVariant` | Tertiary highlight |
| Success | `success` / `onSuccess` / `successContainer` / `onSuccessContainer` | Positive feedback |
| Alert | `alert` / `onAlert` / `alertContainer` / `onAlertContainer` | Warning feedback |
| Error | `error` / `onError` / `errorContainer` / `onErrorContainer` | Error states |
| Info | `info` / `onInfo` / `infoContainer` / `onInfoContainer` | Informational feedback |
| Neutral | `neutral` / `onNeutral` / `neutralContainer` / `onNeutralContainer` | Neutral / muted emphasis |
| Surface | `surface` / `onSurface` / `surfaceInverse` / `onSurfaceInverse` / `surfaceDark` / `onSurfaceDark` | Card and sheet backgrounds |
| Background | `background` / `onBackground` / `backgroundVariant` / `onBackgroundVariant` | Screen backgrounds |

Every foreground colour is prefixed with `on` and pairs with its background counterpart. The
`contentColorFor` function returns the correct foreground given any background colour:

```kotlin
val fg = SparkTheme.colors.contentColorFor(SparkTheme.colors.mainContainer)
```

### Reading tokens

```kotlin
@Composable
fun BrandBadge() {
    Box(
        modifier = Modifier
            .background(SparkTheme.colors.main)
            .padding(8.dp)
    ) {
        Text(
            text = "New",
            color = SparkTheme.colors.onMain,
            style = SparkTheme.typography.caption,
        )
    }
}
```

### Dim levels

`SparkColors` exposes five alpha values for de-emphasis:

| Property | Alpha | Typical use |
|---|---|---|
| `dim1` | 0.72 | Medium-emphasis text |
| `dim2` | 0.56 | Medium-emphasis icons |
| `dim3` | 0.40 | Disabled components |
| `dim4` | 0.16 | Low-visibility elements |
| `dim5` | 0.08 | Pressed / ripple (avoid on Android) |

Use the extension properties on `Color` to apply them:

```kotlin
// dim3 composited over the surface colour - safe for disabled text
val disabledColour = SparkTheme.colors.onSurface.disabled

// or apply any dim directly
val mutedIcon = SparkTheme.colors.onBackground.dim2
```

---

## Providing a custom palette

Call `lightSparkColors()` or `darkSparkColors()` with only the tokens you want to override. All
other parameters default to the Spark baseline palette.

```kotlin
val brandLight = lightSparkColors(
    main = Color(0xFF1A6B3C),
    onMain = Color.White,
    mainContainer = Color(0xFFB7E4C7),
    onMainContainer = Color(0xFF0A3D22),
    mainVariant = Color(0xFF155230),
    onMainVariant = Color.White,
)

val brandDark = darkSparkColors(
    main = Color(0xFF74C69D),
    onMain = Color(0xFF0A3D22),
    mainContainer = Color(0xFF155230),
    onMainContainer = Color(0xFFB7E4C7),
    mainVariant = Color(0xFF95D5B2),
    onMainVariant = Color(0xFF0A3D22),
)

@Composable
fun App() {
    SparkTheme(
        colors = if (isSystemInDarkTheme()) brandDark else brandLight,
    ) {
        // content
    }
}
```

For high-contrast accessibility variants, use `lightHighContrastSparkColors()` and
`darkHighContrastSparkColors()` as starting points instead.

### Building from a Material 3 colour scheme

If you already generate a `ColorScheme` with Material's dynamic colour or Compose theme builder,
convert it directly:

```kotlin
val sparkColors = colorScheme.asSparkColors(useDark = isSystemInDarkTheme())

SparkTheme(colors = sparkColors) { /* … */ }
```

---

## Shape tokens

`SparkShapes` provides seven steps from flat to fully circular.

| Token | Corner radius | Default component uses |
|---|---|---|
| `none` | 0 dp | App bars, banners, navigation rails |
| `extraSmall` | 4 dp | Text fields, snackbars, menus |
| `small` | 8 dp | Chips |
| `medium` | 12 dp | Cards, small FABs |
| `large` | 16 dp | Extended FABs, navigation drawers |
| `extraLarge` | 28 dp | Bottom sheets, dialogs, large FABs |
| `full` | 50 % (circle) | Buttons, badges, sliders, switches |

```kotlin
Box(
    modifier = Modifier
        .clip(SparkTheme.shapes.medium)
        .background(SparkTheme.colors.surface)
)
```

### Custom shapes

```kotlin
SparkTheme(
    shapes = sparkShapes(
        medium = RoundedCornerShape(8.dp),
        large = RoundedCornerShape(20.dp),
    ),
) { /* … */ }
```

### `useRebrandedShapes`

`SparkFeatureFlag.useRebrandedShapes` opts in to updated corner radii for buttons, chips, tags, and
text fields introduced during the Adevinta rebranding. Set it to `true` once your product has
adopted the new visual identity:

```kotlin
SparkTheme(
    colors = myColors,
    sparkFeatureFlag = SparkFeatureFlag(useRebrandedShapes = true),
) { /* … */ }
```

#### Component token objects

Each affected component family exposes a token object that resolves the active shape (and any
related spacing) for the current flag value. Composables consume these internally; you can read
them to match component geometry in custom layouts or wrappers.

| Object | API |
|---|---|
| `ButtonTokens` | `shape: Shape`, `buttonShape: ButtonShape` |
| `ChipTokens` | `shape: Shape`, `leadingIconSpacing: Dp` |
| `TagTokens` | `shape: Shape` |
| `TextFieldTokens` | `shape: Shape` |
| `IconButtonTokens` | `resolveShape(fallback: Shape): Shape`, `resolveFullShape(fallback: Shape): Shape` |

All members are `@Composable` and must be read inside a composition:

```kotlin
// Match a custom overlay to the current button shape
Box(modifier = Modifier.clip(ButtonTokens.shape)) { /* … */ }
```

`ButtonTokens`, `ChipTokens`, `TagTokens`, and `TextFieldTokens` expose plain `shape` properties. `IconButtonTokens` uses functions instead because icon button composables accept a caller-supplied shape as the legacy fallback — the token object needs that argument to resolve the correct value.

---

## Typography tokens

`SparkTypography` covers display through legal text.

| Token | Size / weight | Use |
|---|---|---|
| `display1` | 40 sp Bold | Short, important large text |
| `display2` | 32 sp Bold | Short, important medium text |
| `display3` | 24 sp Bold | Short, important small text |
| `headline1` | 20 sp Bold | High-emphasis large heading |
| `headline2` | 18 sp Bold | High-emphasis medium heading |
| `subhead` | 16 sp Bold | High-emphasis small heading |
| `body1` | 16 sp Regular | Primary body text |
| `body2` | 14 sp Regular | Secondary body text (theme default) |
| `caption` | 12 sp Regular | Support text, error messages |
| `small` | 10 sp Regular | Legal text, app bar labels |
| `callout` | 16 sp Bold | Call-to-action labels |

```kotlin
Text(
    text = "Price",
    style = SparkTheme.typography.headline2,
)
```

Apply `highlight` to any style to switch its weight to Bold:

```kotlin
Text(
    text = "Important",
    style = SparkTheme.typography.body2.highlight,
)
```

### Custom typography

```kotlin
val myTypography = sparkTypography(
    display1 = TextStyle(
        fontFamily = MyBrandFont,
        fontSize = 40.sp,
        fontWeight = FontWeight.Bold,
        lineHeight = 56.sp,
    ),
    body2 = TextStyle(
        fontFamily = MyBrandFont,
        fontSize = 14.sp,
        fontWeight = FontWeight.Normal,
        lineHeight = 20.sp,
    ),
)

SparkTheme(typography = myTypography) { /* … */ }
```

Alternatively, pass a `fontFamily` parameter to `SparkTheme` directly to apply a single font
family across all type styles without replacing each style individually:

```kotlin
SparkTheme(
    colors = myColors,
    fontFamily = sparkFontFamily(/* custom FontFamily */),
) { /* … */ }
```
