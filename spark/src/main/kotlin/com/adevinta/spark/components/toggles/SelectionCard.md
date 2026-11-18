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

| Layout                            | Control position                          |
|-----------------------------------|-------------------------------------------|
| `SelectionCardLayout.Horizontal`  | At the start, with the content beside it  |
| `SelectionCardLayout.Vertical`    | On top, with the content centred below it |

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

Hover and pressed feedback come from the default Spark ripple. The card has no custom focus style.
The ripple also shows when the card has keyboard focus.

![](../../images/com.adevinta.spark.components.toggles_SelectionCardDocumentationScreenshots_states.png)

## Error

`Checkbox.Card` and `RadioButton.Card` accept `error = true`. The border and the default control
use the error colour. The border is 1 dp when unselected and 2 dp when selected. `Switch.Card` has
no error state.

## Empty toggle

Pass an empty `toggle` to hide the control. The card then looks like a plain selectable tile, and
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
        toggle = {},
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
