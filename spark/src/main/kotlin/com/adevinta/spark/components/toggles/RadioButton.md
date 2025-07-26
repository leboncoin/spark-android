# Package com.adevinta.spark.components.toggles

The [radio button](https://spark.adevinta.com/1186e1705/p/98058f-radio-button/b/700a17) allow
users to select one option from a set.

- Use radio buttons to select a single option from a list
- It should be visible at a glance if a radio button has been selected, and selected items should be
  more visually prominent than unselected items.
- Present a list showing all available options. If available options can be collapsed, consider
  using a dropdown menu because it uses less space.

| Light                                                                                   | Dark                                                                                   |
|-----------------------------------------------------------------------------------------|----------------------------------------------------------------------------------------|
| ![](../../images/com.adevinta.spark.toggles_RadioButtonScreenshot_all_states_light.png) | ![](../../images/com.adevinta.spark.toggles_RadioButtonScreenshot_all_states_dark.png) |

The minimal usage of the component is the radio button in standalone but you can add a content at
the end of the radio or customize it.

```kotlin
var selected by remember { mutableStateOf(true) }
RadioButton(
    selected = selected,
    onClick = {
        selected = !selected
    }
)
```

---

### CheckBoxLabelled

The main radio button allow users to select one option from a set.

- Use radio buttons to select a single option from a list
- It should be visible at a glance if a radio button has been selected, and selected items should be
  more visually prominent than unselected items.
- Present a list showing all available options. If available options can be collapsed, consider
  using a dropdown menu because it uses less space.

The minimal usage of the component is the radiobutton in standalone but you can add a content at the
end of the box or customize it.

```kotlin
var selected by remember { mutableStateOf(true) }
RadioButtonLabelled(
    selected = selected,
    onClick = {
        selected = !selected
    }
) { Text("RadioButton On") }
```

### Styles

The `RadioButton` and `RadioButtonLabelled` can have an error style by passing `error = true` to its
parameters.

## Layout

### RadioGroup

> 🚀 TODO
> We plan to provide a layout composable to make it easier to follow the design specs.

```kotlin
@Composable
fun RadioGroupSample() {
    val radioOptions = listOf("Calls", "Missed", "Friends")
    val (selectedOption, onOptionSelected) = remember { mutableStateOf(radioOptions[0]) }
    // Note that Modifier.selectableGroup() is essential to ensure correct accessibility behavior
    Column(Modifier.selectableGroup()) {
        radioOptions.forEach { text ->
            RadioButtonLabelled(
                selected = text == selectedOption,
                onClick = { selectedOption(text) },
            ) { Text(text) }
        }
    }
}
```

## Form states 🚀 TODO
