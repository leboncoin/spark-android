# Package com.adevinta.spark.components.image

The image package provides three composables for displaying visual content: `Image` for remote/async
images with managed loading states, `Illustration` for local static assets, and `Avatar` for
profile and company pictures.

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

## Avatar

`Avatar.User` and `Avatar.Company` show a profile picture or a company logo. Each uses `Image`
internally and falls back to a placeholder when no image loads.

> [!IMPORTANT]
> `UserAvatar` is deprecated. Use `Avatar.User` for a person or `Avatar.Company` for a company.

```kotlin
Avatar.User(
    username = user.name,
    model = user.avatarUrl,
    style = AvatarSize.MD,
    addon = { onlineIndicator() },
)

Avatar.Company(
    model = company.logoUrl,
    style = AvatarSize.MD,
)
```

### Sizes

`AvatarSize` has seven values. The default is `SM`. The image shows `SM`, `MD`, and `XL`.

![](../../images/com.adevinta.spark.image_AvatarDocumentationScreenshots_sizes.png)

| Size | Image size | Badge size | Stroke |
|---|---|---|---|
| `AvatarSize.XS` | 24dp | 2dp | 1dp |
| `AvatarSize.SM` | 32dp | 8dp | 1dp |
| `AvatarSize.MD` | 40dp | 8dp | 1dp |
| `AvatarSize.LG` | 56dp | 16dp | 2dp |
| `AvatarSize.XL` | 64dp | 16dp | 2dp |
| `AvatarSize.XXL` | 96dp | 16dp | 2dp |
| `AvatarSize.XXXL` | 128dp | 16dp | 2dp |

### User and company

`Avatar.User` is always a circle. When no image loads, it shows the first non-whitespace character
of `username`, in upper case. A blank `username` shows an empty neutral circle; it has no icon
placeholder.

`Avatar.Company` is always a rounded square. When no image loads, it shows a building icon on a
neutral container. The left avatar is `Avatar.User`, the right avatar is `Avatar.Company`.

![](../../images/com.adevinta.spark.image_AvatarDocumentationScreenshots_variants.png)

### Letter placeholder

The left avatar has a blank `username`; the right avatar has a `username` of "Spark".

![](../../images/com.adevinta.spark.image_AvatarDocumentationScreenshots_letter.png)

### Addon

Pass `addon` to show an element on the avatar. Call `AvatarAddonScope.onlineIndicator()` for the
standard presence dot, `AvatarAddonScope.iconButton(...)` for an action button, or
`AvatarAddonScope.custom { ... }` for other content. The default icon of `iconButton` is
`LeboncoinIcons.Plus`, so the call below shows an add button.

```kotlin
Avatar.User(
    username = user.name,
    model = user.avatarUrl,
    style = AvatarSize.XL,
    addon = { iconButton(onClick = onAddPhoto) },
)
```

The addon position depends on the variant. `Avatar.User` centres the addon on the circle rim,
where it can overflow the avatar bounds. `Avatar.Company` places the addon inside the bottom-end
corner, where it always stays inside the avatar bounds. The left avatar in each image is
`Avatar.User`, the right avatar is `Avatar.Company`.

![](../../images/com.adevinta.spark.image_AvatarDocumentationScreenshots_onlineIndicator.png)

![](../../images/com.adevinta.spark.image_AvatarDocumentationScreenshots_addonButton.png)
