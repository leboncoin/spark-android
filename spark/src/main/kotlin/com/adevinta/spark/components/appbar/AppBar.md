# Package com.adevinta.spark.components.appbar

App bars display navigation controls and key actions at the top or bottom of a screen. Use them to give users consistent access to navigation and contextual actions across your app.

## Top App Bars

Four variants are available, differing in title size and collapsing behaviour.

| Variant | Height (expanded) | Title alignment | Collapses on scroll |
|---|---|---|---|
| `TopAppBar` | 64 dp | Start | Yes (hides entirely) |
| `CenterAlignedTopAppBar` | 64 dp | Center | Yes (hides entirely) |
| `MediumTopAppBar` | 112 dp | Start (second row) | Yes (collapses to 64 dp) |
| `LargeTopAppBar` | 152 dp | Start (second row) | Yes (collapses to 64 dp) |

All four share the same parameters:

| Parameter | Description |
|---|---|
| `title` | Composable shown as the screen title |
| `navigationIcon` | Leading icon, typically an `IconButton` with `UpNavigationIcon` or a menu icon |
| `actions` | Trailing `IconButton`s laid out in a `Row` |
| `scrollBehavior` | Controls collapse animation; provide via `TopAppBarDefaults` |
| `colors` | Override colours via the matching `TopAppBarSparkDefaults` factory |
| `windowInsets` | Insets the bar respects; defaults to `TopAppBarDefaults.windowInsets` |

### TopAppBar

Single-row bar with a start-aligned title. The bar hides entirely when the user scrolls down.

```kotlin
val scrollBehavior = TopAppBarDefaults.enterAlwaysScrollBehavior()

Scaffold(
    modifier = Modifier.nestedScroll(scrollBehavior.nestedScrollConnection),
    topBar = {
        TopAppBar(
            title = { Text("Listings") },
            navigationIcon = {
                UpNavigationIcon(onClick = onNavigateUp)
            },
            actions = {
                IconButton(onClick = onSearch) {
                    Icon(SparkIcons.SearchFill, contentDescription = "Search")
                }
            },
            scrollBehavior = scrollBehavior,
        )
    },
) { padding -> /* content */ }
```

### CenterAlignedTopAppBar

Same as `TopAppBar` but the title is centred horizontally.

```kotlin
CenterAlignedTopAppBar(
    title = { Text("My Profile") },
    navigationIcon = {
        UpNavigationIcon(onClick = onNavigateUp)
    },
)
```

### MediumTopAppBar

Two-row bar: navigation and actions on the first row, large title on the second. The title row collapses into the first row when scrolled.

```kotlin
val scrollBehavior = TopAppBarDefaults.exitUntilCollapsedScrollBehavior()

Scaffold(
    modifier = Modifier.nestedScroll(scrollBehavior.nestedScrollConnection),
    topBar = {
        MediumTopAppBar(
            title = { Text("Search results") },
            navigationIcon = {
                UpNavigationIcon(onClick = onNavigateUp)
            },
            scrollBehavior = scrollBehavior,
        )
    },
) { padding -> /* content */ }
```

### LargeTopAppBar

Same two-row pattern as `MediumTopAppBar` with a taller expanded height (152 dp) suited for prominent section headers.

```kotlin
val scrollBehavior = TopAppBarDefaults.exitUntilCollapsedScrollBehavior()

LargeTopAppBar(
    title = { Text("Favourites") },
    navigationIcon = {
        UpNavigationIcon(onClick = onNavigateUp)
    },
    scrollBehavior = scrollBehavior,
)
```

### Colours

Each variant has a matching colour factory on `TopAppBarSparkDefaults`. The container colour animates to a tonal-elevated surface when content scrolls underneath the bar.

```kotlin
TopAppBar(
    title = { Text("Custom colours") },
    colors = TopAppBarSparkDefaults.topAppBarColors(
        containerColor = SparkTheme.colors.primaryContainer,
        titleContentColor = SparkTheme.colors.onPrimaryContainer,
    ),
)
```

| Factory | Variant |
|---|---|
| `TopAppBarSparkDefaults.topAppBarColors()` | `TopAppBar` |
| `TopAppBarSparkDefaults.centerAlignedTopAppBarColors()` | `CenterAlignedTopAppBar` |
| `TopAppBarSparkDefaults.mediumTopAppBarColors()` | `MediumTopAppBar` |
| `TopAppBarSparkDefaults.largeTopAppBarColors()` | `LargeTopAppBar` |

---

## BottomAppBar

A bottom bar for navigation controls and a primary action. It optionally hosts a `FloatingActionButton` at the trailing end.

```kotlin
BottomAppBar(
    actions = {
        IconButton(onClick = onEdit) {
            Icon(SparkIcons.PenOutline, contentDescription = "Edit")
        }
        IconButton(onClick = onShare) {
            Icon(SparkIcons.Share, contentDescription = "Share")
        }
    },
    floatingActionButton = {
        FloatingActionButton(
            onClick = onAdd,
            containerColor = BottomAppBarDefaults.bottomAppBarFabColor,
            elevation = FloatingActionButtonDefaults.bottomAppBarFabElevation(),
            icon = SparkIcons.Plus,
            contentDescription = "Add",
        )
    },
)
```

### Scroll behaviour

`BottomAppBar` gains elevation when the user has scrolled down and there is content above to return to. Use `BottomAppBarSparkDefaults.bottomAppBarScrollBehavior()` and connect it via `nestedScroll`.

```kotlin
val bottomBarBehavior = BottomAppBarSparkDefaults.bottomAppBarScrollBehavior()

Scaffold(
    modifier = Modifier.nestedScroll(bottomBarBehavior.nestedScrollConnection),
    bottomBar = {
        BottomAppBar(
            scrollBehavior = bottomBarBehavior,
            actions = {
                IconButton(onClick = onCancel) {
                    Icon(SparkIcons.ArrowLeft, contentDescription = "Cancel")
                }
            },
        )
    },
) { padding -> /* content */ }
```

---

## NavigationBar

A persistent bottom destination switcher. Use it with three to five `NavigationBarItem`s.

```kotlin
var selected by remember { mutableIntStateOf(0) }

NavigationBar {
    NavigationBarItem(
        icon = SparkIcons.HomeFill,
        label = { Text("Home") },
        selected = selected == 0,
        onClick = { selected = 0 },
    )
    NavigationBarItem(
        icon = SparkIcons.SearchFill,
        label = { Text("Search") },
        selected = selected == 1,
        onClick = { selected = 1 },
    )
    NavigationBarItem(
        icon = SparkIcons.AccountFill,
        label = { Text("Profile") },
        selected = selected == 2,
        onClick = { selected = 2 },
    )
}
```

`NavigationBarItem` always shows the label for the selected item. Set `alwaysShowLabel = false` to hide labels on unselected items when space is limited.
