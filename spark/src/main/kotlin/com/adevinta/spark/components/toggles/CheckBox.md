# Package com.adevinta.spark.components.toggles

[Checkboxes](https://spark.adevinta.com/1186e1705/p/76f5a8-checkbox/b/98915d) allows users to select
one or more items from a set. Checkboxes can turn an option on or off.

![](../../images/com.adevinta.spark.components.toggles_CheckboxDocumentationScreenshots_checkboxStates.png)

- Toggle a single item on or off.
- Require another action to activate or deactivate something.
- In cases of a global activation in a indeterminate state where on and off states coexist in the
  children.

The minimal usage of the component is the checkbox in standalone but you can add a content at the
end of the box or customize it.

```kotlin
var checkedState by remember { mutableStateOf(ToggleableState.On) }
Checkbox(
    state = checkedState,
    onClick = {
        checkedState = if (checkedState == ToggleableState.On) ToggleableState.Off else ToggleableState.On
    }
)
```

`Checkbox.Standalone` takes the same parameters as `Checkbox`. Pass `onClick = null` for a passive
checkbox, for example when a parent owns the click and the state.

```kotlin
Checkbox.Standalone(
    state = ToggleableState.On,
    onClick = null,
)
```

---

### CheckBoxLabelled

The Checkbox allows users to select one or more items from a set. Checkboxes can turn an option on
or off.

The minimal usage of the component is the checkbox in standalone but you can add a content at the
end of the box or customize it.

```kotlin
var checkedState by remember { mutableStateOf(ToggleableState.On) }
CheckboxLabelled(
    state = checkedState,
    onClick = {},
) { Text("CheckBox On") }
```

When the label content is taller than the checkbox (e.g. multi-line text), use `verticalAlignment`
to control how the checkbox widget aligns within the row. The default is `Alignment.Top`.

```kotlin
CheckboxLabelled(
    state = checkedState,
    onClick = {},
    verticalAlignment = Alignment.CenterVertically,
) { Text("A long label that wraps across\nmultiple lines") }
```

### Styles

The `Checkbox` and `CheckboxLabelled` can have an error style by passing `error = true` to its parameters.
`Checkbox.Card` takes an error message instead. See "Selection cards" below.

## Layout

### CheckBoxGroup

> 🚀 TODO
> We plan to provide a layout composable to make it easier to follow the design specs.

```kotlin
@Composable
fun CheckboxSample() {
    Column {
        // define dependent checkboxes states
        var checked by remember { mutableStateOf(true) }
        var checked2 by remember { mutableStateOf(true) }
        // The parent state reflects the state of the dependent checkboxes
        val parentState = when {
            checked && checked2 -> ToggleableState.On
            !checked && !checked2 -> ToggleableState.Off
            else -> ToggleableState.Indeterminate
        }
        // A click on the parent sets the state of the dependent checkboxes
        CheckboxLabelled(
            state = parentState,
            onClick = {
                val newValue = parentState != ToggleableState.On
                checked = newValue
                checked2 = newValue
            },
        ) { Text("All") }
        Column(modifier = Modifier.selectableGroup()) {
            Option(option = "First", checked = checked, onCheckedChange = { checked = it })
            Option(option = "Second", checked = checked2, onCheckedChange = { checked2 = it })
        }
    }
}

@Composable
fun Option(
    option: String,
    checked: Boolean,
    onCheckedChange: (Boolean) -> Unit,
) {
    CheckboxLabelled(
        state = ToggleableState(checked),
        onClick = { onCheckedChange(!checked) },
    ) { Text(option) }
}
```

## Selection cards

To present each option as a bordered card, use `Checkbox.Card`. The usage and design rules for cards are in the "Selection cards" section of this package.

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

## Form states 🚀 TODO
