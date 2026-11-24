# Package com.adevinta.spark.components.toggles

## Selection cards

A selection card is a bordered card that the user taps as a whole to make a choice. It holds a
passive control (a checkbox, a radio button or a switch) and your own content.

| Card              | Use it to                                | Role                |
|-------------------|------------------------------------------|---------------------|
| `Checkbox.Card`   | Pick zero, one or several options        | `Role.Checkbox`     |
| `RadioButton.Card`| Pick exactly one option from a group     | `Role.RadioButton`  |
| `Switch.Card`     | Turn one setting on or off               | `Role.Switch`       |

The whole card is the touch target and it owns the role. The control inside the card is only a
visual cue. It does not react to clicks.

### Checkbox.Card

`Checkbox.Card` takes a `ToggleableState`, so it supports the indeterminate state.

![](../../images/com.adevinta.spark.components.toggles_SelectionCardDocumentationScreenshots_checkbox_group.png)

```kotlin
var state by remember { mutableStateOf(ToggleableState.Off) }
Checkbox.Card(
    state = state,
    onClick = {
        state = if (state == ToggleableState.On) ToggleableState.Off else ToggleableState.On
    },
) {
    Text("Delivery")
    Text("Receive the item at home")
}
```

### RadioButton.Card

`RadioButton.Card` takes a `selected` flag. Put the cards of one group in a parent with
`Modifier.selectableGroup()`. The card does not add it for you, and it is essential for correct
accessibility behaviour.

![](../../images/com.adevinta.spark.components.toggles_SelectionCardDocumentationScreenshots_radio_group.png)

```kotlin
val options = listOf("Pick-up", "Delivery")
var selectedOption by remember { mutableStateOf(options.first()) }
Column(
    modifier = Modifier.selectableGroup(),
    verticalArrangement = Arrangement.spacedBy(8.dp),
) {
    options.forEach { option ->
        RadioButton.Card(
            selected = option == selectedOption,
            onClick = { selectedOption = option },
        ) {
            Text(option)
        }
    }
}
```

### Switch.Card

`Switch.Card` takes `checked` and `onCheckedChange`. It has no `error` parameter.

![](../../images/com.adevinta.spark.components.toggles_SelectionCardDocumentationScreenshots_switch_list.png)

```kotlin
var checked by remember { mutableStateOf(false) }
Switch.Card(
    checked = checked,
    onCheckedChange = { checked = it },
) {
    Text("Notifications")
}
```

## Layouts

Use `SelectionCardLayout` to place the control relative to the content.

| Layout                            | Control position                                     |
|-----------------------------------|------------------------------------------------------|
| `SelectionCardLayout.Horizontal`  | At the start, centred vertically next to the content |
| `SelectionCardLayout.Vertical`    | On top, with the content centred below it            |

`Horizontal` is the default.

![](../../images/com.adevinta.spark.components.toggles_SelectionCardDocumentationScreenshots_horizontal.png)

![](../../images/com.adevinta.spark.components.toggles_SelectionCardDocumentationScreenshots_vertical.png)

The `content` slot is a `ColumnScope`, so you can stack text, images and other composables in it.

## States

The card has an 8 dp radius, 16 dp of padding and a 16 dp gap between the control and the content.

| State      | Border                         |
|------------|--------------------------------|
| Unselected | 1 dp, `outline` colour         |
| Selected   | 2 dp, `support` colour         |
| Disabled   | The whole card is dimmed to 40% |

The border animates its width and its colour when the selected or error state changes.

Hover and pressed feedback come from the default Spark ripple. The card has no custom focus style.
The ripple also shows when the card has keyboard focus.

`CheckboxTokens.Card`, `RadioButtonTokens.Card` and `SwitchTokens.Card` expose these values as
public tokens:

- `shape`: the corner shape of the card.
- `contentPadding`: the space between the border and the content.
- `horizontalStartSpacing`: the gap between the control and the content in the horizontal layout.
- `verticalTopSpacing`: the gap between the control and the content in the vertical layout.
- `borderWidth`: the border width of an unselected card.
- `selectedBorderWidth`: the border width of a selected card.
- `disabledAlpha`: the alpha that dims a disabled card.

![](../../images/com.adevinta.spark.components.toggles_SelectionCardDocumentationScreenshots_states.png)

## Error

`Checkbox.Card` and `RadioButton.Card` take an `error` message. Pass `null` when the card has no
error. When the message is not null, the border and the default control use the error colour. The
border is 1 dp when unselected and 2 dp when selected. `Switch.Card` has no error state.

The card sets the message as its error semantics, so screen readers read it aloud with the card.
Keep the message short and specific. A blank message throws an `IllegalArgumentException`.

```kotlin
Checkbox.Card(
    state = state,
    onClick = onClick,
    error = if (state == ToggleableState.Off) "Select at least one option" else null,
) {
    Text("Delivery")
}
```

If helper text near the card shows the same message, a screen reader can read it twice, so hide
that text from accessibility.

## Toggle slot

The `toggle` slot draws the passive control. Each card gives the slot its own scope, and the scope
passes the state of the card to the control.

| Card               | Scope                  | Scope functions               |
|--------------------|------------------------|-------------------------------|
| `Checkbox.Card`    | `CheckboxCardScope`    | `checkbox()`, `custom {}`     |
| `RadioButton.Card` | `RadioButtonCardScope` | `radioButton()`, `custom {}`  |
| `Switch.Card`      | `SwitchCardScope`      | `switch()`, `custom {}`       |

The default `toggle` calls the control of the card, for example `{ checkbox() }`. Use `custom {}`
only when that control does not fit. The last expression of the lambda must be a scope call.

```kotlin
Checkbox.Card(
    state = state,
    onClick = onClick,
    toggle = { custom { Icon(sparkIcon = LeboncoinIcons.House, contentDescription = null) } },
) {
    Text("House")
}
```

### Empty toggle

Pass `toggle = { null }` to hide the control. The card then looks like a plain selectable tile, and
the user cannot see whether one or several choices are allowed. Add helper text near the group that
states the rule, for example "Choose one option".

![](../../images/com.adevinta.spark.components.toggles_SelectionCardDocumentationScreenshots_empty_toggle_with_helper_text.png)

```kotlin
var selected by remember { mutableStateOf(true) }
Text("Choose one option")
Column(modifier = Modifier.selectableGroup()) {
    RadioButton.Card(
        selected = selected,
        onClick = { selected = true },
        toggle = { null },
    ) {
        Text("Pick-up")
    }
}
```

## Design rules

- Use at most 6 options in a group. For more options, use a dropdown.
- Keep at least one option selectable.
- Do not mix horizontal and vertical cards in a group.
- Do not mix `Switch.Card` with `Checkbox.Card` or `RadioButton.Card` in one list.
- Use a card only when a plain radio group lacks visual hierarchy.
- Keep labels short and give all cards in a group the same size.
- Use text of at least 14 px in a card.
