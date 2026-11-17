# Package com.adevinta.spark.components.dialog

The dialog package provides two composables: `AlertDialog` for concise confirmations and
`ModalScaffold` for full-screen flows with top/bottom app bars and scrollable content.

Both are annotated `@ExperimentalSparkApi`.

---

## AlertDialog

A compact dialog for alerting the user or requesting confirmation of a single action.

```kotlin
var showDialog by remember { mutableStateOf(false) }

if (showDialog) {
    AlertDialog(
        onDismissRequest = { showDialog = false },
        title = { Text("Delete listing?") },
        text = { Text("This action cannot be undone.") },
        confirmButton = {
            ButtonGhost(
                text = "Delete",
                onClick = { viewModel.delete(); showDialog = false },
            )
        },
        dismissButton = {
            ButtonGhost(
                text = "Cancel",
                onClick = { showDialog = false },
            )
        },
    )
}
```

### Parameters

| Parameter | Default | Description |
|---|---|---|
| `onDismissRequest` | - | Called when the user taps outside the dialog or presses Back |
| `confirmButton` | - | Primary action slot; no click handler is wired automatically |
| `dismissButton` | `null` | Secondary action slot; omit to show a single button |
| `icon` | `null` | Optional icon displayed above the title |
| `title` | `null` | Dialog headline; omit when body text is self-explanatory |
| `text` | `null` | Supporting body text |
| `properties` | `DialogProperties()` | Platform-specific window properties |

To prevent dismissal on back press or outside tap, pass a no-op lambda or configure
`DialogProperties`:

```kotlin
AlertDialog(
    onDismissRequest = { /* intentionally empty */ },
    properties = DialogProperties(dismissOnBackPress = false, dismissOnClickOutside = false),
    confirmButton = { /* ... */ },
)
```

Use `ButtonGhost` for action buttons inside `AlertDialog`. Filled or outlined buttons are too
visually heavy for this compact context.

---

## ModalScaffold

`ModalScaffold` presents a full-screen flow with a `TopAppBar` containing a close button, an
optional bottom action bar, and scrollable content. On tablets and foldables it renders as a
centred modal dialog instead of full-screen.

```kotlin
var showModal by remember { mutableStateOf(false) }

if (showModal) {
    ModalScaffold(
        onClose = { showModal = false },
        title = { Text("Edit profile") },
        mainButton = { modifier ->
            ButtonFilled(
                modifier = modifier,
                text = "Save",
                onClick = { viewModel.save(); showModal = false },
            )
        },
        supportButton = { modifier ->
            ButtonOutlined(
                modifier = modifier,
                text = "Discard",
                onClick = { showModal = false },
            )
        },
    ) { innerPadding ->
        ProfileForm(modifier = Modifier.padding(innerPadding))
    }
}
```

### Parameters

| Parameter | Default | Description |
|---|---|---|
| `onClose` | - | Invoked when the close button is tapped or the dialog is dismissed |
| `title` | `{}` | Composable placed in the `TopAppBar` title slot |
| `actions` | `{}` | `IconButton`s placed at the end of the `TopAppBar` |
| `mainButton` | `null` | Primary bottom-bar action; receives a `Modifier` for sizing |
| `supportButton` | `null` | Secondary bottom-bar action; receives a `Modifier` for sizing |
| `snackbarHost` | `{}` | Slot for a `SnackbarHost` |
| `contentPadding` | `PaddingValues(horizontal = 24.dp)` | Padding applied around `content` |
| `inEdgeToEdge` | feature flag | Set `true` when the host activity is edge-to-edge |

### Dismiss behaviour

The close (X) button in the top bar calls `onClose`. On phone portrait and landscape it also
dismisses when the user presses Back. On tablets it dismisses on outside tap. To prevent
dismissal, pass a no-op `onClose` lambda.

### Layout adaptation

`ModalScaffold` picks a layout automatically based on `LocalWindowSizeClass`:

| Context | Layout |
|---|---|
| Phone portrait | Full-screen `Scaffold` with stacked buttons |
| Phone landscape | Full-screen `Scaffold` with side-by-side buttons |
| Tablet / foldable | Centred dialog (280-560dp wide), `large` shape |

### Bottom bar

Omit both `mainButton` and `supportButton` to hide the bottom bar entirely. This is useful for
informational modals that need only the close button.

```kotlin
ModalScaffold(
    onClose = { showModal = false },
    title = { Text("Terms of service") },
    // No mainButton or supportButton - bottom bar hidden
) { innerPadding ->
    TermsContent(Modifier.padding(innerPadding))
}
```

### Snackbar

Pass a `SnackbarHost` to `snackbarHost` to display in-modal feedback:

```kotlin
val snackbarHostState = remember { SnackbarHostState() }

ModalScaffold(
    onClose = { showModal = false },
    snackbarHost = { SnackbarHost(snackbarHostState) },
    mainButton = { modifier ->
        ButtonFilled(modifier = modifier, text = "Submit", onClick = {
            scope.launch { snackbarHostState.showSnackbar("Saved") }
        })
    },
) { innerPadding ->
    FormContent(Modifier.padding(innerPadding))
}
```
