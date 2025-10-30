# Contributing to Spark Android

This guide provides instructions for contributing to Spark Android. It covers environment setup, component development standards, testing requirements, and the contribution workflow.


## Table of Contents

### ⚙️ Setup and Configuration

- [Prerequisites](#prerequisites)
- [Environment Setup](#environment-setup)
- [Project Setup](#project-setup)

### 🛠️ Development Workflow

- [🧩 Creating Components](#-creating-components)
- [🧪 Testing Requirements](#-testing)
- [✨ Code Quality Standards](#-code-quality)
- [📱 Catalog Integration](#-demo-app-integration)

### 📝 Documentation and Review

- [📖 Documentation Standards](#-documentation)
- [🔄 Pull Request Process](#-pull-request-process)
- [Code Review Guidelines](#code-review)

### 🔬 Advanced Topics

- [⚗️ Experimental Features](#️-experimental-changes)
- [🚀 Release Process](#-release-process)
- [🆘 Getting Support](#-getting-help)

---

## Prerequisites

### Repository Access

> [!NOTE] 
> Leboncoin employees only
> Please ensure you have push rights to this repository, rather than forking the repository for contributions. Follow the "Engineering Contribution" guide in the (To be defined, Android Guild or Foundation) space in Backstage to get access.

This repository being public, you can directly contribute to it. However, we encourage you to fork the repository for your contributions to avoid conflicts when pulling as only contributors can push to the main repository.

### Knowledge Requirements

Before contributing to Spark, ensure you have:

1. **Basic Jetpack Compose knowledge** - Understanding of `@Composable` functions, state management, and UI composition
2. **Kotlin proficiency** - Comfortable with Kotlin syntax, data classes, and sealed classes
3. **Android development experience** - Familiar with Android Studio, Gradle, and the Android build system
4. **Version control skills** - Git workflow knowledge including branching, merging, and pull requests

### Pre-contribution Checklist

Before starting development:

1. **Review architecture patterns** - Study the [Creating Components](#creating-components) section
2. **Examine existing components** - Look at implementations in `/spark/src/main/kotlin/com/adevinta/spark/components/`
3. **Verify issue tracking** - Ensure there's a corresponding GitHub issue for your planned changes
4. **Understand testing requirements** - Review the [Testing Requirements](#testing) section

---

## Environment Setup

Configure your development environment for Spark Android development. This section covers all required tools and dependencies.

### Prerequisites

1. **Install Android Studio**
   - Download from [developer.android.com](https://developer.android.com/studio)
   - Install with recommended settings

2. **Verify your setup**

   ```bash
   echo $ANDROID_HOME
   # Should output something like: /Users/username/Library/Android/sdk
   ```

3. **[Git LFS](https://git-lfs.com/)**

    This repository uses Git LFS to handle [cashapp/paparazzi](https://github.com/cashapp/paparazzi)
    screenshot testing. Git LFS (Large File Storage) manages large binary files efficiently.
    If you introduce visible changes, you'll likely need to update screenshot files for the tests to
    pass.

    ```bash
    # Install
    brew install git-lfs
    # Check
    git lfs install
    ```

4. **[Java 17](https://github.com/leboncoin/spark-android/issues/74)**

    We currently use a version of AGP which requires developers to use **JDK 17** on Gradle JDK.

    _If you're on macOS, you can install it with [brew](https://formulae.brew.sh/formula/openjdk@17)_

    ```bash
    brew install openjdk@17
    ```

    > ℹ️ If you're using a device with [Apple silicon (M1/M2)](https://support.apple.com/en-us/HT211814) then you might need to install
    > a [zulu distribution](https://www.azul.com/downloads/zulu-community/?version=java-17-lts&architecture=x86-64-bit&package=jdk)

    Alternatively, install it directly from **Android Studio**:  
    `File` → `Settings` → `Build, Execution, Deployment` → `Build Tools` → `Gradle` → `Gradle JDK`

---

## Project Setup

Once you have a compatible environment as described above, you can set up the project:

1. **Clone the repository**

   ```bash
   git clone https://github.com/adevinta/spark-android.git
   cd spark-android
   ```

2. **Open the project in Android Studio**
   - File > Open > Select the `spark-android` directory

3. **Build the project**

   ```bash
   ./gradlew build
   ```

---

## 🧩 Creating Components

The Spark Design System follows a standardized architecture for all components. This section walks you through creating a new component from scratch.

> [!NOTE]
> This section contains all the architectural standards and patterns you need to follow.

### Component Structure

Each component follows this standardized file structure:

```text
spark/src/main/kotlin/com/adevinta/spark/components/{component-name}/
├── Component.kt                 # Public API surface
├── ComponentDefaults.kt         # Design token mappings and default values
├── ComponentIntent.kt           # Semantic color variants
├── ComponentSize.kt            # Size specifications
├── ComponentState.kt           # Behavioral state management (if needed)
└── Component.md                # API documentation and usage guidelines
```

### API Design Principles

All component APIs must follow this parameter ordering convention:

```kotlin
@Composable
public fun ComponentName(
    // 1. Required behavioral parameters
    onClick: () -> Unit,
    
    // 2. Standard Compose parameters
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    
    // 3. Design system parameters (semantic abstractions)
    intent: ComponentIntent = ComponentIntent.Basic,
    size: ComponentSize = ComponentSize.Medium,
    
    // 4. Advanced behavioral parameters
    interactionSource: MutableInteractionSource = remember { MutableInteractionSource() },

    // 5. Content slots (always last)
    content: @Composable () -> Unit,
)
```

### Example: Creating a Tag Component

1. Create the main component file with the internal version ([see](https://github.com/leboncoin/spark-android/blob/f108bfd6ce313005eb2ff9fa563345497829b5f1/spark/src/main/kotlin/com/adevinta/spark/components/badge/Badge.kt#L58C1-L137))
2. Create the public api ([see](https://github.com/leboncoin/spark-android/blob/f108bfd6ce313005eb2ff9fa563345497829b5f1/spark/src/main/kotlin/com/adevinta/spark/components/tags/TagFilled.kt#L35-L60))
3. Create defaults either inlined in the component file or in a separate file if its already too big ([see](https://github.com/leboncoin/spark-android/blob/f108bfd6ce313005eb2ff9fa563345497829b5f1/spark/src/main/kotlin/com/adevinta/spark/components/tags/Tag.kt#L246-L292)):
4. Create intent and size enums ([see](https://github.com/leboncoin/spark-android/blob/f108bfd6ce313005eb2ff9fa563345497829b5f1/spark/src/main/kotlin/com/adevinta/spark/components/tags/TagIntent.kt#L14-L19)):
---

## 🧪 Testing

Testing is crucial for maintaining quality in the Spark Design System. We use multiple testing strategies to ensure components work reliably across different scenarios.

### Visual Regression Testing (Mandatory)

We use Paparazzi for screenshot testing to prevent unintended visual changes.

**Coverage Requirements:**

- All visual variants (intents, sizes, shapes)
- Interactive states (default, hover, pressed, disabled)
- Theme variations (light/dark mode)
- Edge cases (empty content, overflow scenarios)

#### Creating Screenshot Tests

Screenshot tests live in the `spark-screenshot-testing` module ([see](https://github.com/leboncoin/spark-android/blob/f108bfd6ce313005eb2ff9fa563345497829b5f1/spark-screenshot-testing/src/test/kotlin/com/adevinta/spark/tags/TagsScreenshot.kt))

Group all component states into a single screenshot when possible. This approach helps you quickly identify visual changes when you introduce them. It also prevents hundreds of small changes that could hide regressions.
This pattern saves disk space (Git LFS storage on [GitHub incurs costs](https://docs.github.com/en/billing/concepts/product-billing/git-lfs)) and reduces the burden on reviewers.

You can also add different variants like `sparkSnapshotDevices` to generate screenshots for mobile, foldable, and tablet layouts. Use `sparkSnapshotNightMode` for light and dark mode screenshots, and `sparkSnapshotHighContrast` for high contrast mode. However, high contrast mode is more commonly used for tokens rather than individual components.

#### Running Screenshot Tests

Snapshot tests will run on CI and compare them to the stored golden images. However, if you want to debug or verify your tests locally, you can run tests as usual via Android Studio for the default variant and check the output.

```bash
# Run all screenshot tests
./gradlew spark-screenshot-testing:verifyPaparazziRelease
```

Alternatively, use the run configuration: `⚡️ Run unit tests & screenshot tests`

#### Generate Golden Screenshots

```bash
# Run all screenshot tests
./gradlew spark-screenshot-testing:cleanRecordPaparazziRelease
```

Alternatively, use the run configuration: `📸 Record Paparazzi Golden Images`

This command generates the snapshots. Verify that the changes and generated snapshots are as expected, but don't commit the snapshot files. Our CI will generate them using the [automated action](https://github.com/leboncoin/spark-android/actions/workflows/paparazzi-golden-images.yml). Therefore, snapshots generated on your local machine will be different, and the CI will fail. Use this command only for debugging purposes.

#### Best Practices

##### Test Organization

- Group related component tests in dedicated test classes
- Use descriptive test method names that indicate what is being tested
- Test multiple component states in a single screenshot when logical
- Use FlowColumn or similar layouts for compact multi-variant displays

##### Performance Optimization

- Use tablet device configuration for better screenshot real estate
- Enable scrolling (H_SCROLL/V_SCROLL) for wide content layouts
- Limit the number of variants per test to balance coverage and execution time

##### Maintenance

- Update baseline images when legitimate design changes occur
- Review failed screenshot tests carefully to distinguish bugs from intentional changes
- Use consistent component configurations across similar tests


### ♿ Accessibility Testing (Required for Interactive Components)

Accessibility tests ensure components meet [RAAM](https://accessibilite.public.lu/fr/raam1.1/index.html) (Référentiel d'Accessibilité pour les Applications Mobiles) guidelines and integrate properly with assistive technologies.

Components should support these basic requirements:

- Automatically provides semantic role for screen readers
- Supports dynamic text scaling up to 200%
- Maintains minimum touch targets when interactive

For components with semantics that handle content descriptions, state descriptions, and actions, we need to verify that these elements are correctly applied (on the correct layer of the component, for example). We also need to ensure that they remain testable even with the semantics. In some cases when using `clearAndSetSemantics`, it may clear tags and other implicit semantics (roles, actions, descriptions, etc.).

```kotlin
// spark/src/test/kotlin/com/adevinta/spark/badge/
@Test
fun `Badge has proper accessibility semantics`() {
    composeTestRule.setContent {
        Badge(
            text = "1",
            intent = BadgeIntent.Success,
            modifier = Modifier
                .testTag("badge")
                .semantics { contentDescription = "You have 1 new message" },
        )
    }
    
    composeTestRule.onNodeWithTag("badge")
        .assertContentDescriptionEquals("You have 1 new message")
}
```

### Unit Testing (Optional)

Not all components need unit tests since we cover most of our components with snapshots.
These tests ensure components integrate correctly with the Compose runtime in ways that snapshots cannot test. (e.g. callbacks, state management, interactions, navigations etc.)

```kotlin
@Test
fun `Button responds to click events correctly`() {
    var clicked = false
    
    composeTestRule.setContent {
        Button(
            text = "Clickable",
            onClick = { clicked = true },
        )
    }
    
    composeTestRule.onNode(hasClickAction())
        .performClick()
        
    assertTrue(clicked)
}
```

---

## 📱 Catalog/Demo App Integration

The Catalog App is a demo application that showcases all components, configurators, and icons available in the Design System. It serves as both a development tool for testing components interactively and a reference implementation for developers integrating Spark into their applications.

For information about the core Spark Design System components, see [Components](https://spark.adevinta.com/1186e1705/p/590121-components). For details about theming and design tokens, see [Design System](https://spark.adevinta.com/1186e1705/p/1983fd-comment-est-compose-notre-design-system-).

> [!NOTE]
> Leboncoin developers, you can install the catalog application by registering on Firebase App Distribution if you follow this [guide](https://backstage.mpi-internal.com/docs/polaris/system/guild-android/deliveries/#firebase-app-distribution)

To test the application, you can either run `./gradlew :catalog:installDebug` or `./gradlew :catalog:installRelease`, or use the `catalog` run configuration in Android Studio.

### Adding Component Examples

Each component must provide interactive examples that demonstrate core functionality and edge cases. These examples serve multiple purposes:

- 🎓 **Developer onboarding** through working code samples showing actual usecases and how to use the component
- 🎨 **Design validation** via visual component showcase showing all the variants and states
- 🔧 **Integration testing** in realistic usage contexts showing how the component can be used in a real app

[See](https://github.com/leboncoin/spark-android/blob/f108bfd6ce313005eb2ff9fa563345497829b5f1/catalog/src/main/kotlin/com/adevinta/spark/catalog/examples/samples/tags/TagsExamples.kt) 

### Registering Components

Add your component to the central registry ([see](https://github.com/leboncoin/spark-android/blob/f108bfd6ce313005eb2ff9fa563345497829b5f1/catalog/src/main/kotlin/com/adevinta/spark/catalog/model/Components.kt#L387-L398))

> [!TIP]
> If you want to plan your examples before doing the implementation you can use the WipIllustration component that shows an illustration with a work in progress text. 🚧

### Interactive Configurators

For components with multiple configuration options, provide interactive configurators that allow real-time parameter adjustment
[See](https://github.com/leboncoin/spark-android/blob/f108bfd6ce313005eb2ff9fa563345497829b5f1/catalog/src/main/kotlin/com/adevinta/spark/catalog/configurator/samples/tags/TagsConfigurator.kt)

---

## 📖 Documentation

### Component Documentation

Each component requires a comprehensive README following this structure:

[See](https://github.com/leboncoin/spark-android/blob/f108bfd6ce313005eb2ff9fa563345497829b5f1/spark/src/main/kotlin/com/adevinta/spark/components/tags/Tag.md)

### API Documentation Standards

All public APIs must include comprehensive KDoc:

```kotlin
/**
 * Badge provides visual status indication following Spark Design System principles.
 *
 * This component implements semantic color intentions and consistent typography
 * to communicate status, counts, or categories within the interface.
 *
 * @param text The text content to display within the badge
 * @param modifier [Modifier] to be applied to the badge layout
 * @param intent [BadgeIntent] defining the semantic color treatment
 * @param size [BadgeStyle] defining the typography and padding scale
 * 
 * @sample BasicBadgeSample
 * @sample BadgeIntentSample
 */
@Composable
public fun Badge(/* parameters */) { /* implementation */ }
```

---

## 🔄 Pull Request Process

The best way to make an impact is by creating code submissions called pull requests. Pull requests should be solutions to GitHub issues.

### Submitting a Pull Request

1. **Create an issue first** - Make sure there's a GitHub issue for the change you're proposing
2. **Fork the repository** - [Fork](https://github.com/leboncoin/spark-android/fork) the repo for your contributions
3. **Create a feature branch** - Write code in your fork, on a branch if you plan to make multiple changes
4. **Follow the template** - [Create a pull request](https://help.github.com/articles/creating-a-pull-request/) following our [pull request template](.github/PULL_REQUEST_TEMPLATE.md)
5. **Wait for review** - A **[leboncoin/android](https://github.com/orgs/leboncoin/teams/android)** team member will triage and review your pull request
6. **Merge** - If we accept your pull request, the accepting member will merge it for you

### Pull Request Requirements

Before submitting, ensure your PR:

- [ ] Addresses a specific GitHub issue
- [ ] Follows the architectural standards
- [ ] Includes comprehensive tests
- [ ] Has complete documentation
- [ ] Passes all automated checks

### Code Review Guidelines

All contributions follow established code review practices focusing on:

- **API Design**: Consistency with existing patterns
- **Visual Quality**: Pixel-perfect implementation of designs
- **Accessibility**: Compliance with accessibility standards
- **Testing**: Comprehensive coverage of functionality
- **Documentation**: Clear and complete API documentation

---

## ✨ Code Quality

### Code Formatting

Code style is enforced through Spotless with KtLint integration:

```bash
# Apply formatting automatically
./gradlew spotlessApply

# Verify formatting compliance
./gradlew spotlessCheck
```

Alternatively, use the `✨ spotlessApply` run configuration.

### Lint Checks

The project includes custom lint rules that enforce Spark Design System patterns:

```bash
# Run all lint checks
./gradlew lintRelease
```

Alternatively, use the `🔎 Lint` run configuration.

### Architecture Compliance

All components must adhere to established architectural patterns:

- **Parameter Ordering**: Follow the standardized parameter order
- **API Stability**: Use `@InternalSparkApi` for internal implementations
- **Design Tokens**: Use Spark theme tokens, never hardcoded values
- **Component Naming**: Follow `ComponentStyle` conventions (e.g., `ButtonFilled`, `ButtonOutlined`)

---

## ⚗️ Experimental Changes

For experimental features or components that depend on A/B test results or can introduce breaking changes:

### Marking Experimental APIs

Use the `@ExperimentalSparkApi` annotation:

```kotlin
@Composable
fun Badge(
    text: String,
    modifier: Modifier = Modifier,
    @ExperimentalSparkApi experimentalFeature: Boolean = false,
    // other parameters
)
```

### Feature Flags

Feature flags allow you to introduce new APIs or behavior changes that can be toggled on/off by consumers. This is particularly useful for gradual rollouts, A/B testing, or breaking changes that need a migration period.

#### When to Use Feature Flags

Use feature flags when:
- **Adding new optional API parameters** that might change behavior
- **Introducing breaking changes** that need a migration period
- **Implementing experimental features** that may not be stable
- **A/B testing** different component behaviors
- **Gradual rollouts** of new functionality

#### Usage

**Step 1: Define the Feature Flag**

Add a new entry to the `SparkFeatureFlags` class:

```kotlin
// spark/src/main/kotlin/com/adevinta/spark/SparkFeatureFlags.kt
object SparkFeatureFlags {
    /**
     * Enables the new ripple animation for buttons.
     * When enabled, buttons use Material 3 ripple effects.
     * When disabled, buttons use the legacy ripple animation.
     * 
     * @default false (legacy behavior maintained)
     */
    var enableNewButtonRipple: Boolean = false
    
    /**
     * Enables automatic content description generation for badges.
     * When enabled, badges automatically generate accessibility descriptions.
     * When disabled, consumers must provide explicit content descriptions.
     * 
     * @default true (new accessibility feature enabled by default)
     */
    var enableBadgeAutoContentDescription: Boolean = true
}
```

**Step 2: Use the Feature Flag in Your Component**

```kotlin
// spark/src/main/kotlin/com/adevinta/spark/components/button/Button.kt
@Composable
public fun ButtonFilled(
    onClick: () -> Unit,
    text: String,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    // ... other parameters
) {
    val interactionSource = remember { MutableInteractionSource() }
    
    Button(
        onClick = onClick,
        modifier = modifier,
        enabled = enabled,
        interactionSource = interactionSource,
        // Use feature flag to determine behavior
        colors = if (SparkFeatureFlags.enableNewButtonRipple) {
            ButtonDefaults.newRippleColors()
        } else {
            ButtonDefaults.legacyColors()
        }
    ) {
        Text(text = text)
    }
}
```

**Step 3: Consumer Override Example**

Here's how a consumer (like the catalog app or a feature team) can override the feature flag:

```kotlin
...
// Override Spark feature flags before using components
SparkFeatureFlags.enableNewButtonRipple = true
CompositionLocalProvider(
    LocalSparkFeatureFlag provides SparkFeatureFlag(
        enableNewButtonRipple = true,
    ),
) {
    ButtonFilled()
}
...
```

**Or in a specific activity/fragment:**

```kotlin
// In a specific screen where you want to test the new behavior
@Composable
fun MyScreen() {
    // Temporarily override for this screen
    DisposableEffect(Unit) {
        val originalRippleFlag = SparkFeatureFlags.enableNewButtonRipple
        SparkFeatureFlags.enableNewButtonRipple = true
        
        onDispose {
            // Restore original value when leaving the screen
            SparkFeatureFlags.enableNewButtonRipple = originalRippleFlag
        }
    }
    
    // Use components normally - they'll use the overridden flag
    ButtonFilled(
        onClick = { /* handle click */ },
        text = "Try New Ripple Effect"
    )
}
```

#### Best Practices

1. **Document the flag purpose** and default behavior clearly
2. **Use descriptive names** that indicate what the flag controls
3. **Set sensible defaults** that maintain backward compatibility when possible
4. **Plan flag lifecycle** - consider when to remove the flag and make the behavior permanent
5. **Test both states** of the flag in your component tests
6. **Coordinate with consumers** when changing default values or removing flags

---

## 🚀 Release Process

The indepth documentation for this subject is available in [RELEASING.md](../RELEASING.md).

### Local Testing

To test your implementation locally:

```bash
# Publish to local Maven repository
./gradlew publishToMavenLocal -Pversion=x.x.x-SNAPSHOT

# Use in your project with mavenLocal() repository
implementation 'com.adevinta.spark:spark-android:x.x.x-SNAPSHOT'
```

### API Documentation Generation

Generate comprehensive API documentation:

```bash
./gradlew dokkaHtmlMultiModule --no-configuration-cache
```

Documentation will be generated in the `build/dokka` folder and you can then preview it locally.

### Contribution Checklist

Before submitting a PR, ensure:

- [ ] Component follows architectural standards
- [ ] Screenshot tests cover all visual variants
- [ ] Accessibility tests for interactive components
- [ ] Comprehensive API documentation
- [ ] Component integrated into catalog app
- [ ] All lint checks pass
- [ ] Code properly formatted

---

## 🆘 Getting Help

### 📚 Common Resources

| Resource Type | Location |
|---------------|----------|
| **Design specifications** | [Spark Design Guidelines](https://spark.adevinta.com) |
| **Implementation examples** | Existing components in `/catalog/src/main/kotlin/com/adevinta/spark/catalog/examples/samples/` |
| **API patterns** | Component files (Button, TextField, etc.) |

### 💬 Support Channels

**For technical questions:**
- [#adv-spark-android Slack channel](https://adevinta.enterprise.slack.com/archives/C04QQEU3Y2H)

**For bug reports or feature requests:**
- [Open a GitHub issue](https://github.com/leboncoin/spark-android/issues/new)

**For design-related questions:**
- Consult the [design guidelines](https://spark.adevinta.com)
- Ask in the Slack channel

### 📋 Quick Reference Checklists

<details>
<summary>🚀 First-Time Setup Checklist</summary>

- [ ] Read the [Prerequisites](#prerequisites)
- [ ] Complete [Environment Setup](#environment-setup)
- [ ] Clone and [build the project](#project-setup)
- [ ] Review [existing components](spark/src/main/kotlin/com/adevinta/spark/components/)
- [ ] Explore the [catalog app](#-demo-app-integration)

</details>

<details>
<summary>🛠️ Component Development Checklist</summary>

- [ ] Create component files following [structure](#component-structure)
- [ ] Implement [API design patterns](#api-design-principles)
- [ ] Add [screenshot tests](#visual-regression-testing-mandatory)
- [ ] Add [accessibility tests](#-accessibility-testing-required-for-interactive-components) (if interactive)
- [ ] Write [documentation](#component-documentation)
- [ ] Add [catalog examples](#adding-component-examples)
- [ ] Submit [pull request](#-pull-request-process)

</details>
