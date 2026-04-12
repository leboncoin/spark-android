# Package com.adevinta.spark.components.chips

[Chips](https://spark.adevinta.com/1186e1705/p/17568d-chip/b/98915d) help users quickly recognize an important information that has been entered by them,
trigger actions, make selections, or filter content.

Most commonly chip contains an optional `leadingIcon` and the text.

### Styles

The chip can have one of the `ChipStyles`:

#### Outlined

![](../../images/com.adevinta.spark.components.chips_ChipDocumentationScreenshots_chipOutlined.png)

Outlined chips use a solid border stroke and no background.

#### Tinted

![](../../images/com.adevinta.spark.components.chips_ChipDocumentationScreenshots_chipTinted.png)

Tinted chips use one of the "containers" colors for a filled background.

#### Dashed

![](../../images/com.adevinta.spark.components.chips_ChipDocumentationScreenshots_chipDashed.png)

Dashed chips use a dashed border and no background.

The color is set using one of the `ChipIntent`s:
- Support (default color)
- Accent
- Main
- Success
- Alert
- Danger
- Info
- Neutral
- Surface

To draw a chip with an optional leading icon and text.
```kotlin
ChipOutlined(
    text = "default chip",
    leadingIcon = SparkIcons.CalendarOutline,
    onClick = {},
)
```

To pass a custom content:

```kotlin
ChipSelectable(
    onClick = {},
) {
    Text(text = "Animals")
    Icon(
        sparkIcon = SparkIcons.ArrowHorizontalUp,
        modifier = Modifier.size(12.dp),
        contentDescription = null,
        tint = LocalContentColor.current,
    )
}
```

### Handling selection
Chips support selection with the ChipSelectable Variant.

#### Single selection
You'll need to add the `selectableGroup` modifier to the parent container of the chips.

and specify the `Role.RadioButton` semantics for each chip to indicate that only one of the chip
can be selected at a time.
 ```kotlin
fun Modifier.semantics {
    this.role = Role.RadioButton
}
```
```kotlin
val filters by remember {
    mutableStateOf(
        persistentListOf("Fruit","Vegetable"),
    )
}
var singleSelected by remember { mutableStateOf("Fruit") }
FlowRow(
    horizontalArrangement = spacedBy(8.dp),
    modifier = Modifier.selectableGroup()
) {
    filters.forEach { filter ->
        ChipSelectable(
            modifier = Modifier.semantics {
                role = Role.RadioButton
            },
            text = filter,
            selected = singleSelected == filter,
            onClick = { singleSelected = filter },
        )
    }
}
```

#### Multiple selection
```kotlin
val filters by remember {
    mutableStateOf(
        persistentListOf("Animal","Flower","Tree")
    )
}
var selectedFilters by remember { mutableStateOf(listOf("Animal", "Tree")) }
FlowRow(
    horizontalArrangement = spacedBy(8.dp),
) {
    filters.forEach { filter ->
        val selected = filter in selectedFilters
        ChipSelectable(
            text = filter.name,
            selected = selected,
            leadingIcon = if (selected) SparkIcons.Check else null,
            onClick = {
                unionSelected = if (selected) {
                    selectedFilters - filter
                } else {
                    selectedFilters + filter
                }
            },
        )
    }
}
```
