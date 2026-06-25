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

The minimal usage of the component is the text and the click action.

```kotlin
ButtonFilled(
    text = "Main",
    onClick = { /*Click event*/ },
)
```

The buttons have an loading state that can be used to indicate that the button is loading some
data and show/hide an indeterminate circular progress indicator on the start of the button.

![](../../images/loading-button.gif)

### All Styles

There are multiple style variants for the button with the same parameters:

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
It is recommended to pair it with a button with more emphasis like the filled button or the tinted
button.

Be aware that it's not advised to use it on top of images since it will be hard to see.

```kotlin
ButtonOutlined(
    text = "Main",
    onClick = { /*Click event*/ },
)
```

#### ButtonTinted

![](../../images/com.adevinta.spark.buttons_ButtonDocumentationScreenshots_buttonTinted.png)

Tinted buttons are medium-emphasis buttons that is an alternative middle ground between
default Buttons (filled) and Outlined buttons. They can be used in contexts where lower-priority
button requires slightly more emphasis than an outline would give, such as "Next" in an onboarding
flow.

It's best paired with either a filled button or an outlined button.

```kotlin
ButtonTinted(
    text = "Main",
    onClick = { /*Click event*/ },
)
```

#### ButtonGhost

![](../../images/com.adevinta.spark.buttons_ButtonDocumentationScreenshots_buttonGhost.png)

Ghost buttons are used for the lowest priority actions, especially when presenting multiple options.

Ghost buttons can be placed on a variety of backgrounds. Until the button is interacted with, its
container isn't visible.
This button style is often used inside other components like snackbars, dialogs, and cards.

```kotlin
ButtonGhost(
    text = "Main",
    onClick = { /*Click event*/ },
)
```

#### ButtonContrast

![](../../images/com.adevinta.spark.buttons_ButtonDocumentationScreenshots_buttonContrast.png)

Contrast buttons are used for the high to mid priority actions when the background is dark like on
an image or a video.

```kotlin
ButtonContrast(
    text = "Main",
    onClick = { /*Click event*/ },
)
```
