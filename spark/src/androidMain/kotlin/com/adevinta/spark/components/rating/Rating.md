# Package com.adevinta.spark.components.rating

Rating components display or collect a 1-5 star score. Use display variants to show existing scores
and `RatingInput` to let users submit their own.

## Variants

| Variant | Purpose | Accepts fractional values |
|---|---|---|
| `RatingDisplay` | Raw star row, no label | Yes (Float 0–5) |
| `RatingFull` | Stars with numeric value, label, and review count | Yes (Float 0–5) |
| `RatingSimple` | Single star + numeric value + optional review count | Yes (Float 1–5) |
| `RatingSimpleLarge` | Large single-star display, no review count | Yes (Float 1–5) |
| `RatingInput` | Interactive 5-star picker | No (Int 0–5) |
| `RatingStar` | Atomic star icon (full, half, empty) | Via `RatingStarState` |

---

### RatingDisplay

A row of five stars reflecting a fractional value. Values below `0.5` render nothing.

```kotlin
RatingDisplay(value = 3.5f)
```

Pass `size` to override the default 12dp star size:

```kotlin
RatingDisplay(
    value = 4.2f,
    size = RatingDefault.StarSize, // 16dp
)
```

---

### RatingFull

Displays **`3,4 ★★★☆☆ Label (5 avis)`** — numeric value, stars, optional label, and optional
review count. Requires a value in the range `[1..5]`; values outside this range render nothing.

```kotlin
RatingFull(
    value = 3.6f,
    commentCount = 42,
)
```

Pass `label` to append a category name after the stars:

```kotlin
RatingFull(
    value = 4.2f,
    label = "Communication",
    commentCount = 5,
)
```

Pass `locale = null` to hide the numeric prefix:

```kotlin
RatingFull(
    value = 3.6f,
    commentCount = 23,
    locale = null,
)
```

---

### RatingSimple

A compact single-star summary — **`★ 3,4 (5)`**. Annotated `@ExperimentalSparkApi`.

```kotlin
RatingSimple(
    value = 4.5f,
    commentCount = 12,
)
```

Control which side the numeric label appears on with `labelSide`:

```kotlin
RatingSimple(
    value = 4.5f,
    labelSide = RatingLabelSide.Start, // "4,5 ★"
)
```

---

### RatingSimpleLarge

An oversized version of `RatingSimple` — renders the value in `display3` typography with a `/5`
suffix, no review count. Annotated `@ExperimentalSparkApi`.

```kotlin
RatingSimpleLarge(
    value = 4.5f,
    locale = Locale.US,
)
```

---

### RatingInput

An interactive 5-star picker. Accepts tap and horizontal drag gestures, triggers haptic feedback
on drag, and exposes semantics as a slider for accessibility services (TalkBack, Switch Access).
Keyboard navigation uses `Shift + Arrow` keys to increment or decrement.

```kotlin
var rating by remember { mutableIntStateOf(0) }

RatingInput(
    value = rating,
    onRatingChanged = { rating = it },
)
```

Disable the picker while a submission is in flight:

```kotlin
RatingInput(
    value = rating,
    onRatingChanged = { rating = it },
    enabled = false,
)
```

Supply a custom accessibility description when the default "N stars" wording does not fit the
context:

```kotlin
RatingInput(
    value = rating,
    onRatingChanged = { rating = it },
    stateDescription = { value -> "$value out of 5" },
)
```

Set `allowSemantics = false` when embedding `RatingInput` inside a parent that declares its own
semantics, to prevent duplicate TalkBack announcements:

```kotlin
RatingInput(
    value = rating,
    onRatingChanged = { rating = it },
    allowSemantics = false,
)
```

---

### RatingStar

The atomic star icon. Use it when none of the composite components match your layout needs.

`RatingStarState` accepts a `Boolean`, `Int` (0 or 1), `Float` (0.0–1.0), or `Double`:

```kotlin
RatingStar(state = RatingStarState.Full)
RatingStar(state = RatingStarState.Half)
RatingStar(state = RatingStarState.Empty)

// Derived from a Float — 0.0–0.25 → Empty, 0.25–0.75 → Half, 0.75–1.0 → Full
RatingStar(state = RatingStarState(0.6f))
```

Pass `enabled = false` to render the star at reduced opacity:

```kotlin
RatingStar(
    state = RatingStarState.Full,
    enabled = false,
    size = RatingDefault.StarSize,
)
```

---

## Sizing

| Constant | Value | Used by |
|---|---|---|
| `RatingDefault.SmallStarSize` | 12dp | `RatingDisplay`, `RatingSimple` |
| `RatingDefault.StarSize` | 16dp | `RatingFull`, `RatingStar` |
| `RatingDefaults.StarSize` | 40dp | `RatingInput` touch target interior |

---

## Accessibility

- `RatingFull` and `RatingSimple` merge all descendants into a single node with a
  locale-aware content description (`"3.6 stars"` or `"3.6 stars, 42 reviews"`).
- `RatingInput` exposes a `ProgressBarRangeInfo` so TalkBack announces it as a slider. Swipe up/down
  adjusts the value in increments of one. `Shift + Arrow` provides the same control from a keyboard.
- Individual `RatingStar` instances carry no content description; they rely on a parent node to
  supply context.
