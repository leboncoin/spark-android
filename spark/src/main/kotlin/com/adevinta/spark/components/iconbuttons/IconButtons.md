# Package com.adevinta.spark.components.buttons

[Icon buttons](https://spark.adevinta.com/1186e1705/p/2352e9-icon-button/b/32e1a2) let people take a
supplementary action with one click. Use an icon button when a compact button is required, such as
in
a toolbar or an image list.

## IconButton API

To add an icon button, call a variant of the `IconButton` object. Each variant is an extension
function of that object, and it takes an icon and a required content description.

```kotlin
IconButton.Primary(
    icon = LeboncoinIcons.GearOutline,
    onClick = { /*Click event*/ },
    contentDescription = "Settings",
)
```

All variants take the same parameters:

| Parameter            | Description                                                                                                                   | Default                   |
|----------------------|-------------------------------------------------------------------------------------------------------------------------------|---------------------------|
| `icon`               | The `SparkIcon` to draw at the centre of the icon button                                                                      | None                      |
| `onClick`            | The callback that runs when the user clicks the icon button. It does not run while `isLoading` is `true`                      | None                      |
| `contentDescription` | The localised text that screen readers announce and that the icon button shows as its tooltip. A blank value shows no tooltip | None                      |
| `modifier`           | The `Modifier` to apply to the icon button                                                                                    | `Modifier`                |
| `size`               | The `IconButtonSize`: `Small`, `Medium`, or `Large`                                                                           | `Medium`                  |
| `enabled`            | Whether the icon button responds to user input. When `false`, it ignores input and draws its disabled colours                 | `true`                    |
| `isLoading`          | Whether a spinner replaces the icon to show a loading state                                                                   | `false`                   |
| `interactionSource`  | The `MutableInteractionSource` that emits the interaction events of the icon button                                           | A remembered new instance |
| `atEnd`              | Whether an animated icon is drawn at the end of its animation. A static icon ignores it                                       | `false`                   |

### Sizes

The `size` parameter takes one of the three `IconButtonSize` values:

| Size                              | Container | Icon  |
|-----------------------------------|-----------|-------|
| `IconButtonSize.Small`            | 32 dp     | 16 dp |
| `IconButtonSize.Medium` (default) | 44 dp     | 16 dp |
| `IconButtonSize.Large`            | 56 dp     | 24 dp |

The layout reserves a touch target of at least 44 dp, so `IconButtonSize.Small` keeps a 44 dp touch
target.

### Loading

When `isLoading` is `true`, an indeterminate spinner replaces the icon, in the content colour of the
variant. The icon button stays enabled but ignores clicks. Set `isLoading` back to `false` to show
the
icon again.

![](../../images/loading-button.gif)

### Accessibility

Every variant provides the following accessibility behaviour:

- The content description is required and has no default. Localise it, for example with
  `stringResource`.
- Screen readers announce the content description, and the icon button shows it as a tooltip on long
  press and on pointer hover. A blank value shows no tooltip.
- The role is `Button`.
- While `isLoading` is `true`, the content description stays on the icon button, and screen readers
  announce the localised state "Loading".

These rules also apply to the legacy icon buttons.

## Variants

Choose the variant by the importance of the action in the view.

| Variant                | Appearance                                                 | Use for                                                     |
|------------------------|------------------------------------------------------------|-------------------------------------------------------------|
| `IconButton.Primary`   | Main colour container                                      | The one action of a view that matters most                  |
| `IconButton.Secondary` | Support colour container                                   | Most actions                                                |
| `IconButton.Tertiary`  | Transparent container, outline border                      | Important actions that are not the likely next step         |
| `IconButton.Boost`     | Accent colour container                                    | Conversion and monetisation actions, such as boosting an ad |
| `IconButton.Ai`        | AI colour container (experimental)                         | AI-powered actions                                          |
| `IconButton.Danger`    | Transparent container, outline border, error colour icon   | Destructive or irreversible actions                         |
| `IconButton.Success`   | Transparent container, outline border, success colour icon | Positive confirmations                                      |
| `IconButton.Contrast`  | Surface colour container                                   | Coloured or image backgrounds                               |
| `IconButton.Ghost`     | No container, icon follows the content colour              | The lowest priority actions                                 |

### IconButton.Primary

Use `IconButton.Primary` for the one action of a view that matters more than all others and needs
attention. Use it once per view, not counting a modal dialog, and do not place two next to each
other.

![](../../images/com.adevinta.spark.iconbutton_RebrandedIconButtonDocumentationScreenshots_iconButtonPrimary.png)

```kotlin
IconButton.Primary(
    icon = LeboncoinIcons.GearOutline,
    onClick = { /*Click event*/ },
    contentDescription = "Settings",
)
```

### IconButton.Secondary

`IconButton.Secondary` is the standard icon button for most actions. It has less emphasis than
`IconButton.Primary` but still signals an important action.

![](../../images/com.adevinta.spark.iconbutton_RebrandedIconButtonDocumentationScreenshots_iconButtonSecondary.png)

```kotlin
IconButton.Secondary(
    icon = LeboncoinIcons.GearOutline,
    onClick = { /*Click event*/ },
    contentDescription = "Settings",
)
```

### IconButton.Tertiary

Use `IconButton.Tertiary` for an action that is important but may not be what the user wants to do
right then. It is often paired with `IconButton.Primary` or `IconButton.Secondary`. The container is
transparent, the icon uses the on-surface colour, and the border uses the outline colour.

![](../../images/com.adevinta.spark.iconbutton_RebrandedIconButtonDocumentationScreenshots_iconButtonTertiary.png)

```kotlin
IconButton.Tertiary(
    icon = LeboncoinIcons.GearOutline,
    onClick = { /*Click event*/ },
    contentDescription = "Settings",
)
```

### IconButton.Boost

Use `IconButton.Boost` for an icon-only action in a conversion or monetisation flow, such as
boosting
an ad or upgrading. For any other action, use another variant.

![](../../images/com.adevinta.spark.iconbutton_RebrandedIconButtonDocumentationScreenshots_iconButtonBoost.png)

```kotlin
IconButton.Boost(
    icon = LeboncoinIcons.GearOutline,
    onClick = { /*Click event*/ },
    contentDescription = "Boost",
)
```

### IconButton.Ai

Use `IconButton.Ai` only for AI-powered actions, with an icon that signals AI involvement, such as
the
Sparks icon.

`IconButton.Ai` is experimental and may change in the future. Opt in with
`@OptIn(ExperimentalSparkApi::class)`. Without the opt-in, the compiler reports a warning.

![](../../images/com.adevinta.spark.iconbutton_RebrandedIconButtonDocumentationScreenshots_iconButtonAi.png)

```kotlin
IconButton.Ai(
    icon = LeboncoinIcons.Sparks,
    onClick = { /*Click event*/ },
    contentDescription = "Ask AI",
)
```

### IconButton.Danger

Use `IconButton.Danger` for a destructive or irreversible action, such as "Delete" or "Remove". The
error colour warns the user of negative consequences. The container is transparent, and the border
uses the outline colour.

![](../../images/com.adevinta.spark.iconbutton_RebrandedIconButtonDocumentationScreenshots_iconButtonDanger.png)

```kotlin
IconButton.Danger(
    icon = LeboncoinIcons.GearOutline,
    onClick = { /*Click event*/ },
    contentDescription = "Delete",
)
```

### IconButton.Success

Use `IconButton.Success` for a positive confirmation, such as "Approve" or "Mark as sold", where the
outcome is beneficial. The container is transparent, and the border uses the outline colour. For a
destructive action, use `IconButton.Danger`.

![](../../images/com.adevinta.spark.iconbutton_RebrandedIconButtonDocumentationScreenshots_iconButtonSuccess.png)

```kotlin
IconButton.Success(
    icon = LeboncoinIcons.Check,
    onClick = { /*Click event*/ },
    contentDescription = "Confirm",
)
```

### IconButton.Contrast

Use `IconButton.Contrast` on a coloured or image background where the other variants lack
visibility.
The container uses the surface colour, and the icon uses the on-surface colour. On a background of
the
surface colour, the container blends in, so use another variant.

![](../../images/com.adevinta.spark.iconbutton_RebrandedIconButtonDocumentationScreenshots_iconButtonContrast.png)

```kotlin
IconButton.Contrast(
    icon = LeboncoinIcons.GearOutline,
    onClick = { /*Click event*/ },
    contentDescription = "Settings",
)
```

### IconButton.Ghost

Use `IconButton.Ghost` for the lowest priority actions, especially when the view presents several
options. It suits actions inside other components, such as snackbars, dialogs, and cards. The
container is transparent, and the icon follows the content colour of its parent.

![](../../images/com.adevinta.spark.iconbutton_RebrandedIconButtonDocumentationScreenshots_iconButtonGhost.png)

```kotlin
IconButton.Ghost(
    icon = LeboncoinIcons.GearOutline,
    onClick = { /*Click event*/ },
    contentDescription = "Settings",
)
```

## Legacy API

Existing code can keep calling the top-level functions `IconButtonFilled`, `IconButtonOutlined`,
`IconButtonTinted`, `IconButtonGhost`, and `IconButtonContrast`. For new code, use the variants of
the
`IconButton` object.

Unlike the variants, the legacy functions take a nullable content description, `String? = null`. A
`null` value leaves the icon button without an accessible name and without a tooltip, so pass `null`
only for a purely decorative icon.

When `SparkFeatureFlag.useRebrandedButtons` is on, each legacy icon button draws the colours of the
variant that the following table gives for its intent and style. The `Ai` intent always gives the
`IconButton.Ai` colours. The `Ghost` and `Contrast` styles give the `IconButton.Ghost` and
`IconButton.Contrast` colours for every other intent. When the flag is off, the legacy rendering
stays
unchanged.

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

The legacy API offers the following styles:

- Filled
- Tinted
- Outlined
- Contrast
- Ghost

| Enabled                                                                            | Disabled                                                                            |
|------------------------------------------------------------------------------------|-------------------------------------------------------------------------------------|
| ![](../../images/com.adevinta.spark.iconbutton_IconButtonScreenshot_enabled.png)   | ![](../../images/com.adevinta.spark.iconbutton_IconButtonScreenshot_disabled.png)   |

### Intents

The legacy API offers the following intents. The default is Main.

- Main
- Support
- Accent
- Success
- Alert
- Danger
- Info
- Neutral
- Surface
- Ai

#### IconButtonFilled

Use `IconButtonFilled` for important actions. The filled styling places the most emphasis on the
action.

```kotlin
IconButtonFilled(
    icon = LeboncoinIcons.GearOutline,
    onClick = { /*Click event*/ },
    contentDescription = "Settings",
)
```

#### IconButtonOutlined

Use `IconButtonOutlined` for support actions that are important but not the main ones. The outlined
styling places less emphasis on the action. Do not use it on top of images, because it is hard to
see
there.

```kotlin
IconButtonOutlined(
    icon = LeboncoinIcons.GearOutline,
    onClick = { /*Click event*/ },
    contentDescription = "Settings",
)
```

#### IconButtonTinted

Use `IconButtonTinted` for a lower priority action that needs more emphasis than an outline gives.
The tinted styling is a middle ground between the filled and the outlined styling.

```kotlin
IconButtonTinted(
    icon = LeboncoinIcons.GearOutline,
    onClick = { /*Click event*/ },
    contentDescription = "Settings",
)
```

#### IconButtonGhost

Use `IconButtonGhost` for the lowest priority actions, especially when the view presents several
options. The container is not visible until the user interacts with the icon button. You can place
it
on a variety of backgrounds, and other components often contain it, such as snackbars, dialogs, and
cards.

```kotlin
IconButtonGhost(
    icon = LeboncoinIcons.GearOutline,
    onClick = { /*Click event*/ },
    contentDescription = "Settings",
)
```

#### IconButtonContrast

Use `IconButtonContrast` for high to medium priority actions on a dark background, such as an image
or a video.

```kotlin
IconButtonContrast(
    icon = LeboncoinIcons.GearOutline,
    onClick = { /*Click event*/ },
    contentDescription = "Settings",
)
```
