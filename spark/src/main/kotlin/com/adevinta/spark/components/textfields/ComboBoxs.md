# Package com.adevinta.spark.components.textfields

## ComboBox

[ComboBox](https://spark.adevinta.com/1186e1705/p/773c60-input--text-field/b/0658e2) combines a text field with a dropdown menu, providing a searchable interface for selecting options. It's particularly useful when users need to select from a large list of options while having the ability to search or filter the choices.

They typically appear in forms and dialogs where users need to search through a large set of options.

<table width="100%">
    <thead>
        <tr>
            <td>Single Choice</td>
            <td>Multi Choice</td>
        </tr>
    </thead>
    <tr>
        <td width="50%"><img src="../../images/com.adevinta.spark.textfields_ComboBoxScreenshot_singleChoice.png"/></td>
        <td width="50%"><img src="../../images/com.adevinta.spark.textfields_ComboBoxScreenshot_multiChoice.png"/></td>
    </tr>
</table>

The minimal usage of the component is:

```kotlin
private val options = listOf("Option 1", "Option 2", "Option 3", "Option 4", "Option 5")
private var expanded by remember { mutableStateOf(false) }
private val state = rememberTextFieldState()

SingleChoiceComboBox(
    state = state,
    expanded = expanded,
    onExpandedChange = { expanded = it },
    onDismissRequest = { expanded = false },
    label = "Select an option",
) {
    options.forEach { option ->
        DropdownMenuItem(
            text = { Text(option) },
            selected = false,
            onClick = { expanded = false }
        )
    }
}
```

### Multi selection

The multi-selection ComboBox allows users to select multiple options, with selected choices displayed as chips below the text field. The selection system is handled by the developer, enabling custom multi-selection logic.

Here's an example of a multi-selection ComboBox:

```kotlin
// Your data source from your state model
private val ComboBoxStubData = persistentListOf(
    "To Kill a Mockingbird", "War and Peace", "The Idiot",
    "A Picture of Dorian Gray", "1984", "Pride and Prejudice",
)

private var selectedChoices by remember {
    mutableStateOf(persistentListOf(
        SelectedChoice("1", "To Kill a Mockingbird"),
        SelectedChoice("2", "War and Peace")
    ))
}

MultiChoiceComboBox(
    state = rememberTextFieldState(),
    expanded = expanded,
    onExpandedChange = { expanded = it },
    onDismissRequest = { expanded = false },
    selectedChoices = selectedChoices,
    onSelectedClick = { id ->
        selectedChoices = selectedChoices.filter { it.id != id }.toPersistentList()
    },
    label = "Select books",
) {
    ComboBoxStubData.forEachIndexed { index, book ->
        DropdownMenuItem(
            text = { Text(book) },
            selected = selectedChoices.any { it.label == book },
            onClick = {
                val choice = SelectedChoice(index.toString(), book)
                selectedChoices = if (selectedChoices.any { it.id == choice.id }) {
                    selectedChoices.filter { it.id != choice.id }.toPersistentList()
                } else {
                    selectedChoices.add(choice)
                }
            }
        )
    }
}
```

### Filtering

ComboBox supports filtering through the text field input. Here's an example of implementing filtering:

```kotlin
private val allOptions = listOf("Option 1", "Option 2", "Option 3", "Option 4", "Option 5")
private var expanded by remember { mutableStateOf(false) }
private val state = rememberTextFieldState()
private val filteredOptions = remember(state.text) {
    allOptions.filter { it.contains(state.text, ignoreCase = true) }
}

SingleChoiceComboBox(
    state = state,
    expanded = expanded,
    onExpandedChange = { expanded = it },
    onDismissRequest = { expanded = false },
    label = "Search and select",
) {
    if (filteredOptions.isEmpty()) {
        DropdownMenuItem(
            text = { Text("No matches found") },
            onClick = { }
        )
    } else {
        filteredOptions.forEach { option ->
            DropdownMenuItem(
                text = { Text(option) },
                selected = false,
                onClick = { expanded = false }
            )
        }
    }
}
```

### Item groups

Like Dropdowns, ComboBox items can be grouped. You can provide a label for each group and implement your own sorting logic.

Here's an example of a grouped ComboBox:

```kotlin
private val groupedOptions = mapOf(
    "Fiction" to listOf("To Kill a Mockingbird", "War and Peace", "The Idiot"),
    "Classics" to listOf("A Picture of Dorian Gray", "1984", "Pride and Prejudice")
)

SingleChoiceComboBox(
    state = state,
    expanded = expanded,
    onExpandedChange = { expanded = it },
    onDismissRequest = { expanded = false },
    label = "Select a book",
) {
    groupedOptions.forEach { (groupName, books) ->
        DropdownMenuGroupItem(
            title = { Text(groupName) }
        ) {
            books.forEach { book ->
                DropdownMenuItem(
                    text = { Text(book) },
                    selected = false,
                    onClick = { expanded = false }
                )
            }
        }
    }
}
```

## Usage

ComboBoxes are used when:

- Users need to select from a large list of options
- Search or filtering capabilities are required
- The list of options is dynamic or frequently updated
- Space is limited and a full dropdown list would be too large

## Types

### Single Choice ComboBox

A ComboBox that allows users to select a single option from the dropdown list.

```kotlin
@Composable
fun SingleChoiceComboBox(
    state: TextFieldState,
    expanded: Boolean,
    onExpandedChange: (Boolean) -> Unit,
    onDismissRequest: () -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    required: Boolean = false,
    label: String? = null,
    placeholder: String? = null,
    helper: String? = null,
    leadingContent: @Composable (AddonScope.() -> Unit)? = null,
    status: FormFieldStatus? = null,
    statusMessage: String? = null,
    inputTransformation: InputTransformation? = null,
    keyboardOptions: KeyboardOptions = KeyboardOptions.Default,
    onKeyboardAction: KeyboardActionHandler? = null,
    lineLimits: TextFieldLineLimits = TextFieldLineLimits.Default,
    onTextLayout: (Density.(getResult: () -> TextLayoutResult?) -> Unit)? = null,
    interactionSource: MutableInteractionSource? = null,
    outputTransformation: OutputTransformation? = null,
    scrollState: ScrollState = rememberScrollState(),
    dropdownContent: @Composable SingleChoiceDropdownItemColumnScope.() -> Unit,
)
```

### Multi Choice ComboBox

A ComboBox that allows users to select multiple options from the dropdown list. Selected choices are displayed as chips below the text field.

```kotlin
@Composable
fun MultiChoiceComboBox(
    state: TextFieldState,
    expanded: Boolean,
    onExpandedChange: (Boolean) -> Unit,
    onDismissRequest: () -> Unit,
    selectedChoices: ImmutableList<SelectedChoice>,
    onSelectedClick: (String) -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    required: Boolean = false,
    label: String? = null,
    placeholder: String? = null,
    helper: String? = null,
    leadingContent: @Composable (AddonScope.() -> Unit)? = null,
    status: FormFieldStatus? = null,
    statusMessage: String? = null,
    inputTransformation: InputTransformation? = null,
    keyboardOptions: KeyboardOptions = KeyboardOptions.Default,
    onKeyboardAction: KeyboardActionHandler? = null,
    lineLimits: TextFieldLineLimits = TextFieldLineLimits.Default,
    onTextLayout: (Density.(getResult: () -> TextLayoutResult?) -> Unit)? = null,
    interactionSource: MutableInteractionSource? = null,
    outputTransformation: OutputTransformation? = null,
    scrollState: ScrollState = rememberScrollState(),
    dropdownContent: @Composable MultiChoiceDropdownItemColumnScope.() -> Unit,
)
```

## Examples

### Basic Single Choice ComboBox

```kotlin
@Composable
fun BasicSingleChoiceComboBox() {
    val state = rememberTextFieldState()
    var expanded by remember { mutableStateOf(false) }
    
    SingleChoiceComboBox(
        state = state,
        expanded = expanded,
        onExpandedChange = { expanded = it },
        onDismissRequest = { expanded = false },
        label = "Select an option",
        placeholder = "Choose an option",
    ) {
        for (i in 1..5) {
            DropdownMenuItem(
                text = { Text("Option $i") },
                selected = false,
                onClick = { expanded = false }
            )
        }
    }
}
```

### Basic Multi Choice ComboBox

```kotlin
@Composable
fun BasicMultiChoiceComboBox() {
    val state = rememberTextFieldState()
    var expanded by remember { mutableStateOf(false) }
    var selectedChoices by remember { mutableStateOf(persistentListOf<SelectedChoice>()) }
    
    MultiChoiceComboBox(
        state = state,
        expanded = expanded,
        onExpandedChange = { expanded = it },
        onDismissRequest = { expanded = false },
        selectedChoices = selectedChoices,
        onSelectedClick = { id ->
            selectedChoices = selectedChoices.filter { it.id != id }.toPersistentList()
        },
        label = "Select options",
        placeholder = "Choose options",
    ) {
        for (i in 1..5) {
            DropdownMenuItem(
                text = { Text("Option $i") },
                selected = selectedChoices.any { it.id == "option$i" },
                onClick = {
                    val choice = SelectedChoice("option$i", "Option $i")
                    selectedChoices = if (selectedChoices.any { it.id == choice.id }) {
                        selectedChoices.filter { it.id != choice.id }.toPersistentList()
                    } else {
                        selectedChoices.add(choice)
                    }
                }
            )
        }
    }
}
```
