# Package com.adevinta.spark.components.stepper

Steppers let users increment and decrement a numeric value within a defined range using
decrease/increase buttons on either side of the current value.

## Stepper

The minimal variant for inline use where a label and helper text are not needed.

```kotlin
var quantity by remember { mutableStateOf(1) }

Stepper(
    value = quantity,
    onValueChange = { quantity = it },
    range = 1..99,
    step = 1,
)
```

### Parameters

| Parameter | Default | Description |
|---|---|---|
| `value` | - | Current displayed value |
| `onValueChange` | - | Called after each increment or decrement |
| `range` | `0..10` | Accepted value bounds; buttons disable at the limits |
| `step` | `1` | Amount added or subtracted per button press |
| `suffix` | `""` | String appended after the value, e.g. `" kg"` |
| `enabled` | `true` | Disables interaction and applies disabled styling |
| `status` | `null` | Validation state; tints the outline (`Error`, `Alert`, `Success`) |
| `flexible` | `false` | When `true`, fills maximum available width |

```kotlin
Stepper(
    value = weight,
    onValueChange = { weight = it },
    range = 0..500,
    step = 5,
    suffix = " kg",
    status = if (weight == 0) FormFieldStatus.Error else null,
)
```

Both `range.first` and `range.last` must be multiples of `step`, otherwise `stepperSemantics`
throws `IllegalArgumentException`.

---

## StepperForm

`StepperForm` wraps `Stepper` with a label, optional helper text, and status message - matching
the layout of other form fields.

```kotlin
var guests by remember { mutableStateOf(1) }

StepperForm(
    value = guests,
    onValueChange = { guests = it },
    label = "Number of guests",
    helper = "Maximum 8 per booking",
    range = 1..8,
    required = true,
    status = if (guests > 8) FormFieldStatus.Error else null,
    statusMessage = if (guests > 8) "Exceeds maximum capacity" else null,
)
```

### Additional parameters

| Parameter | Description |
|---|---|
| `label` | Field label displayed above the stepper |
| `helper` | Hint text shown below when `status` is null |
| `required` | Appends `*` to the label and reads it as "mandatory field" |
| `statusMessage` | Replaces `helper` when `status` is non-null |

`StepperForm` sets `allowSemantics = false` on the inner `Stepper` and applies
`stepperSemantics` itself at the `Column` level, so TalkBack announces the label, mandatory
indicator, and value as a single node.

---

## Accessibility: `stepperSemantics`

`Modifier.stepperSemantics` is a public extension that configures a Stepper-containing layout
to behave like a slider under TalkBack. Apply it when composing a `Stepper` inside a custom
layout (e.g. a `Row` with a label).

```kotlin
var value by remember { mutableStateOf(50) }
val label = "Volume"

Row(
    modifier = Modifier
        .fillMaxWidth()
        .semantics { text = AnnotatedString(label) }
        .stepperSemantics(
            value = value,
            onValueChange = { value = it },
            range = 0..100,
            step = 1,
            suffix = "%",
            enabled = true,
        ),
) {
    Text(
        text = label,
        modifier = Modifier.invisibleSemantic(), // avoids duplicate announcement
    )
    Stepper(
        value = value,
        onValueChange = { value = it },
        range = 0..100,
        allowSemantics = false, // required when parent applies stepperSemantics
    )
}
```

The modifier:

- Exposes `setProgress` so TalkBack volume-key gestures and swipe-up/down gestures change the value
- Overrides the percentage description with the raw value plus suffix
- Announces "disabled" when `enabled = false`
- Handles Shift+Up / Shift+Down keyboard events for hardware keyboard navigation

Pass `allowSemantics = false` to the inner `Stepper` whenever `stepperSemantics` is applied on
an ancestor, otherwise accessibility nodes are duplicated.

### Validation constraints

`stepperSemantics` enforces:

- `step > 0`
- `range.first % step == 0`
- `range.last % step == 0`

These are also checked by `StepperForm` and `Stepper` at composition time.
