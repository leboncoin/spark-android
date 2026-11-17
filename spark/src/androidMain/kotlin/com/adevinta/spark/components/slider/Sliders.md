# Package com.adevinta.spark.components.slider

[Sliders](https://spark.adevinta.com/1186e1705/p/25cceb-slider/b/03f6fc)
A slider is an interactive component that allows users to set values by moving a handle within a
defined range.

### Variants
2 Variants:

#### Slider
Provides the slider with a minimum and maximum value.

```kotlin
Slider(
    value = 0.5f,
    intent = SliderIntent.Success,
    onValueChange = {},
)
```

|          | Slider                                                                       |
|----------|------------------------------------------------------------------------------|
| Variants | ![](../../images/com.adevinta.spark.slider_SliderScreenshot_testSliders.png) |

#### Range Slider
Provides the slider with a set of two values (range).

```kotlin
RangeSlider(
    value = 0.1f..0.5f,
    intent = SliderIntent.Success,
    onValueChange = {},
)
```

|          | Range Slider                                                                      |
|----------|-----------------------------------------------------------------------------------|
| Variants | ![](../../images/com.adevinta.spark.slider_SliderScreenshot_testRangeSliders.png) |
