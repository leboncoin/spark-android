# Package com.adevinta.spark.components.textfields

## MultilineTextFields

[MultilineTextFields](https://spark.adevinta.com/1186e1705/p/365c2e-text-viewarea/b/0658e2) allow users to enter text into a UI. They typically appear in forms and dialogs.
The multiline variant can show a character counter and a clear value icon button.
The clear icon shows only when the value is not blank. A tap on it calls `onCancelClick`.

![](../../images/com.adevinta.spark.textfields_TextFieldDocumentationScreenshots_multilineTextFieldShowcase_light.png)

The minimal usage of the component is the value of your field, `onValueChange`, and `onCancelClick`.
You can configure the field much more.

```kotlin
var value by remember { mutableStateOf("Input") }
MultilineTextField(
    value = value,
    onValueChange = { value = it },
    onCancelClick = { value = "" },
    label = "Label",
    placeholder = "Placeholder",
    leadingContent = {
        Icon(
            sparkIcon = LeboncoinIcons.ArrowHorizontalUp,
            contentDescription = null,
        )
    },
)
```

#### Prefix

The multiline variant has no trailing slot, because the clear icon uses it. You cannot add a suffix.

```kotlin
var value by remember { mutableStateOf("Input") }
MultilineTextField(
    value = value,
    onValueChange = { value = it },
    onCancelClick = { value = "" },
    label = "Label",
    placeholder = "Placeholder",
    leadingContent = {
        // Prefix
        Text("Prefix", modifier = Modifier.padding(start = 16.dp))
    },
)
```
