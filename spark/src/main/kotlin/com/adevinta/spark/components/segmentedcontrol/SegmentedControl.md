# Package com.adevinta.spark.components.segmentedcontrol

[SegmentedControl](https://spark.adevinta.com/guidelines/segmented-control) allows users to select a single option from a small set of choices, while maintaining an immediate overview of the available alternatives.

## When to Use

- Selecting a single option from 2-8 choices
- Filtering or sorting content
- Switching between different views or modes

## API Overview

```kotlin
object SegmentedControl {
    @Composable
    fun SingleSelect(
        selectedIndex: Int,
        onSegmentSelect: (Int) -> Unit,
        modifier: Modifier = Modifier,
        title: String? = null,
        linkText: String? = null,
        onLinkClick: (() -> Unit)? = null,
        enabled: Boolean = true,
        content: @Composable SegmentedControlScope.() -> Unit,
    )
}
```

## Adaptive Layout

The component adapts its layout based on segment count:

| Segments | Layout                    |
|----------|---------------------------|
| 2-4      | Single row                |
| 5-8      | Multi-row (two balanced rows) |

## Segment Types

Each segment can contain different content types:

- **SingleLine** - Single line of text
- **TwoLine** - Two lines (title and subtitle)
- **Icon** - Icon only
- **IconText** - Icon with text
- **Number** - Number display
- **Custom** - Custom content with customisable selected background colour

## Usage Examples

### Basic Usage

```kotlin
var selectedIndex by remember { mutableIntStateOf(0) }

SegmentedControl.SingleSelect(
    selectedIndex = selectedIndex,
    onSegmentSelect = { selectedIndex = it },
) {
    SingleLine("Option 1")
    SingleLine("Option 2")
    SingleLine("Option 3")
    SingleLine("Option 4")
}
```

### With Title and Link

```kotlin
SegmentedControl.SingleSelect(
    selectedIndex = selectedIndex,
    onSegmentSelect = { selectedIndex = it },
    title = "Filter",
    linkText = "Learn more",
    onLinkClick = { /* Show help */ }
) {
    SingleLine("All")
    SingleLine("Active")
    SingleLine("Completed")
}
```

### Different Content Types

```kotlin
SegmentedControl.SingleSelect(
    selectedIndex = selectedIndex,
    onSegmentSelect = { selectedIndex = it },
) {
    SingleLine("Text")
    TwoLine("Title", "Subtitle")
    Icon(LeboncoinIcons.ShoppingCartOutline)
    IconText(LeboncoinIcons.ShoppingCartOutline, "Cart")
    Number(42)
}
```

### Multi-Row (5+ segments)

```kotlin
SegmentedControl.SingleSelect(
    selectedIndex = selectedIndex,
    onSegmentSelect = { selectedIndex = it },
) {
    SingleLine("A")
    SingleLine("B")
    SingleLine("C")
    SingleLine("D")
    SingleLine("E")
    SingleLine("F")
    SingleLine("G")
}
```

### Custom Segment

```kotlin
SegmentedControl.SingleSelect(
    selectedIndex = selectedIndex,
    onSegmentSelect = { selectedIndex = it },
) {
    SingleLine("Standard")
    Custom(selectedBackgroundColor = Color.Green) {
        Row {
            Icon(LeboncoinIcons.Star, contentDescription = null)
            Text("Premium")
        }
    }
}
```

## Accessibility

- Tab role semantics for each segment
- Content descriptions for icons
- State announcements when selection changes
- Keyboard navigation support
- Minimum touch target size of 48dp

## Design Guidelines

Refer to the [official design specifications](https://spark.adevinta.com/guidelines/segmented-control) for detailed visual and interaction guidelines.

Key design principles:
- Use short, explicit labels
- Keep content homogeneous between segments
- Avoid more than 8 elements (prefer a Dropdown for more options)
- Do not use for multiple selection
