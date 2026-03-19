# Package com.adevinta.spark.components.icons

The [Icon](https://spark.adevinta.com/1186e1705/p/11373f-icon/b/80bf01) component display any Icon
coming from either of these sources:

- [SparkIcon](https://github.com/leboncoin/spark-android/blob/main/spark-icons/src/main/kotlin/com/adevinta/spark/icons/SparkIcon.kt)
- [ImageVector](https://developer.android.com/reference/kotlin/androidx/compose/ui/graphics/vector/ImageVector)
- [ImageBitmap](https://developer.android.com/reference/kotlin/androidx/compose/ui/graphics/ImageBitmap)
- [Painter](https://developer.android.com/reference/kotlin/androidx/compose/ui/graphics/painter/Painter)

It behaves like the Compose Foundation Icon component but it also accepts size parameter that have
the following values (`IconSize`):

- Small (16.dp)
- Medium (24.dp) - default size
- Large (32.dp)
- ExtraLarge (40.dp)

Use `IconIntent` to pass one of the accepted intents:
- Support
- Accent
- Main
- Surface
- Success
- Alert
- Danger
- Neutral
- Current
- Unspecified

```kotlin
Icon(
    sparkIcon = SparkIcons.Check,
    tint = IconIntent.Main,
    contentDescription = "Done",
    size = IconSize.Large,
)
```
