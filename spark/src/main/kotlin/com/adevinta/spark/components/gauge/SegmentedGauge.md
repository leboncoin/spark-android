# Package com.adevinta.spark.components.gauge

[SegmentedGauge](https://spark.adevinta.com/1186e1705/p/71a76b-segmented-gauge/b/269150) is a non-interactive visual indicator made up of colored segments. It shows a level or value along a scale (e.g., from "low" to "high"). The gauge is used to visually illustrate the quality of an abstract element (e.g., contact, pricing, rating, etc.). It has a limited granularity (between 3 and 5 colored cells) to help users instantly understand the value of the associated element.

## When to Use

- Display price level (e.g., fair or overpriced)
- Indicate potential (e.g., to explore, moderate, high)
- Show a score or rating
- When you need to show a visual level or performance in a quick, simple way
- When you want to compare several items on the same scale (e.g., products)
- When the user doesn't need to interact (read-only data)
- When the value is qualitative, not exact (e.g., "moderate", not "75%")

## When Not to Use

- If the data is precise or numeric (use a chart or numeric gauge instead)
- If the user must interact (select, filter, or edit)
- If color meaning isn't clear or consistent
- If the gauge would be too small to read easily (dense mobile layouts, compact tables)
- For evaluating an element based on human input (use the "rating" component instead)
- When a bi-directional scale (center-out) is required

## API Overview

### SegmentedGauge (Five Segments)

```kotlin
@Composable
fun SegmentedGauge(
    modifier: Modifier = Modifier,
    size: GaugeSize = GaugeDefaults.Size,
    type: GaugeTypeFive? = null,
    color: Color = GaugeDefaults.Color,
    testTag: String? = null,
)
```

### SegmentedGaugeShort (Three Segments)

```kotlin
@Composable
fun SegmentedGaugeShort(
    modifier: Modifier = Modifier,
    size: GaugeSize = GaugeDefaults.Size,
    type: GaugeTypeThree? = null,
    color: Color = GaugeDefaults.Color,
    testTag: String? = null,
)
```

## Variants

### Five-Segment Gauge Types

| Type | Description | Light Theme | Dark Theme |
|------|-------------|-------------|------------|
| VeryHigh | Excellent/Optimal status | ![VeryHigh Light](screenshots/segmentedGauge_five_segment_types_light.png) | ![VeryHigh Dark](screenshots/segmentedGauge_five_segment_types_dark.png) |
| High | Good/Above average status | ![High Light](screenshots/segmentedGauge_five_segment_types_light.png) | ![High Dark](screenshots/segmentedGauge_five_segment_types_dark.png) |
| Medium | Average/Neutral status | ![Medium Light](screenshots/segmentedGauge_five_segment_types_light.png) | ![Medium Dark](screenshots/segmentedGauge_five_segment_types_dark.png) |
| Low | Below average/Poor status | ![Low Light](screenshots/segmentedGauge_five_segment_types_light.png) | ![Low Dark](screenshots/segmentedGauge_five_segment_types_dark.png) |
| VeryLow | Critical/Bad status | ![VeryLow Light](screenshots/segmentedGauge_five_segment_types_light.png) | ![VeryLow Dark](screenshots/segmentedGauge_five_segment_types_dark.png) |

### Three-Segment Gauge Types

| Type | Description | Light Theme | Dark Theme |
|------|-------------|-------------|------------|
| VeryHigh | Excellent/Optimal status | ![VeryHigh Light](screenshots/segmentedGauge_three_segment_types_light.png) | ![VeryHigh Dark](screenshots/segmentedGauge_three_segment_types_dark.png) |
| Low | Below average/Poor status | ![Low Light](screenshots/segmentedGauge_three_segment_types_light.png) | ![Low Dark](screenshots/segmentedGauge_three_segment_types_dark.png) |
| VeryLow | Critical/Bad status | ![VeryLow Light](screenshots/segmentedGauge_three_segment_types_light.png) | ![VeryLow Dark](screenshots/segmentedGauge_three_segment_types_dark.png) |

### Sizes

| Size | Width × Height | Indicator Size | Example |
|------|----------------|----------------|---------|
| Small | 24 × 8 dp | 12 dp | ![Small Size](screenshots/segmentedGauge_sizes_light.png) |
| Medium | 34 × 12 dp | 16 dp | ![Medium Size](screenshots/segmentedGauge_sizes_light.png) |

### Custom Colors

When semantic colors don't fit your use case, you can provide custom colors:

![Custom Colors](screenshots/segmentedGauge_custom_colors_light.png)

## Usage Examples

### Basic Implementation

```kotlin
// Five-segment gauge for quality rating
SegmentedGauge(type = GaugeTypeFive.VeryHigh)

// Three-segment gauge for potential indicator
SegmentedGaugeShort(type = GaugeTypeThree.Low)

// Custom colored gauge
SegmentedGauge(
    type = GaugeTypeFive.Medium,
    color = Color.Blue
)

// Different sizes
SegmentedGauge(
    size = GaugeSize.Small,
    type = GaugeTypeFive.High
)
```

### Contact Purchase Score

```kotlin
@Composable
fun ContactPurchaseScore(score: Int) {
    // Scale: 1 (Potential to explore) → 3 (High potential)
    val gaugeType = when (score) {
        3 -> GaugeTypeThree.VeryHigh
        2 -> GaugeTypeThree.Low
        1 -> GaugeTypeThree.VeryLow
        else -> null
    }

    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        Text("Purchase Potential:")
        SegmentedGaugeShort(type = gaugeType)
    }
}
```

### Pricing Position Quality

```kotlin
@Composable
fun PricingPositionIndicator(position: PricingPosition) {
    // Scale: 1 (Above market price) → 5 (Great deal)
    val gaugeType = when (position) {
        PricingPosition.GREAT_DEAL -> GaugeTypeFive.VeryHigh
        PricingPosition.GOOD_DEAL -> GaugeTypeFive.High
        PricingPosition.FAIR -> GaugeTypeFive.Medium
        PricingPosition.OVERPRICED -> GaugeTypeFive.Low
        PricingPosition.WAY_OVERPRICED -> GaugeTypeFive.VeryLow
    }

    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        Text("Price Position:")
        SegmentedGauge(type = gaugeType)
    }
}
```

### Import Listing Occupancy Rate

```kotlin
@Composable
fun OccupancyRateIndicator(rate: OccupancyRate) {
    // Scale: 1 (Underuse) → 3 (Overuse)
    val gaugeType = when (rate) {
        OccupancyRate.OVERUSE -> GaugeTypeThree.VeryHigh
        OccupancyRate.OPTIMAL -> GaugeTypeThree.Low
        OccupancyRate.UNDERUSE -> GaugeTypeThree.VeryLow
    }

    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        Text("Occupancy:")
        SegmentedGaugeShort(type = gaugeType)
    }
}
```

### Product Comparison

```kotlin
@Composable
fun ProductComparison(products: List<Product>) {
    Column(verticalArrangement = Arrangement.spacedBy(16.dp)) {
        products.forEach { product ->
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                Text(product.name, modifier = Modifier.weight(1f))
                SegmentedGauge(
                    size = GaugeSize.Small,
                    type = product.qualityGaugeType
                )
            }
        }
    }
}
```

## Accessibility

The SegmentedGauge component provides semantic color coding that conveys meaning through visual design. Always pair color with a textual label to ensure readability and accessibility. The component can receive focus for screen reader access.

- **Color Consistency**: Maintain consistent color meanings (green = positive, yellow = warning, red = negative)
- **Text Labels**: Always pair color with textual labels
- **Focus Support**: Component can receive focus for screen reader accessibility
- **Color Contrast**: Ensure sufficient contrast ratios are maintained

```kotlin
@Composable
fun AccessibleQualityIndicator(quality: QualityLevel) {
    val gaugeType = when (quality) {
        QualityLevel.VERY_GOOD -> GaugeTypeFive.VeryHigh
        QualityLevel.GOOD -> GaugeTypeFive.High
        QualityLevel.NEUTRAL -> GaugeTypeFive.Medium
        QualityLevel.LOW -> GaugeTypeFive.Low
        QualityLevel.VERY_LOW -> GaugeTypeFive.VeryLow
    }

    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        Text("${quality.displayName} quality")
        SegmentedGauge(type = gaugeType)
    }
}
```

## Implementation Notes

- The indicator animates smoothly when the gauge type changes
- Null type displays segments without an indicator (neutral state)
- Custom colors override the default semantic colors
- The component uses alpha transparency for smooth indicator transitions
- Border styling changes based on whether an indicator is present
- Designed for read-only data visualization, not user interaction
- Non-clickable and does not change on hover
- Full-rounded shapes for both segments and markers
