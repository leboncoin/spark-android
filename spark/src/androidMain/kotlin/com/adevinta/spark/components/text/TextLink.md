# Package com.adevinta.spark.components.popover

[TextLink](https://spark.adevinta.com/1186e1705/p/75ed11-textlink/b/403107)
A TextLink is a reference to a resource.

It can be external (e.g. a different web page) or internal (e.g. a specific element in the current
page).

##### Variants:

#### TextLink

```kotlin
fun TextLink(
    text: Int,
    onClickLabel: String,
    onClick: () -> Unit,
)
```

| Light                                                                                   | Dark                                                                                   |
|-----------------------------------------------------------------------------------------|----------------------------------------------------------------------------------------|
| ![](../../images/com.adevinta.spark.text_TextLinkScreenshot_testTextLink_light.png)     | ![](../../images/com.adevinta.spark.text_TextLinkScreenshot_testTextLink_dark.png)     |

#### TextLinkButton

```kotlin
fun TextLinkButton(
    text: String,
    onClick: () -> Unit,
    intent: ButtonIntent,
    enabled: Boolean = true,
    isLoading: Boolean = false,
)
```
