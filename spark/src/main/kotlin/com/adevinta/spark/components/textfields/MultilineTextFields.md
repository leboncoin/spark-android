# Package com.adevinta.spark.components.textfields

## MultilineTextFields

[MultilineTextFields](https://spark.adevinta.com/1186e1705/p/365c2e-text-viewarea/b/0658e2) allow users to enter text into a UI. They typically appear in forms and dialogs.
The multiline variant can show a character counter and a clear value icon button.

![](../../images/com.adevinta.spark.textfields_TextFieldDocScreenshot_multilineTextFieldShowcase__light.png)

The minimal usage of the component is the value of your TextField and the callback to be called
when the user type a new character but you can configure your TextField much more.

```kotlin
MultilineTextField(
    value = "Input",
    label = { Text("Label") },
    placeholder = { Text("Placeholder") },
    leadingIcon = {
        Icon(
            imageVector = Icons.Filled.ArrowDropDown,
            contentDescription = null
        )
    },
    onValueChange = { newValue -> }
)
```

#### Prefix/Suffix

```kotlin
MultilineTextField(
    value = "Input",
    label = { Text("Label") },
    placeholder = { Text("Placeholder") },
    leadingIcon = {
        // Prefix
        Text("Prefix", modifier = Modifier.padding(start = 16.dp))
    },
    leadingIcon = {
        // Suffix
        Text("Suffix", modifier = Modifier.padding(end = 16.dp))
    },
    onValueChange = { newValue -> }
)
```
