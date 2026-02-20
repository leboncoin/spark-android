# Package com.adevinta.spark.components.snackbars

[Snackbars](https://spark.adevinta.com/1186e1705/p/36d4af-snack-bar--toast/b/380770)
inform users of a process that an app has performed or will perform
They appear temporarily, towards the bottom of the screen.
They should not interrupt the user experience, and they don’t require user input to disappear.
Only one snackbar may be displayed at a time.

| Light                                                                                                | Dark                                                                                             |
|------------------------------------------------------------------------------------------------------|--------------------------------------------------------------------------------------------------|
| ![](../../images/com.adevinta.spark.snackbar_SnackbarDocScreenshot_snackbarDocScreenshot__light.png) | ![](../../images/com.adevinta.spark.snackbar_SnackbarDocScreenshot_snackbarDocScreenshot__dark.png) |

## Basic Usage

The minimal usage of the component requires a message content:

```kotlin
Snackbar {
    Text("Your changes have been saved")
}
```

## Using with SnackbarHost

To display snackbars in your app, use them with a `SnackbarHost`:

```kotlin
private val snackbarHostState = remember { SnackbarHostState() }

LaunchedEffect(conversationsState) {
    if (shouldShowSnackbar) {
        snackbarHostState.showSnackbar(
            message = "Message",
            duration = SnackbarDuration.Short)
    }
}
Scaffold(
    snackbarHost = {
        SnackbarHost(hostState = snackbarHostState)
    },
) { innerPadding ->
    // Content
}
```

## Custom Icon

You can override the default intent icon by providing a custom `icon` parameter. When provided, the custom icon is displayed; otherwise, the snackbar falls back to the intent's default icon.

```kotlin
Snackbar(
    intent = SnackbarIntent.Success,
    icon = SparkIcons.FlashlightFill,
) {
    Text("Custom icon snackbar")
}
```

## Title

An optional `title` parameter displays a title above the message content.

```kotlin
Snackbar(
    intent = SnackbarIntent.Alert,
    title = "Warning",
) {
    Text("Please review your changes before proceeding")
}
```

## Title with Custom Icon

You can combine both `title` and `icon` parameters:

```kotlin
Snackbar(
    intent = SnackbarIntent.Info,
    title = "Information",
    icon = SparkIcons.FlashlightFill,
) {
    Text("This is an informational message with a custom icon")
}
```

## With Action

Snackbars can include a single action button:

```kotlin
Snackbar(
    intent = SnackbarIntent.Success,
    actionLabel = "Undo",
    onActionClick = { /* Handle action */ },
) {
    Text("Item deleted")
}
```

## With Dismiss Action

Enable a dismiss icon to allow users to manually close the snackbar:

```kotlin
Snackbar(
    intent = SnackbarIntent.Info,
    onDismissClick = { /* Handle dismiss */ },
) {
    Text("This snackbar can be dismissed")
}
```
