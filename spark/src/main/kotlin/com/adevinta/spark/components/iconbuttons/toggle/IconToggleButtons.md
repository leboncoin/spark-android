# Package com.adevinta.spark.components.iconbuttons.toggle

[IconToggleButtons](https://spark.adevinta.com/1186e1705/p/2352e9-icon-button/b/32e1a2) take supplementary
actions with a single tap. They're used when a compact button is required, such as in a toolbar or
image list.

### Styles

Icon toggle buttons come in various styles:

- Filled
- Tinted
- Outlined
- Contrast
- Ghost

| Enabled                                                                                    | Disabled                                                                                    | Shapes                                                                                    |
|--------------------------------------------------------------------------------------------|---------------------------------------------------------------------------------------------|-------------------------------------------------------------------------------------------|
| ![](../../images/com.adevinta.spark.iconbutton_IconToggleButtonScreenshot_enabled.png)     | ![](../../images/com.adevinta.spark.iconbutton_IconToggleButtonScreenshot_disabled.png)     | ![](../../images/com.adevinta.spark.iconbutton_IconToggleButtonScreenshot_shape.png)      |

### Sizes

Icon toggle buttons come in 3 sizes `IconButtonSize`:

- small - 32.dp (however, the minimum touch size is applied and is 44.dp)
- medium - 44.dp (default)
- large - 56.dp

The content icon is 16.dp for `IconToggleButtonSize.Small` and `IconToggleButtonSize.Medium`, and 24.dp
for `IconToggleButtonSize.Large`

### Intents

Icon Toggle Buttons support all intents:
- Basic (default)
- Accent
- Main
- Support
- Success
- Alert
- Danger
- Info
- Neutral
- Surface

#### IconToggleButtonFilled

Filled icon toggle buttons are the standard for most use cases. The filled styling places the most
emphasis and should be used for important actions.

```kotlin
fun IconToggleButtonFilled(
    checked: Boolean,
    onCheckedChange: (Boolean) -> Unit,
    icons: IconToggleButtonIcons,
)
```

#### IconToggleButtonOutlined

Outlined icon toggle buttons are used for support actions. The outlined styling places less emphasis on these
actions that are important but not the main ones.

Be aware that it's not advised to use it on top of images since it will be hard to see.

```kotlin
fun IconToggleButtonOutlined(
    checked: Boolean,
    onCheckedChange: (Boolean) -> Unit,
    icons: IconToggleButtonIcons,
)
```

#### IconToggleButtonTinted

Tinted icon toggle buttons are medium-emphasis buttons that is an alternative middle ground between
default filled icon toggle buttons and outlined icon toggle buttons. They can be used in contexts where lower-priority
icon button requires slightly more emphasis than an outline would give.

```kotlin
fun IconToggleButtonTinted(
    checked: Boolean,
    onCheckedChange: (Boolean) -> Unit,
    icons: IconToggleButtonIcons,
)
```

#### IconToggleButtonGhost

Ghost icon toggle buttons are used for the lowest priority actions, especially when presenting multiple options.

Ghost icon toggle buttons can be placed on a variety of backgrounds. Until the button is interacted with, its
container isn't visible.
This button style is often used inside other components like snackbars, dialogs, and cards.

```kotlin
fun IconToggleButtonGhost(
    checked: Boolean,
    onCheckedChange: (Boolean) -> Unit,
    icons: IconToggleButtonIcons,
)
```

#### IconToggleButtonContrast

Contrast icon toggle buttons are used for the high to mid priority actions when the background is dark like on
an image or a video.

```kotlin
fun IconToggleButtonContrast(
    checked: Boolean,
    onCheckedChange: (Boolean) -> Unit,
    icons: IconToggleButtonIcons,
)
```
