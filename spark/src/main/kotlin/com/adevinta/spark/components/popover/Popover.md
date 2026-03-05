# Package com.adevinta.spark.components.popover

[Popover](https://spark.adevinta.com/1186e1705/p/88a08c-popover/b/904ceb) is kinda similar to Plain tooltip from Compose UI
Provides a descriptive message or Info for an Anchor. 
Popover that is invoked when the anchor is pressed/long pressed:

The minimal usage of the component is the content and the anchor.

```kotlin
Popover(
    popover = {
        Text(
            text = "Do you want to have this cookie now?",
        )
    },
    isDismissButtonEnabled = true,
    popoverState = popoverState,
) {
    ButtonOutlined(
        text = "Display Popover",
        onClick = { scope.launch { popoverState.show() } },
    )
}
```

The popover can also have intents as it's background color, you can fin the list [here](https://spark.adevinta.com/1186e1705/p/88a08c-popover/t/60da0b).
