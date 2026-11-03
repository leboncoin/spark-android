# Package com.adevinta.spark.components.textfields

## Textfields

[Textfields](https://spark.adevinta.com/1186e1705/p/773c60-input/b/0658e2) allow users to enter text into a UI. They typically appear in forms and dialogs.


![](../../images/com.adevinta.spark.textfields_TextFieldDocumentationScreenshots_textFieldShowcase_light.png)

The minimal usage of the component is the value of your TextField and the callback to be called
when the user type a new character but you can configure your TextField much more.

```kotlin
var value by remember { mutableStateOf("Input") }
TextField(
    value = value,
    label = "Label",
    placeholder = "Placeholder",
    leadingContent = {
        Icon(
            imageVector = Icons.Filled.ArrowDropDown,
            contentDescription = null
        )
    },
    onValueChange = { value = it },
)
```

#### Prefix/Suffix

```kotlin
var value by remember { mutableStateOf("Input") }
TextField(
    value = value,
    label = "Label",
    placeholder = "Placeholder",
    leadingContent = {
        // Prefix
        Text("Prefix", modifier = Modifier.padding(start = 16.dp))
    },
    trailingContent = {
        // Suffix
        Text("Suffix", modifier = Modifier.padding(end = 16.dp))
    },
    onValueChange = { value = it },
)
```
