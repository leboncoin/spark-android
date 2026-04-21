# Package com.adevinta.spark.components.stepper

Steppers let users increment and decrement a numeric value within a defined range using
decrease/increase buttons on either side of the current value.

All stepper variants live inside `object Stepper` and come in two flavours:
**Nudger** (read-only display) and **Input** (editable text field).

---

## Stepper.Nudger

The Nudger variant displays the value between decrease/increase buttons. The value is
read-only; users change it via the buttons. Each digit animates independently on change
(odometer effect). Long-pressing a button repeats the action with acceleration.

```kotlin
var quantity by rememberSaveable { mutableStateOf<Int?>(0) }

Stepper.Nudger(
    value = quantity,
    onValueChange = { quantity = it },
    range = 1..99,
    step = 1,
)
```

### Nullable value

Pass `null` for an empty initial state. Pressing either button snaps to `range.first`.

```kotlin
var quantity by rememberSaveable { mutableStateOf<Int?>(null) }

Stepper.Nudger(
    value = quantity,
    onValueChange = { quantity = it },
    range = 0..10,
)
```

### State holder

`StepperState` bundles value, range, and step into a single saveable holder.

```kotlin
val state = rememberStepperState(initialValue = 5, range = 0..100, step = 5)

Stepper.Nudger(state = state)
```

### Parameters

| Parameter | Default | Description |
|---|---|---|
| `value` | - | Current displayed value, or `null` for empty |
| `onValueChange` | - | Called after each increment or decrement |
| `range` | `0..10` | Accepted value bounds; buttons disable at the limits |
| `step` | `1` | Amount added or subtracted per button press |
| `suffix` | `""` | String appended after the value, e.g. `" kg"` |
| `enabled` | `true` | Disables interaction and applies disabled styling |
| `flexible` | `false` | When `true`, fills maximum available width |

---

## Stepper.NudgerForm

Wraps `Stepper.Nudger` with a label, optional helper text, and status message.

```kotlin
var guests by rememberSaveable { mutableStateOf<Int?>(1) }

Stepper.NudgerForm(
    value = guests,
    onValueChange = { guests = it },
    label = "Number of guests",
    helper = "Maximum 8 per booking",
    range = 1..8,
    required = true,
    status = FormFieldStatus.Error,
    statusMessage = "Exceeds maximum capacity",
)
```

### State holder

```kotlin
val state = rememberStepperState(initialValue = 1, range = 1..8, step = 1)

Stepper.NudgerForm(
    state = state,
    label = "Number of guests",
    helper = "Maximum 8 per booking",
)
```

### Additional parameters

| Parameter | Description |
|---|---|
| `label` | Field label displayed above the stepper |
| `helper` | Hint text shown below when `status` is null |
| `required` | Appends `*` to the label and reads it as "mandatory field" |
| `status` | Validation state; tints the helper text |
| `statusMessage` | Replaces `helper` when `status` is non-null |

---

## Stepper.Input

The Input variant places an editable text field between the buttons. Users can type a
value directly or use the buttons. Non-digit characters are rejected, and the value is
clamped to the range on blur. An empty field has `value == null`; the field then shows
a `-` placeholder. Long-pressing a button repeats with acceleration.

```kotlin
var quantity by rememberSaveable { mutableStateOf<Int?>(3) }

Stepper.Input(
    value = quantity,
    onValueChange = { quantity = it },
    range = 0..100,
    step = 1,
)
```

### State holder

`StepperInputState` wraps a `TextFieldState` and exposes a parsed `value: Int?`.

```kotlin
val state = rememberStepperInputState(initialValue = 5)

Stepper.Input(
    state = state,
    range = 0..100,
    step = 1,
)
Text(text = "Current value: ${state.value ?: "empty"}")
```

### Parameters

| Parameter | Default | Description |
|---|---|---|
| `value` | - | Current value, or `null` for empty |
| `onValueChange` | - | Called on button press or blur commit; receives `null` when the field is cleared |
| `range` | `0..10` | Accepted value bounds |
| `step` | `1` | Increment/decrement amount |
| `suffix` | `""` | Shown after the value inside the field, e.g. `kg` |
| `enabled` | `true` | Disables interaction |
| `status` | `null` | Validation state; tints the inner text field border |
| `flexible` | `false` | When `true`, fills maximum available width |

---

## Stepper.InputForm

Wraps `Stepper.Input` with a label, helper, and status message.

```kotlin
var weight by rememberSaveable { mutableStateOf<Int?>(70) }

Stepper.InputForm(
    value = weight,
    onValueChange = { weight = it },
    label = "Weight",
    helper = "Enter weight in kg",
    range = 0..500,
    step = 1,
    required = true,
    status = if (weight == null) FormFieldStatus.Error else null,
    statusMessage = if (weight == null) "Required" else null,
)
```

### State holder

```kotlin
val state = rememberStepperInputState(initialValue = 3)

Stepper.InputForm(
    state = state,
    label = "Quantity",
    helper = "Enter a value between 0 and 100",
    range = 0..100,
    required = true,
)
```

---

## Accessibility

### `stepperSemantics` (Nudger)

`Modifier.stepperSemantics` configures a Nudger-containing layout to behave like a slider
under TalkBack. Apply it when composing `Stepper.Nudger` inside a custom layout.

```kotlin
var value by rememberSaveable { mutableStateOf<Int?>(50) }
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
    Text(text = label, modifier = Modifier.invisibleSemantic())
    Stepper.Nudger(
        value = value,
        onValueChange = { value = it },
        range = 0..100,
        allowSemantics = false,
    )
}
```

The modifier exposes `setProgress`, overrides the percentage description, announces
"disabled" when `enabled = false`, and handles Shift+Up/Down keyboard events.

### Input variant

`Stepper.Input` exposes the editable text field as the single accessible control. TalkBack
reads and edits the value through the field; the `+`/`-` buttons are visual only and hidden
from accessibility, so they add no extra nodes to navigate.

### Validation constraints

Both modifiers enforce:

- `step > 0`
- `range.first % step == 0`
- `range.last % step == 0`

---

## Migration from deprecated API

> [!WARNING]
> `Stepper()` and `StepperForm()` are deprecated. Migrate to the new API below.

| Old | New |
|---|---|
| `Stepper(value, onValueChange, ...)` | `Stepper.Nudger(value, onValueChange, ...)` |
| `StepperForm(value, onValueChange, label, helper, ...)` | `Stepper.NudgerForm(value, onValueChange, label, helper, ...)` |

The deprecated functions still compile with a `WARNING` and internally delegate to the new
API. Use the IDE quick-fix (`ReplaceWith`) to migrate call sites.

The `status` parameter of the old `Stepper()` is silently dropped; `Stepper.Nudger` has
no `status` param. Use `Stepper.NudgerForm` if you need to show a validation status.
