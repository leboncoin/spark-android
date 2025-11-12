# Package com.adevinta.spark.components.textfields

## Dropdowns

[Dropdown](https://spark.adevinta.com/1186e1705/p/773c60-input--text-field/b/0658e2) allow users to select an option from a dropdown.

They typically appear in forms and dialogs.
Dropdown is an interactive element that allows users to select an option from a list of choices presented in a collapsible menu. It saves space on the interface by concealing the options until the user interacts with the component.


<table width="100%">
    <thead>
        <tr>
            <td>Textfield</td>
            <td>Items & Groups</td>
        </tr>
    </thead>
    <tr>
        <td width="50%"><img src="../../images/com.adevinta.spark.textfields_DropdownScreenshot_expanded.png"/></td>
        <td width="50%"><img src="../../images/com.adevinta.spark.textfields_DropdownScreenshot_groups.png"/></td>
    </tr>
</table>

The minimal usage of the component is :

- The value of your TextField
- The callback when the user exit the dismiss the dropdown without selecting a value.
- A value to indicate if the dropdown should be shown or not.
- A value

```kotlin
private val options = listOf("Option 1", "Option 2", "Option 3", "Option 4", "Option 5")
private var expanded by remember { mutableStateOf(false) }
private var selectedOptionText by remember { mutableStateOf(options[0]) }
Dropdown(
    value = selectedOptionsText,
    label = "label",
    onDismissRequest = {
        expanded = false
    },
    expanded = expanded,
    onExpandedChange = {
        expanded = !expanded
    },
) {
    options.forEach { selectionOption ->
        DropdownMenuItem(
            onClick = {
                selectedOptionText = selectionOption
                expanded = false
            },
        ) {
            Text(text = selectionOption)
        }
    }
}
```

#### Multi selection

Like single selection, the selection system is handled by the developer. It enables you to have a 
multi selection dropdown logics that don't force you into using a specific model but your own.

Here's an example of a multi selection dropdown that you could use in your project:

```kotlin
// Your data source from your state model
private val DropdownStubData = persistentListOf(
    "To Kill a Mockingbird", "War and Peace", "The Idiot",
    "A Picture of Dorian Gray", "1984", "Pride and Prejudice",
)
private val multiSelectionFilter by remember { mutableStateOf(DropdownStubData) }

private var selectedItems by remember {
    mutableStateOf(listOf("To Kill a Mockingbird", "War and Peace"))
}
private val multiSelectedValues by remember(selectedItems.size) {
    derivedStateOf {
        val suffix = if (selectedItems.size > 1) ", +${selectedItems.size - 1}" else ""
        selectedItems.firstOrNull().orEmpty() + suffix
    }
}

SelectTextField(
    value = multiSelectedValues,
    // ...
) {
    multiSelectionFilter.forEach { book ->
        // This should be part of your model otherwise it's a huge work that done on
        // each items but we're simplifying things since it's an example here.
        val isSelected = book in selectedItems
        DropdownMenuItem(
            text = {
                Text(
                    text = book,
                    style = if (isSelected) {
                        SparkTheme.typography.body1.highlight
                    } else {
                        SparkTheme.typography.body1
                    },
                )
            },
            onClick = {
                selectedItems = if (book in selectedItems) {
                    selectedItems - book
                } else {
                    selectedItems + book
                }
                // Here we want to have the dropdown to stay expanded when we multiSelect but
                // you could use the line below if in the case you want to have the same behaviour
                // as the single selection.
                // expanded = false
            },
        )
    }
}
```

#### Item groups

Dropdowns items can be grouped, you'll need to provide a label on it though.
You can sort them like you want and the logic to show or not the group on sort is up to you.

Here's an example of a grouped dropdown that you could use in your project:

```kotlin
private val singleSelectionFilter by remember { mutableStateOf(DropdownStubData) }

private var singleSelected by remember { mutableStateOf("") }

Dropdown(
    value = singleSelected,
    // ...
) {
    singleSelectionFilter.forEach { (groupName, books) ->
        DropdownMenuGroupItem(
            title = { Text(groupName) }, // You can customize the style of the label here
        ) {
            books.forEach { book ->
                DropdownMenuItem(
                    text = { Text(book) },
                    onClick = {
                        singleSelected = book
                        expanded = false
                    },
                )
            }
        }
    }
}
```
