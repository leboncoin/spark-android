# Package com.adevinta.spark.components.image

The image package provides three composables for displaying visual content: `Image` for remote/async
images with managed loading states, `Illustration` for local static assets, and `UserAvatar` for
circular profile pictures.

## Image

`Image` loads remote or local content asynchronously via Coil and handles all loading states
automatically. Unlike the standard Compose `Image`, it requires bounded dimensions and shows
built-in placeholder states.

```kotlin
Image(
    model = "https://example.com/photo.jpg",
    contentDescription = "Product photo",
    modifier = Modifier.size(200.dp),
)
```

The component **requires** bounded width and height. Wrap in a `size`, `fillMaxWidth`, or other
size-constraining modifier; an unbounded `Image` triggers an exception via the Spark exception
handler.

### Loading states

Three states are rendered automatically. Each slot accepts a custom composable:

| State | Default behaviour | Parameter |
|---|---|---|
| Empty (null or blank model) | Neutral icon on `neutralContainer` background | `emptyIcon` |
| Loading | Shimmer placeholder animation | `loadingPlaceholder` |
| Error (URL failed to load) | Warning icon on `errorContainer` background | `errorIcon` |

```kotlin
Image(
    model = imageUrl,
    contentDescription = "Ad photo",
    modifier = Modifier
        .size(120.dp)
        .clip(SparkTheme.shapes.medium),
    emptyIcon = {
        // Custom empty state
        Icon(sparkIcon = SparkIcons.ImageFill, contentDescription = null)
    },
    errorIcon = {
        // Custom error state
        Icon(sparkIcon = SparkIcons.WarningFill, contentDescription = null)
    },
)
```

The `emptyIcon` slot is also shown when loading fails with a null, blank, or
`NullRequestData` model, distinguishing "no content" from a genuine network failure.

### Observing state changes

Use `onState` to react to loading lifecycle events:

```kotlin
Image(
    model = url,
    contentDescription = "Photo",
    modifier = Modifier.size(100.dp),
    onState = { state ->
        when (state) {
            is State.Success -> analyticsTracker.imageLoaded()
            is State.Error -> analyticsTracker.imageFailedToLoad()
            else -> Unit
        }
    },
)
```

### Content scale and alignment

`contentScale` and `alignment` mirror the standard Compose parameters and default to
`ContentScale.Fit` / `Alignment.Center`.

```kotlin
Image(
    model = url,
    contentDescription = "Banner",
    modifier = Modifier
        .fillMaxWidth()
        .height(200.dp),
    contentScale = ContentScale.Crop,
    alignment = Alignment.TopCenter,
)
```

### Applying shape

Apply shape via `Modifier.clip` before passing to the composable:

```kotlin
Image(
    model = url,
    contentDescription = "Thumbnail",
    modifier = Modifier
        .size(80.dp)
        .clip(SparkTheme.shapes.large),
)
```

---

## Illustration

`Illustration` renders static local assets - bitmaps, vectors, drawables, or `SparkIcon`s - without
loading states or network access.

```kotlin
// From a drawable resource
Illustration(
    drawableRes = R.drawable.il_onboarding,
    contentDescription = "Onboarding graphic",
    modifier = Modifier.size(240.dp),
)

// From a SparkIcon
Illustration(
    sparkIcon = SparkIcons.Store,
    contentDescription = "Store",
    modifier = Modifier.size(100.dp),
)

// From a Painter
Illustration(
    painter = rememberVectorPainter(MyVector),
    contentDescription = null, // decorative
)
```

Use `Illustration` for design-system illustrations and marketing graphics where content is
bundled with the app. Use `Image` for any content loaded at runtime.

---

## UserAvatar

`UserAvatar` displays a circular profile picture. It uses `Image` internally and falls back to
a profile or pro icon when no model is provided.

```kotlin
UserAvatar(
    model = user.avatarUrl,
    style = UserAvatarStyle.MD,
    isPro = user.isProfessional,
    isOnline = user.isOnline,
)
```

### Sizes

`UserAvatarStyle` has seven sizes. The default is `SM`. The image shows `SM`, `MD`, and `XL`.

![](../../images/com.adevinta.spark.image_UserAvatarDocumentationScreenshots_sizes.png)

| Style | Image size | Badge size | Stroke |
|---|---|---|---|
| `UserAvatarStyle.XS` | 24dp | 2dp | 1dp |
| `UserAvatarStyle.SM` | 32dp | 8dp | 1dp |
| `UserAvatarStyle.MD` | 40dp | 8dp | 1dp |
| `UserAvatarStyle.LG` | 56dp | 16dp | 2dp |
| `UserAvatarStyle.XL` | 64dp | 16dp | 2dp |
| `UserAvatarStyle.XXL` | 96dp | 16dp | 2dp |
| `UserAvatarStyle.XXXL` | 128dp | 16dp | 2dp |

### Online indicator

Set `isOnline = true` to display a green dot badge at the bottom-right of the avatar. The badge
scales with the chosen `UserAvatarStyle`. The left avatar is offline, the right avatar is online.

![](../../images/com.adevinta.spark.image_UserAvatarDocumentationScreenshots_onlineIndicator.png)

### Pro badge

Set `isPro = true` to swap the fallback icon from a generic profile silhouette to a pro indicator.
The pro icon is only visible when no image is loaded (empty or error state). The left avatar is a
regular user, the right avatar is a pro.

![](../../images/com.adevinta.spark.image_UserAvatarDocumentationScreenshots_types.png)

```kotlin
UserAvatar(
    model = null, // shows fallback icon
    style = UserAvatarStyle.XL,
    isPro = true,
    color = SparkTheme.colors.mainContainer,
)
```

The `color` parameter tints the fallback icon's background surface. Leave as `Color.Unspecified`
to use the default neutral colour.
