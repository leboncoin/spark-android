# Package com.adevinta.spark.components.progressbar

[Proressbar](https://spark.adevinta.com/1186e1705/p/89544a-progress-bar/b/2873c8)
The progress bar component is used to display the length and your progression inside the process or
to express a waiting time.
This waiting time can be either determinate or indeterminate.

### Variants

There are multiple variants available:

- Determinate progress bar
- Indeterminate progress bar

### Border

Progressbar come in 2 border shapes:

- Square
- Rounded (default)

### Intents

Icon Buttons support all intents:

- Main
- Support
- Accent
- Basic
- Success
- Alert
- Danger
- Info
- Neutral

#### Progressbar

```kotlin
fun Progressbar(
    intent: ProgressbarIntent,
    isRounded: Boolean,
    progress: Float
)
```

#### ProgressbarIntermediate

```kotlin
fun ProgressbarIndeterminate(
    intent: ProgressbarIntent,
    isRounded: Boolean
)
```

