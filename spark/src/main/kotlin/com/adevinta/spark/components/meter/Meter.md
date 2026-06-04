# Package com.adevinta.spark.components.meter

[Meter](https://www.figma.com/design/0QchRdipAVuvVoDfTjLrgQ/Spark-Component-Specs?node-id=62526-4082)
A circular progress indicator displaying a percentage value with configurable content inside the ring.

|       | Circular | CircularSmall |
|-------|--------|--------|
| Light | ![](../../images/com.adevinta.spark.components.meter_MeterDocumentationScreenshots_circular.png) | ![](../../images/com.adevinta.spark.components.meter_MeterDocumentationScreenshots_circularSmall.png) |

## Meter.Circular

A circular meter supporting Medium, Large, and XLarge sizes with content inside the ring via a scoped DSL.

```kotlin
Meter.Circular(
    progress = 0.7f,
    intent = MeterIntent.Support,
    size = MeterSize.Large,
) {
    valueLabel("70%", "Label")
}
```

### Content types

- `value(text)` - formatted value string centred in the ring
- `valueLabel(text, label)` - value with a secondary label below
- `icon(icon, contentDescription, label)` - a SparkIcon with optional label
- `image(label) { ... }` - custom image composable with optional label
- `custom { ... }` - fully custom content

### Sizes

| Size | Diameter | Stroke | Icon/Image |
|------|----------|--------|------------|
| Medium | 64dp | 5dp | 24dp |
| Large (default) | 96dp | 8dp | 32dp |
| XLarge | 128dp | 10dp | 40dp |

### Intents

Support (default), Main, Accent, Success, Alert, Danger, Info, Neutral.

## Meter.CircularSmall

A compact 24dp circular meter that renders the percentage value outside the ring.

```kotlin
Meter.CircularSmall(
    progress = 0.7f,
    intent = MeterIntent.Support,
)
```