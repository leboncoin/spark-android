# Package com.adevinta.spark.components.toggles

[Switches](https://spark.adevinta.com/1186e1705/p/58a2c6-switch/b/700a17) can be used in forms on a
full page, in modals, or on side panels.
They can be used in a list but we shouldn’t mix them with other components such
as `Checkbox` or `RadioButton`.

![](../../images/com.adevinta.spark.components.toggles_SwitchDocumentationScreenshots_switchStates.png)

Switches must respect the established color code and not use other colors to emphasize the
activation and deactivation of a functionality or service.

Switch component allows the user to activate or deactivate the state of an element or concept.
It is usually used as an element to add services, activate functionalities or adjust settings.
It is also used to control binary options (On/Off or True/False).

### SwitchLabelled

The minimal usage of the component is a standalone checkbox but you can add a label
using `SwitchLabelled`.
Please refer to design specs to find what content is accepted.

- An icon can be added on the right of label.
- A caption can be added in different positions of the label in order to to expand the information.

```kotlin
var checked by remember { mutableStateOf(false) }
SwitchLabelled(
    checked = checked,
    onCheckedChange = {
        checked = !checked
    }
) { Text(text = "Switch On") }
```

When the label content is taller than the switch (e.g. multi-line text), use `verticalAlignment`
to control how the switch widget aligns within the row. The default is `Alignment.Top`.

```kotlin
SwitchLabelled(
    checked = checked,
    onCheckedChange = { checked = it },
    verticalAlignment = Alignment.Top,
) { Text("A long label that wraps across\nmultiple lines") }
```

### Styles

The `Switch` and `SwitchLabelled` now only have one style: **"Support"**.

## Layout

- The Switch respects the minimum touch size.
- Switch labels are now always positioned at the right/end for improved accessibility.

### SwitchGroup

> 🚀 TODO
> We plan to provide a layout composable to make it easier to follow the design specs.
