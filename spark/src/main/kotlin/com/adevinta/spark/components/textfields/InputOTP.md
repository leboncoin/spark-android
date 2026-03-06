# Package com.adevinta.spark.components.textfields

## InputOTP

[InputOTP](https://spark.adevinta.com/1186e1705/p/773c60-input--text-field/b/0658e2) is a specialized component for entering one-time passwords (OTP) and verification codes. It displays individual slots for each character and automatically manages focus and keyboard navigation.

They typically appear in authentication flows, two-factor authentication (2FA), and multi-factor authentication (MFA) scenarios.

![](../../images/com.adevinta.spark.textfields_InputOTPScreenshot_allStatesAndTypes_light.png)

The minimal usage of the component is:

```kotlin
var otpValue by remember { mutableStateOf("") }

InputOTP(
    value = otpValue,
    onValueChange = { otpValue = it },
    length = 4
)
```

### Specifications
- **Size**: 40px width × 50px height per slot, 8dp spacing between slots
- **Length**: 4 to 6 digits (most common: 4 or 6)
- **Input Types**: Numeric (default), Alphanumeric, or Alphabetic
- **Paste support**: Automatically fills cells from left to right
- **Label/Helper**: Not included in component, wrap externally if needed
- **Error feedback**: Show only after backend validation

### Usage Examples

**6-Digit Code with Separator:**
```kotlin
var otpValue by remember { mutableStateOf("") }

InputOTP(
    value = otpValue,
    onValueChange = { otpValue = it },
    length = 6,
    showSeparator = true
)
```

**With Validation State:**
```kotlin
var otpValue by remember { mutableStateOf("") }

InputOTP(
    value = otpValue,
    onValueChange = { otpValue = it },
    length = 4,
    state = FormFieldStatus.Error, // or Success, Alert
    onComplete = {
        // Validate on backend when complete
    }
)
```

**Alphanumeric Code:**
```kotlin
InputOTP(
    value = otpValue,
    onValueChange = { otpValue = it },
    length = 6,
    inputType = InputOTPType.Alphanumeric
)
```


### Input Types

```kotlin
public enum class InputOTPType {
    Numeric,      // 0-9 only (default)
    Alphanumeric, // A-Z, a-z, 0-9
    Alphabetic,   // A-Z, a-z only
}
```

### Parameters

| Parameter | Type | Description |
|-----------|------|-------------|
| `value` | String | Current OTP value |
| `onValueChange` | (String) -> Unit | Callback when value changes |
| `length` | Int | Number of slots (4-6) |
| `modifier` | Modifier | Optional modifier |
| `enabled` | Boolean | Whether input is enabled (default: true) |
| `inputType` | InputOTPType | Numeric, Alphanumeric, or Alphabetic (default: Numeric) |
| `state` | FormFieldStatus? | Validation state (Error, Success, Alert) - set after backend validation |
| `showSeparator` | Boolean | Show separator between groups of 3 (only for length 6) |
| `onComplete` | (() -> Unit)? | Callback when all slots filled |

See the catalog app for live examples: **TextFields > InputOTP Examples**
