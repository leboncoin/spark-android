# Package com.adevinta.spark.components.progress.tracker

[ProgressTracker](https://spark.adevinta.com/1186e1705/p/549af2-progress-tracker/b/207b6b) is a
visual navigation element typically used to display progress or guide user through a multi-step
process.

It displays a linear progress as steps, accepts between 2 and 6 steps maximum.
There are two possible orientations: horizontal with `ProgressTrackerRow` or vertical with
`ProgressTrackerColumn`.
Each step displays its label via stepLabel. Selection and click are handled.
A step indicator is displayed via StepIndicator for each step. Its state is updated according to
selection.

|            | Light                                                                                                                | Dark                                                                                                                |
|------------|----------------------------------------------------------------------------------------------------------------------|---------------------------------------------------------------------------------------------------------------------|
| Horizontal | ![](../../images/com.adevinta.spark.progress.tracker_ProgressTrackerDocScreenshot_progressRowShowcase_light.png)    | ![](../../images/com.adevinta.spark.progress.tracker_ProgressTrackerDocScreenshot_progressRowShowcase_dark.png)    |
| Vertical   | ![](../../images/com.adevinta.spark.progress.tracker_ProgressTrackerDocScreenshot_progressColumnShowcase_light.png) | ![](../../images/com.adevinta.spark.progress.tracker_ProgressTrackerDocScreenshot_progressColumnShowcase_dark.png) |

The minimal usage of the component is the step list to be displayed.

```kotlin

val items = persistentListOf(
    ProgressStep("Lorem ipsume", true),
    ProgressStep("Lorem ipsume dolar sit amet", true),
    ProgressStep("Lorem ipsume", false),
)
ProgressTrackerRow(items = items)
```

### Sizes

There are 3 possible sizes: Large(Default), medium and small.
Only the large one should be interactive, if it's not the case please ask your designer to consider
using the Large size as recommended in the design specs.

![](../../images/com.adevinta.spark.progress.tracker_ProgressTrackerDocScreenshot_progressSizesShowcase.png)
