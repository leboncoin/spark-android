# Package com.adevinta.spark.components.segmentedcontrol

[SegmentedControl](https://spark.adevinta.com/guidelines/segmented-control) allows users to select a single option from a small set of choices, while maintaining an immediate overview of the available alternatives.

## When to Use

- Selecting a single option from 2-8 choices
- Filtering or sorting content
- Switching between different views or modes
- Displaying value scales (e.g., energy ratings A-G)

## API Overview

```kotlin
object SegmentedControl {
    @Composable
    fun Horizontal(
        selectedIndex: Int,
        onSegmentSelected: (Int) -> Unit,
        modifier: Modifier = Modifier,
        title: String? = null,
        linkText: String? = null,
        onLinkClick: (() -> Unit)? = null,
        customSelectedColors: List<Color>? = null,
        enabled: Boolean = true,
        content: @Composable SegmentedControlScope.() -> Unit,
    )

    @Composable
    fun Vertical(
        selectedIndex: Int,
        onSegmentSelected: (Int) -> Unit,
        modifier: Modifier = Modifier,
        title: String? = null,
        linkText: String? = null,
        onLinkClick: (() -> Unit)? = null,
        customSelectedColors: List<Color>? = null,
        enabled: Boolean = true,
        content: @Composable SegmentedControlScope.() -> Unit,
    )
}
```

## Variants

The component supports two layout variants:

| Variant   | Max Segments | Layout                    |
|-----------|--------------|---------------------------|
| Horizontal| 4            | Single row                |
| Vertical  | 8            | Multi-row (typically 2x4) |

## Segment Types

Each segment can contain different content types:

- **SingleLine** - Single line of text
- **TwoLine** - Two lines (title and subtitle)
- **Icon** - Icon only
- **IconText** - Icon with text
- **Number** - Number display
- **Custom** - Custom content with customizable selected background color

## Usage Examples

### Basic Horizontal Usage

```kotlin
var selectedIndex by remember { mutableIntStateOf(0) }

SegmentedControl.Horizontal(
    selectedIndex = selectedIndex,
    onSegmentSelected = { selectedIndex = it },
) {
    SingleLine("Option 1")
    SingleLine("Option 2")
    SingleLine("Option 3")
    SingleLine("Option 4")
}
```

### With Title and Link

```kotlin
SegmentedControl.Horizontal(
    selectedIndex = selectedIndex,
    onSegmentSelected = { selectedIndex = it },
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
SegmentedControl.Horizontal(
    selectedIndex = selectedIndex,
    onSegmentSelected = { selectedIndex = it },
) {
    SingleLine("Text")
    TwoLine("Title", "Subtitle")
    Icon(SparkIcons.ShoppingBag)
    IconText(SparkIcons.ShoppingBag, "Cart")
    Number(42)
}
```

### Custom Colors for Value Scales

```kotlin
val energyColors = listOf(
    Color(0xFF4CAF50), // A - Green
    Color(0xFF8BC34A), // B - Light Green
    Color(0xFFCDDC39), // C - Yellow Green
    Color(0xFFFFEB3B), // D - Yellow
    Color(0xFFFFC107), // E - Amber
    Color(0xFFFF9800), // F - Orange
    Color(0xFFF44336), // G - Red
)

SegmentedControl.Vertical(
    selectedIndex = selectedIndex,
    onSegmentSelected = { selectedIndex = it },
    customSelectedColors = energyColors,
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
SegmentedControl.Horizontal(
    selectedIndex = selectedIndex,
    onSegmentSelected = { selectedIndex = it },
) {
    SingleLine("Standard")
    Custom(selectedBackgroundColor = Color.Green) {
        Row {
            Icon(SparkIcons.Star, contentDescription = null)
            Text("Premium")
        }
    }
}
```

## Accessibility

- Proper semantics for radio button group
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
