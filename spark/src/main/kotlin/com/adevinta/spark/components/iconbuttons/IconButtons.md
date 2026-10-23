# Package com.adevinta.spark.components.buttons

[IconButtons](https://spark.adevinta.com/1186e1705/p/2352e9-icon-button/b/32e1a2) take supplementary
actions with a single tap. They're used when a compact button is required, such as in a toolbar or
image list.

## IconButton API

Icon buttons are accessed as extension functions on the `IconButton` object. Each variant takes an
icon and a required `contentDescription`.

```kotlin
IconButton.Primary(
    icon = LeboncoinIcons.GearOutline,
    onClick = { /*Click event*/ },
    contentDescription = "Settings",
)
```

All variants share these parameters:

| Parameter | Description |
|-----------|-------------|
| `icon` | The `SparkIcon` drawn inside the button |
| `onClick` | Called when the user clicks the button |
| `contentDescription` | Required. Text that accessibility services use to describe the action |
| `modifier` | Modifier applied to the button |
| `size` | Button size (`IconButtonSize.Small`, `Medium`, `Large`) |
| `enabled` | Controls the enabled state |
| `isLoading` | Shows a circular progress indicator instead of the icon |
| `interactionSource` | Stream of interactions for custom appearance |
| `atEnd` | Renders an animated icon at the end of its animation |

When `isLoading` is `true`, an indeterminate spinner replaces the icon and uses the content colour of
the variant.

## Variants

| Variant | Purpose |
|---------|---------|
| `IconButton.Primary` | Used once per view *(not including a modal dialog)*. It has the most emphasis and must not sit side by side with another Primary. |
| `IconButton.Secondary` | The standard icon button for most use cases. It has less emphasis than Primary but still signals an important action. |
| `IconButton.Tertiary` | Used for miscellaneous actions that are important but may not be what the user wants to do right then. Often paired with a Primary or Secondary icon button. |
| `IconButton.Boost` | Used for icon-only actions attached to a conversion or monetisation flow, such as boosting an ad. |
| `IconButton.Ai` | Used for AI-powered actions. It is experimental and needs `@ExperimentalSparkApi`. |
| `IconButton.Danger` | Used for destructive or irreversible actions such as "Delete" or "Remove". |
| `IconButton.Success` | Used for positive confirmations such as "Approve" where the outcome is beneficial. |
| `IconButton.Contrast` | Used on coloured or image backgrounds where other icon buttons lack visibility. |
| `IconButton.Ghost` | Used for the lowest priority actions. The container is not visible until interaction. |

### IconButton.Primary

![](../../images/com.adevinta.spark.iconbutton_RebrandedIconButtonDocumentationScreenshots_iconButtonPrimary.png)

```kotlin
IconButton.Primary(
    icon = LeboncoinIcons.GearOutline,
    onClick = { /*Click event*/ },
    contentDescription = "Settings",
)
```

### IconButton.Secondary

![](../../images/com.adevinta.spark.iconbutton_RebrandedIconButtonDocumentationScreenshots_iconButtonSecondary.png)

```kotlin
IconButton.Secondary(
    icon = LeboncoinIcons.GearOutline,
    onClick = { /*Click event*/ },
    contentDescription = "Settings",
)
```

### IconButton.Tertiary

![](../../images/com.adevinta.spark.iconbutton_RebrandedIconButtonDocumentationScreenshots_iconButtonTertiary.png)

```kotlin
IconButton.Tertiary(
    icon = LeboncoinIcons.GearOutline,
    onClick = { /*Click event*/ },
    contentDescription = "Settings",
)
```

### IconButton.Boost

![](../../images/com.adevinta.spark.iconbutton_RebrandedIconButtonDocumentationScreenshots_iconButtonBoost.png)

```kotlin
IconButton.Boost(
    icon = LeboncoinIcons.GearOutline,
    onClick = { /*Click event*/ },
    contentDescription = "Boost",
)
```

### IconButton.Ai

![](../../images/com.adevinta.spark.iconbutton_RebrandedIconButtonDocumentationScreenshots_iconButtonAi.png)

`IconButton.Ai` is experimental. Opt in with `@OptIn(ExperimentalSparkApi::class)`.

```kotlin
IconButton.Ai(
    icon = LeboncoinIcons.Sparks,
    onClick = { /*Click event*/ },
    contentDescription = "Ask AI",
)
```

### IconButton.Danger

![](../../images/com.adevinta.spark.iconbutton_RebrandedIconButtonDocumentationScreenshots_iconButtonDanger.png)

```kotlin
IconButton.Danger(
    icon = LeboncoinIcons.GearOutline,
    onClick = { /*Click event*/ },
    contentDescription = "Delete",
)
```

### IconButton.Success

![](../../images/com.adevinta.spark.iconbutton_RebrandedIconButtonDocumentationScreenshots_iconButtonSuccess.png)

```kotlin
IconButton.Success(
    icon = LeboncoinIcons.Check,
    onClick = { /*Click event*/ },
    contentDescription = "Confirm",
)
```

### IconButton.Contrast

![](../../images/com.adevinta.spark.iconbutton_RebrandedIconButtonDocumentationScreenshots_iconButtonContrast.png)

```kotlin
IconButton.Contrast(
    icon = LeboncoinIcons.GearOutline,
    onClick = { /*Click event*/ },
    contentDescription = "Settings",
)
```

### IconButton.Ghost

![](../../images/com.adevinta.spark.iconbutton_RebrandedIconButtonDocumentationScreenshots_iconButtonGhost.png)

```kotlin
IconButton.Ghost(
    icon = LeboncoinIcons.GearOutline,
    onClick = { /*Click event*/ },
    contentDescription = "Settings",
)
```

## Legacy API

The top-level composable functions `IconButtonFilled`, `IconButtonOutlined`, `IconButtonTinted`, `IconButtonGhost` and `IconButtonContrast` remain available.

When `SparkFeatureFlag.useRebrandedButtons` is on, each legacy icon button renders the variant that
the table below gives for its style and intent. The `Ai` intent always renders `IconButton.Ai`. The
`Ghost` and `Contrast` styles render `IconButton.Ghost` and `IconButton.Contrast` for every other
intent. When the flag is off, the legacy rendering stays unchanged.

| Intent | `IconButtonFilled` | `IconButtonOutlined` | `IconButtonTinted` | `IconButtonGhost` | `IconButtonContrast` |
|--------|--------------------|----------------------|--------------------|-------------------|----------------------|
| Main | Primary | Tertiary | Tertiary | Ghost | Contrast |
| Support | Secondary | Tertiary | Tertiary | Ghost | Contrast |
| Accent | Boost | Tertiary | Tertiary | Ghost | Contrast |
| Surface | Contrast | Contrast | Contrast | Ghost | Contrast |
| Success | Success | Success | Success | Ghost | Contrast |
| Danger | Danger | Danger | Danger | Ghost | Contrast |
| Info, Alert, Neutral | Tertiary | Tertiary | Tertiary | Ghost | Contrast |
| Ai | Ai | Ai | Ai | Ai | Ai |

### Styles

Icon buttons come in various styles:

- Filled
- Tinted
- Outlined
- Contrast
- Ghost

| Enabled                                                                            | Disabled                                                                            |
|------------------------------------------------------------------------------------|-------------------------------------------------------------------------------------|
| ![](../../images/com.adevinta.spark.iconbutton_IconButtonScreenshot_enabled.png)   | ![](../../images/com.adevinta.spark.iconbutton_IconButtonScreenshot_disabled.png)   |

### Sizes

Icon buttons come in 3 sizes `IconButtonSize`:

- small - 32.dp (however, the minimum touch size is applied and is 44.dp)
- medium - 44.dp (default)
- large - 56.dp

The content icon is 16.dp for `IconButtonSize.Small` and `IconButtonSize.Medium`, and 24.dp
for `IconButtonSize.Large`

### Loading

The buttons have an loading state that can be used to indicate that the button is loading some
data and show/hide an indeterminate circular progress indicator on the start of the button.

![](../../images/loading-button.gif)

### Intents

Icon Buttons support all intents:
- Support (default)
- Accent
- Main
- Success
- Alert
- Danger
- Info
- Neutral
- Surface
- Ai

#### IconButtonFilled

Filled icon buttons are the standard for most use cases. The filled styling places the most
emphasis and should be used for important actions.

```kotlin
fun IconButtonFilled(
    icon: SparkIcon,
    onClick: () -> Unit,
)
```

#### IconButtonOutlined

Outlined icon buttons are used for support actions. The outlined styling places less emphasis on these
actions that are important but not the main ones.

Be aware that it's not advised to use it on top of images since it will be hard to see.

```kotlin
fun IconButtonOutlined(
    icon: SparkIcon,
    onClick: () -> Unit,
)
```

#### IconButtonTinted

Tinted icon buttons are medium-emphasis buttons that is an alternative middle ground between
default filled icon buttons and outlined icon buttons. They can be used in contexts where lower-priority
icon button requires slightly more emphasis than an outline would give.

```kotlin
fun IconButtonTinted(
    icon: SparkIcon,
    onClick: () -> Unit,
)
```

#### IconButtonGhost

Ghost icon buttons are used for the lowest priority actions, especially when presenting multiple options.

Ghost icon buttons can be placed on a variety of backgrounds. Until the button is interacted with, its
container isn't visible.
This button style is often used inside other components like snackbars, dialogs, and cards.

```kotlin
fun IconButtonGhost(
    icon: SparkIcon,
    onClick: () -> Unit,
)
```

#### IconButtonContrast

Contrast icon buttons are used for the high to mid priority actions when the background is dark like on
an image or a video.

```kotlin
fun IconButtonContrast(
    icon: SparkIcon,
    onClick: () -> Unit,
)
```
