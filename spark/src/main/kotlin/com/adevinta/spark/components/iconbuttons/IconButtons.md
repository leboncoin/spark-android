# Package com.adevinta.spark.components.buttons

[IconButtons](https://spark.adevinta.com/1186e1705/p/2352e9-icon-button/b/32e1a2) take supplementary
actions with a single tap. They're used when a compact button is required, such as in a toolbar or
image list.

### Styles

Icon buttons come in various styles:

- Filled
- Tinted
- Outlined
- Contrast
- Ghost

| Enabled                                                                            | Disabled                                                                            | Shapes                                                                          |
|------------------------------------------------------------------------------------|-------------------------------------------------------------------------------------|---------------------------------------------------------------------------------|
| ![](../../images/com.adevinta.spark.iconbutton_IconButtonScreenshot_enabled.png)   | ![](../../images/com.adevinta.spark.iconbutton_IconButtonScreenshot_disabled.png)   | ![](../../images/com.adevinta.spark.iconbutton_IconButtonScreenshot_shape.png)  |

### Sizes

Icon buttons come in 3 sizes `IconButtonSize`:

- small - 32.dp (however, the minimum touch size is applied and is 44.dp)
- medium - 44.dp (default)
- large - 56.dp

The content icon is 16.dp for `IconButtonSize.Small` and `IconButtonSize.Medium`, and 24.dp
for `IconButtonSize.Large`

### Shapes
Icon buttons come in 3 shapes `ButtonShape`:
- Square
- Rounded (default)
- Pill

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
