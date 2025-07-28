# Package com.adevinta.spark.components.toggles

[Checkboxes](https://spark.adevinta.com/1186e1705/p/76f5a8-checkbox/b/98915d) allows users to select
one or more items from a set. Checkboxes can turn an option on or off.

- Toggle a single item on or off.
- Require another action to activate or deactivate something.
- In cases of a global activation in a indeterminate state where on and off states coexist in the
  children.

| Light                                                                                | Dark                                                                                               |
|--------------------------------------------------------------------------------------|----------------------------------------------------------------------------------------------------|
| ![](../../images/com.adevinta.spark.toggles_CheckboxScreenshot_all_states_light.png) | ![](../../images/com.adevinta.spark.toggles_CheckboxScreenshot_all_states_dark.png) |

The minimal usage of the component is the checkbox in standalone but you can add a content at the
end of the box or customize it.

```kotlin
var checkedState by remember { mutableStateOf(ToggleableState.On) }
Checkbox(
    state = checkedState,
    onClick = {
        isChecked = !isChecked
    }
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

### Styles

The `CheckBox` and `CheckboxLabelled` can have an error style by passing `error = true` to its parameters.

## Layout

### CheckBoxGroup

> 🚀 TODO
> We plan to provide a layout composable to make it easier to follow the design specs.

```kotlin
@Composable
fun CheckboxSample() {
    Column {
        // define dependent checkboxes states
        val (state, onStateChange) = remember { mutableStateOf(ToggleableState.On) }
        val (state2, onStateChange2) = remember { mutableStateOf(ToggleableState.On) }
        // TriStateCheckbox state reflects state of dependent checkboxes
        val parentState = remember(state, state2) {
            if (state && state2) ToggleableState.On
            else if (!state && !state2) ToggleableState.Off
            else ToggleableState.Indeterminate
        }
        // click on TriStateCheckbox can set state for dependent checkboxes
        val onParentClick = {
            val s = parentState != ToggleableState.On
            onStateChange(s)
            onStateChange2(s)
        }
        Option(
            state = parentState,
            onClick = onParentClick,
        )
        Column(modifier = Modifier.selectableGroup()) {
            Option(state, onStateChange)
            Option(state2, onStateChange2)
        }
    }
}

@Composable
fun Option(
    option: String,
    onClick: (() -> Unit)
) {
    CheckboxLabelled(
        state = checkedState,
        onClick = onClick,
    ) { Text(option) }
}
```

## Form states 🚀 TODO
