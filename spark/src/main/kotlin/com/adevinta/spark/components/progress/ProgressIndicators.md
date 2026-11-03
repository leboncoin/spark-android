# Package com.adevinta.spark.components.progress

[Progress Indicators](https://m3.material.io/components/progress-indicators/overview) express an unspecified wait time or display the duration of a process.

Spark provides three distinct composables for progress indication:

### CircularProgressIndicator

A circular progress indicator that displays the exact progress of a process.

```kotlin
var progress by remember { mutableFloatStateOf(0.5f) }

CircularProgressIndicator(
    progress = { progress }
)
```

### LinearProgressIndicator

A linear progress indicator that displays the exact progress of a process along a horizontal line.

```kotlin
var progress by remember { mutableFloatStateOf(0.5f) }

LinearProgressIndicator(
    progress = { progress },
    modifier = Modifier.fillMaxWidth(),
)
```

### LinearProgressIndicatorIndeterminate

A linear progress indicator that expresses an unspecified wait time. It continuously animates along a horizontal line.

```kotlin
LinearProgressIndicatorIndeterminate(
    modifier = Modifier.fillMaxWidth(),
)
```
