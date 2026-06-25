# Package com.adevinta.spark.components.placeholder

Placeholders display skeleton UI whilst content is loading. They occupy the same space as the real
content and cross-fade out once data is ready, reducing perceived wait time.

Use placeholders when:

- Data loads asynchronously and the layout shape is known in advance
- Multiple items load simultaneously and a consistent loading state is needed

### Modifiers

Placeholders are applied as `Modifier` extensions rather than standalone composables. Each variant
targets a specific content type.

#### `Modifier.placeholder`

General-purpose skeleton for any layout element. Shape defaults to `SparkTheme.shapes.small`.
Animation is a **fade** (pulse in/out).

```kotlin
Box(
    modifier = Modifier
        .fillMaxWidth()
        .height(48.dp)
        .placeholder(visible = isLoading),
)
```

To override the shape, apply `Modifier.clip` before this modifier:

```kotlin
Box(
    modifier = Modifier
        .size(40.dp)
        .clip(CircleShape)
        .placeholder(visible = isLoading),
)
```

#### `Modifier.textPlaceholder`

Skeleton for text content. Uses `SparkTheme.shapes.full` (pill shape) to mirror the rounded
profile of text runs. Animation is a **fade**.

```kotlin
Text(
    text = username,
    modifier = Modifier.textPlaceholder(visible = isLoading),
)
```

Apply it to every `Text` composable independently so each line produces its own skeleton at the
correct width.

#### `Modifier.illustrationPlaceholder`

Skeleton for images and illustrations. Animation is a **shimmer** (radial gradient sweeping from
top-start to bottom-end). Requires an explicit `shape` parameter.

```kotlin
AsyncImage(
    model = url,
    contentDescription = null,
    modifier = Modifier
        .size(128.dp)
        .illustrationPlaceholder(
            visible = isLoading,
            shape = SparkTheme.shapes.extraLarge,
        ),
)
```

### Animation modes

| Mode | Modifier | Behaviour |
|------|----------|-----------|
| Fade | `placeholder`, `textPlaceholder` | Colour pulses in and out (600 ms, 200 ms delay, reverses) |
| Shimmer | `illustrationPlaceholder` | Radial gradient sweeps across the surface (1700 ms, 200 ms delay, restarts) |

All three modifiers apply a cross-fade transition between the skeleton and the real content when
`visible` flips to `false`.

### Full example

```kotlin
@Composable
fun ListingCard(listing: Listing?, isLoading: Boolean) {
    Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
        AsyncImage(
            model = listing?.imageUrl,
            contentDescription = null,
            modifier = Modifier
                .fillMaxWidth()
                .height(200.dp)
                .illustrationPlaceholder(
                    visible = isLoading,
                    shape = SparkTheme.shapes.medium,
                ),
        )
        Text(
            text = listing?.title.orEmpty(),
            modifier = Modifier.textPlaceholder(visible = isLoading),
        )
        Text(
            text = listing?.price.orEmpty(),
            modifier = Modifier.textPlaceholder(visible = isLoading),
        )
    }
}
```

### Customising colours

`PlaceholderDefaults` exposes `@Composable` functions to produce theme-aware colours for custom
highlight implementations:

| Function | Used by | Default alpha |
|----------|---------|---------------|
| `PlaceholderDefaults.color()` | Base skeleton fill | 0.25f |
| `PlaceholderDefaults.fadeHighlightColor()` | `PlaceholderHighlight.fade()` | 0.3f |
| `PlaceholderDefaults.shimmerHighlightColor()` | `PlaceholderHighlight.shimmer()` | 0.3f |

All three derive from `SparkTheme.colors.surface` and accept `backgroundColor`, `contentColor`,
and `alpha` overrides.
