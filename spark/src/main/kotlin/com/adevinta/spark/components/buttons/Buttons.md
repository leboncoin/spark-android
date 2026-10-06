# Package com.adevinta.spark.components.buttons

[Buttons](https://spark.adevinta.com/1186e1705/p/34b742-button/b/32e1a2) help people take action,
such as sending an email, sharing a document, or liking a comment.
Buttons communicate actions that users can take. They are typically placed throughout your UI, in
places like:

- Dialogs
- Modal windows
- Forms
- Cards
- Toolbars

## Button API

Buttons are accessed as extension functions on the `Button` object. Each variant accepts either
a `text: String` parameter or a `content: @Composable RowScope.() -> Unit` slot for custom content.

```kotlin
Button.Primary(
    onClick = { /*Click event*/ },
    text = "Main",
)
```

All variants share these common parameters:

| Parameter | Description |
|-----------|-------------|
| `onClick` | Called when the user clicks the button |
| `modifier` | Modifier applied to the button |
| `size` | Button size (`ButtonSize.Small`, `Medium`, `Large`) |
| `enabled` | Controls the enabled state |
| `icon` | Optional icon at the start or end of the button |
| `iconSide` | Side where the icon is displayed (`IconSide.START` or `END`) |
| `isLoading` | Shows a circular progress indicator |
| `interactionSource` | Stream of interactions for custom appearance |

The buttons have a loading state that can be used to indicate that the button is loading some
data and show/hide an indeterminate circular progress indicator on the start of the button.

![](../../images/loading-button.gif)

## Variants

| Variant | Purpose |
|---------|---------|
| `Button.Primary` | Used once per view *(not including a modal dialog)*, these buttons have the most emphasis. It should never be doubled up to sit side by side. Used when an action is clearly more important than others and you need to draw attention to it. |
| `Button.Secondary` | The standard button for most use cases. The filled styling places less emphasis than Primary but still signals an important action. |
| `Button.Tertiary` | Used for miscellaneous actions: the action is important, but may not be what the user is looking to do right then. Often paired with a Primary or Secondary button. |
| `Button.Boost` | Used for any action attached to a conversion or monetisation flow, such as "Boost my ad" or "Upgrade". |
| `Button.Ai` *(experimental)* | Used for AI-powered actions. Always displays the Sparks icon to signal AI involvement. |
| `Button.Danger` | Used for destructive or irreversible actions like "Delete" or "Remove". Warns the user of negative consequences. |
| `Button.Success` | Used for positive confirmations like "Approve" or "Mark as sold" where the outcome is beneficial. |
| `Button.Contrast` | Used for primary actions on dark media or dark backgrounds. |
| `Button.Text` *(experimental)* | Used for the most minor actions. Often paired with a Primary button. Can be used with Secondary and Tertiary buttons when the primary action is for forwarding. |
| `Button.Underlined` | Used for link-style actions within body text, such as "Terms and conditions" or "Privacy policy". |
| `Button.Ghost` | Used for the most minor actions, especially when presenting multiple options. The container is not visible until interaction. |

`Button.Ai` and `Button.Text` are marked `@ExperimentalSparkApi`.

### Button.Primary

```kotlin
Button.Primary(
    onClick = { /*Click event*/ },
    text = "Main",
)
```

### Button.Secondary

```kotlin
Button.Secondary(
    onClick = { /*Click event*/ },
    text = "Secondary",
)
```

### Button.Tertiary

```kotlin
Button.Tertiary(
    onClick = { /*Click event*/ },
    text = "Tertiary",
)
```

### Button.Boost

```kotlin
Button.Boost(
    onClick = { /*Click event*/ },
    text = "Boost",
)
```

### Button.Ai

```kotlin
Button.Ai(
    onClick = { /*Click event*/ },
    text = "Ask AI",
)
```

### Button.Danger

```kotlin
Button.Danger(
    onClick = { /*Click event*/ },
    text = "Delete",
)
```

### Button.Success

```kotlin
Button.Success(
    onClick = { /*Click event*/ },
    text = "Confirm",
)
```

### Button.Contrast

```kotlin
Button.Contrast(
    onClick = { /*Click event*/ },
    text = "Contrast",
)
```

### Button.Text

```kotlin
Button.Text(
    onClick = { /*Click event*/ },
    text = "Learn more",
)
```

### Button.Underlined

```kotlin
Button.Underlined(
    onClick = { /*Click event*/ },
    text = "Terms and conditions",
)
```

### Button.Ghost

```kotlin
Button.Ghost(
    onClick = { /*Click event*/ },
    text = "Ghost",
)
```

## Migrating from the legacy API

The style-based functions `ButtonFilled`, `ButtonOutlined`, `ButtonTinted`, `ButtonGhost` and
`ButtonContrast` remain supported. They are not deprecated yet. A later release will deprecate them
once consumers have migrated. New code should use the `Button.*` variants.

Set `SparkFeatureFlag.useRebrandedButtons` to `true` to preview the migration. The flag defaults to
`false`. When it is on, each legacy call renders the matching `Button.*` variant from the table
below.

| Legacy call | Intent | New variant |
|-------------|--------|-------------|
| `ButtonFilled` | `Main` (default) | `Button.Primary` |
| `ButtonFilled` | `Support` | `Button.Secondary` |
| `ButtonFilled` | `Accent` | `Button.Boost` |
| `ButtonFilled` | `Info`, `Alert`, `Neutral` | `Button.Tertiary` |
| `ButtonOutlined`, `ButtonTinted` | `Main`, `Support`, `Accent`, `Info`, `Alert`, `Neutral` | `Button.Tertiary` |
| `ButtonFilled`, `ButtonOutlined`, `ButtonTinted` | `Surface` | `Button.Contrast` |
| `ButtonFilled`, `ButtonOutlined`, `ButtonTinted` | `Success` | `Button.Success` |
| `ButtonFilled`, `ButtonOutlined`, `ButtonTinted` | `Danger` | `Button.Danger` |
| `ButtonGhost` | Any intent except `Ai` | `Button.Ghost` |
| `ButtonContrast` | Any intent except `Ai` | `Button.Contrast` |
| Any legacy call | `Ai` | `Button.Ai` |

`ButtonOutlined` defaults to the `Support` intent. The other legacy functions default to `Main`.

Keep these differences in mind when you migrate:

- The `Button.*` variants have no `intent` parameter. Pick the variant that matches the intent.
- The `Button.*` variants have no `AnnotatedString` overload. Use the `content` slot for styled text.
- `ButtonOutlined` uses the intent colour for its text and border. `Button.Tertiary` uses `onSurface`
  for its text and the neutral `outline` colour for its border.
- `ButtonContrast` uses the intent `onContainerColor` for its content. `Button.Contrast` uses
  `onSurface`.

#### ButtonFilled

![](../../images/com.adevinta.spark.buttons_ButtonDocumentationScreenshots_buttonFilled.png)

Filled buttons are the standard button for most use cases. The filled styling places the most
emphasis and should be used for important, final actions.

```kotlin
ButtonFilled(
    text = "Main",
    onClick = { /*Click event*/ },
)
```

#### ButtonOutlined

![](../../images/com.adevinta.spark.buttons_ButtonDocumentationScreenshots_buttonOutlined.png)

Outlined buttons are used for support actions. The outlined styling places less emphasis on these
actions that are important but not the main ones.

```kotlin
ButtonOutlined(
    text = "Main",
    onClick = { /*Click event*/ },
)
```

#### ButtonTinted

![](../../images/com.adevinta.spark.buttons_ButtonDocumentationScreenshots_buttonTinted.png)

Tinted buttons are medium-emphasis buttons. An alternative middle ground between filled and outlined
buttons.

```kotlin
ButtonTinted(
    text = "Main",
    onClick = { /*Click event*/ },
)
```

#### ButtonGhost

![](../../images/com.adevinta.spark.buttons_ButtonDocumentationScreenshots_buttonGhost.png)

Ghost buttons are used for the lowest priority actions. The container is not visible until
interaction.

```kotlin
ButtonGhost(
    text = "Main",
    onClick = { /*Click event*/ },
)
```

#### ButtonContrast

![](../../images/com.adevinta.spark.buttons_ButtonDocumentationScreenshots_buttonContrast.png)

Contrast buttons are used for high to mid priority actions on dark backgrounds.

```kotlin
ButtonContrast(
    text = "Main",
    onClick = { /*Click event*/ },
)
```
