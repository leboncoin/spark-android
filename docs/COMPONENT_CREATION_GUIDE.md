# Component Development Standards

This document establishes the architectural patterns, implementation standards, and contribution
workflows for extending the Spark Design System. These guidelines ensure consistency,
maintainability, and adherence to established design system principles across all component
implementations.

> [!NOTE]
> **See also:** [`CONTRIBUTING.md`](./CONTRIBUTING.md) — covers environment setup, the PR workflow, feature flags, the release process, and how to get support. This guide focuses on the technical standards; refer to `CONTRIBUTING.md` for the contribution process.

## Overview

The Spark Design System follows a layered architecture approach, similar to Material Design and
other enterprise design systems, where components are built as composable abstractions over
foundational design tokens. This guide codifies the standards for contributing to this system.

## 🧩 Component Architecture

### 📁 Component Module Organization

Each component in the Spark Design System follows a standardized module structure that promotes
separation of concerns and maintainability. This pattern is consistent with industry best practices
from systems like Material Design Components and Ant Design.

#### 📄 Standard File Structure

The `generate-component.main.kts` script scaffolds this layout:

```text/plain
spark/src/main/kotlin/com/adevinta/spark/components/{package}/
├── Component.kt                 # Public API object with variant functions
├── SparkComponent.kt            # @InternalSparkApi implementation
├── ComponentDefaults.kt         # Default values, constants, helper functions
└── Component.md                 # Component documentation

spark-screenshot-testing/src/test/kotlin/com/adevinta/spark/components/{package}/
└── ComponentScreenshot.kt       # Paparazzi regression + documentation screenshots

catalog/src/main/kotlin/com/adevinta/spark/catalog/
├── configurator/samples/{package}/ComponentConfigurator.kt
└── examples/samples/{package}/ComponentExamples.kt
```

> **Tip:** Run `kotlin scripts/generate-component.main.kts` to scaffold all seven files at once.
> Pass `--variants Elevated Outlined` to pre-populate variant stubs.

#### Reference Implementation: Button Component

The Button component exemplifies this structure:

- `Button.kt` - Public API object with `Filled`, `Outlined`, `Ghost`, `Tinted`, `Contrast` variants
- `ButtonDefaults.kt` - Color mappings, elevation tokens, and shape definitions
- `ButtonIntent.kt` - Semantic color intentions (Main, Support, etc.)
- `ButtonSize.kt` - Standardized sizing scale (Small, Medium, Large)
- `ButtonShape.kt` - Shape abstractions (Square, Rounded, Pill)
- `Buttons.md` - Component documentation with screenshots and usage examples

### API Design Principles

Component APIs in the Spark Design System follow established conventions from the Compose ecosystem
and enterprise design systems. These patterns ensure predictable developer experience and consistent
behavior across all components.

#### Parameter Ordering Convention

All component APIs must adhere to the following parameter ordering, which aligns with Material
Design Components and Jetpack Compose standards:

```kotlin
@Composable
public fun ComponentName(
    // 1. Required behavioral parameters
    onClick: () -> Unit,
    
    // 2. Standard Compose parameters
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    
    // 3. Design system parameters (semantic abstractions)
    intent: ComponentIntent = ComponentIntent.Support,
    size: ComponentSize = ComponentSize.Medium,
    
    // 4. Advanced behavioral parameters
    interactionSource: MutableInteractionSource = remember { MutableInteractionSource() },

    // 5. Content slots (always last)
    content: @Composable () -> Unit,
)
```

This ordering provides:

- **Immediate API clarity** for required vs. optional parameters
- **Consistent learning curve** across all system components
- **Optimal auto-completion** experience in IDEs
- **Backward compatibility** for parameter additions

#### Layered Implementation Architecture

Components implement a two-layer architecture separating public APIs from internal implementations.

`Component.kt` — public API object, one function per variant:

```kotlin
public object ComponentName {
    /**
     * ComponentName Filled variant.
     *
     * @param modifier the Modifier to be applied to this componentname
     */
    @Composable
    public fun Filled(
        modifier: Modifier = Modifier,
    ) {
        SparkComponentName(modifier = modifier)
    }
}
```

`SparkComponent.kt` — internal implementation, annotated `@InternalSparkApi`:

```kotlin
@InternalSparkApi
@Composable
internal fun SparkComponentName(
    modifier: Modifier = Modifier,
) {
    Box(modifier = modifier.sparkUsageOverlay()) {
        // TODO: Add component implementation
    }
}
```

`ComponentDefaults.kt` — constants, helpers, and token mappings:

```kotlin
public object ComponentNameDefaults {
    // TODO: Add default values, helper functions, and constants
}
```

#### API Stability Annotations

Component APIs utilize Kotlin's opt-in requirement annotations to communicate stability guarantees:

- **`@InternalSparkApi`** - Internal implementations not intended for external consumption
- **`@ExperimentalSparkApi`** - Public APIs under active development with potential breaking changes
- **Unmarked** - Stable public APIs with semantic versioning guarantees

This annotation strategy allows for iterative component development while maintaining clear
stability contracts for consumers.

### Testing Strategy

The Spark Design System employs a comprehensive testing strategy that ensures visual consistency,
behavioral correctness, and accessibility compliance. This multi-layered approach is essential for
maintaining quality at scale in design systems. 🛡️

#### 📸 Visual Regression Testing (Mandatory)

Visual regression testing using Paparazzi prevents unintended visual changes and ensures consistent
rendering across devices. Each component needs **two kinds** of screenshot test class, both in
`spark-screenshot-testing/src/test/kotlin/com/adevinta/spark/components/{component}/`.

The generator scaffolds a single `ComponentScreenshot.kt` file containing both classes:

##### Regression screenshots (`*Screenshot`)

Groups all states and variants into compact grid-style tests on `PIXEL_C` with `SHRINK`. These
are **not committed** — CI regenerates goldens automatically on every run.

```kotlin
internal class ComponentNameScreenshot {

    @get:Rule
    val paparazzi = paparazziRule(
        renderingMode = SHRINK,
        deviceConfig = DeviceConfig.PIXEL_C,
    )

    @Test
    fun componentNameVariants() = paparazzi.sparkSnapshotNightMode {
        Column {
            ComponentNameIntent.entries.forEach { intent ->
                Row {
                    ComponentName.Filled(intent = intent)
                    ComponentName.Outlined(intent = intent)
                }
            }
        }
    }

    @Test
    fun componentNameStates() = paparazzi.sparkSnapshotNightMode {
        Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
            ComponentName.Filled(enabled = true)
            ComponentName.Filled(enabled = false)
        }
    }
}
```

The template starts with a minimal stub — fill in all intents, sizes, and states as you implement
the component.

##### Documentation screenshots (`*DocumentationScreenshots`)

One test per variant, rendered light/dark side-by-side using `sparkDocSnapshot` on
`DefaultTestDevices.DocPhone` (compact landscape). These **are committed** to the repo and
referenced directly from the component's `.md` file.

```kotlin
internal class ComponentNameDocumentationScreenshots {

    @get:Rule
    val paparazzi = paparazziRule(
        renderingMode = SHRINK,
        deviceConfig = DefaultTestDevices.DocPhone,
    )

    @Test
    fun componentNameFilled() = paparazzi.sparkDocSnapshot {
        ComponentName.Filled()
    }

    @Test
    fun componentNameOutlined() = paparazzi.sparkDocSnapshot {
        ComponentName.Outlined()
    }
}
```

If a variant needs a non-default background (e.g. a contrast variant only visible on coloured
surfaces), pass a `color` lambda:

```kotlin
@Test
fun componentNameContrast() = paparazzi.sparkDocSnapshot(
    color = { SparkTheme.colors.backgroundVariant },
) {
    ComponentName.Contrast()
}
```

The generated filename follows Paparazzi's convention:
`<package>_<ClassName>_<testMethodName>.png`

Reference it from the `.md` file as:
```markdown
![](../../images/com.adevinta.spark.components.componentname_ComponentNameDocumentationScreenshots_componentNameFilled.png)
```

#### ♿ Accessibility Testing (Required for Interactive Components)

Accessibility tests ensure components
meet [RAAM](https://accessibilite.public.lu/fr/raam1.1/index.html) guidelines and integrate properly
with assistive technologies. 🌐

```kotlin
@Test
fun `Stepper semantics suffix`() {
    val minRange = 0
    val maxRange = 10
    val step = 2
    val initialValue = 4
    val stepperTestTag = "myStepper"

    composeTestRule.setContent {
        PreviewTheme {
            var value by remember { mutableIntStateOf(initialValue) }
            Stepper(
                value = value,
                onValueChange = { value = it },
                modifier = Modifier.testTag(stepperTestTag),
                range = minRange..maxRange,
                step = step,
                suffix = " €",
                testTag = stepperTestTag,
            )
        }
    }
    // Verify that the suffix is read by accessibility services
    composeTestRule.onNodeWithTag(stepperTestTag)
        .assert(
            SemanticsMatcher.expectValue(SemanticsProperties.StateDescription, "4 €"),
        )
}
```

#### Behavioral Testing (Optional)

Unit tests verify component behavior, state management, and callback handling. These tests ensure
components integrate correctly with the Compose runtime that can't be tested with snapshots.

```kotlin
// spark/src/test/kotlin/com/adevinta/spark/{component}/
class ComponentTest {
    @get:Rule
    val composeTestRule = createComposeRule()

    @Test
    fun `component responds to user interactions correctly`() {
        var clicked = false
        
        composeTestRule.setContent {
            ComponentName(onClick = { clicked = true })
        }
        
        composeTestRule.onNode(hasClickAction())
            .performClick()
            
        assertTrue(clicked)
    }
    
    @Test
    fun `component reflects state changes`() {
        // Test state management and recomposition behavior
    }
}
```

### 📚 Documentation Standards

Documentation is critical for adoption and serves as the interface between the system and its
consumers. Spark follows documentation patterns established by successful enterprise design systems
by publishing technical documentation, functional documentaion and a test application.

#### 📖 Component Documentation Structure

Each component's `.md` file lives alongside the source in the component package. The generator
scaffolds this structure:

```markdown
# Package com.adevinta.spark.components.{package}

[ComponentName](https://spark.adevinta.com/...) TODO: Add component description.

![](../../images/com.adevinta.spark.components.{package}_{ComponentName}DocumentationScreenshots_{variantMethodName}.png)

The minimal usage of the component is:

```kotlin
ComponentName.FirstVariant()
```

#### ComponentName.FirstVariant

![](../../images/com.adevinta.spark.components.{package}_{ComponentName}DocumentationScreenshots_{variantMethodName}.png)

TODO: Add description for FirstVariant variant.

```kotlin
ComponentName.FirstVariant()
```
```

Each image is a side-by-side light/dark screenshot generated by `sparkDocSnapshot`.
See [Documentation Screenshots](CONTRIBUTING.md#documentation-screenshots) for how to produce them.

The filename pattern is Paparazzi's standard convention:
`<package>_<ClassName>_<testMethodName>.png`

For example:
```
../../images/com.adevinta.spark.components.chips_ChipDocumentationScreenshots_chipOutlined.png
```

### API Documentation Standards

All public APIs must include comprehensive KDoc documentation following Kotlin documentation
conventions:

```kotlin
/**
 * [Component] provides [functional description] following [design principle].
 *
 * This component implements [Material Design pattern] adapted for Spark's 
 * visual language and supports [key capabilities].
 *
 * @param required describing the primary data/behavior requirement
 * @param modifier [Modifier] to be applied to the component layout
 * @param intent [ComponentIntent] defining the semantic color treatment
 * @param enabled Controls interactive state and visual presentation
 * @param interactionSource [MutableInteractionSource] for observing user interactions
 * 
 * @sample ComponentBasicSample
 * @sample ComponentAdvancedSample
 */
@Composable
public fun ComponentName(/* parameters */) { /* implementation */ }
```

#### Documentation quality requirements

- 🎯 **Functional clarity** - Clearly articulate the component's purpose and appropriate usage
  contexts
- 📖 **Complete API coverage** - Document all public parameters with type information and behavioral
  implications
- 💻 **Practical examples** - Provide working code samples for common and specific use cases
- 🔗 **Design system integration** - Link to official design specifications and related components
- ♿ **Accessibility guidance** - Document screen reader behavior and keyboard interaction patterns

### Component Catalog App integration

The Spark Design System includes a comprehensive component catalog that serves as both a development
tool and a reference implementation showcase. 📱

#### Example Implementation Requirements

Each component must provide interactive examples that demonstrate core functionality and edge cases.
These examples serve multiple purposes:

- 🎓 **Developer onboarding** through working code samples showing actual usecases and how to use the
  component
- 🎨 **Design validation** via visual component showcase showing all the variants and states
- 🔧 **Integration testing** in realistic usage contexts showing how the component can be used in a
  real app

```kotlin
// catalog/src/main/kotlin/com/adevinta/spark/catalog/examples/samples/{package}/
private const val ComponentNameExampleDescription = "ComponentName examples"
private const val ComponentNameExampleSourceUrl = "$SampleSourceUrl/ComponentNameSamples.kt"

public val ComponentNameExamples: ImmutableList<Example> = persistentListOf(
    Example(
        id = "filled",
        name = "Filled ComponentName",
        description = ComponentNameExampleDescription,
        sourceUrl = ComponentNameExampleSourceUrl,
    ) {
        ComponentName.Filled()
    },
    Example(
        id = "outlined",
        name = "Outlined ComponentName",
        description = ComponentNameExampleDescription,
        sourceUrl = ComponentNameExampleSourceUrl,
    ) {
        ComponentName.Outlined()
    },
)
```

💡 **Tip:** If you want to plan your examples before doing the implementation you can use the
WipIllustration component that shows an illustration with a work in progress text. 🚧

#### Component registry integration

Components must be registered in the central catalog system to appear in the developer showcase
application:

```kotlin
// catalog/src/main/kotlin/com/adevinta/spark/catalog/model/Components.kt
private val ComponentName = Component(
    id = "component-name",
    name = "Component Display Name",
    description = R.string.component_description,
    illustration = R.drawable.component_illustration, // Optional, it'll use the spark logo by default
    guidelinesUrl = "$ComponentGuidelinesUrl/component-path",
    docsUrl = "$PackageSummaryUrl/com.adevinta.spark.components.component/index.html",
    sourceUrl = "$SparkSourceUrl/kotlin/com/adevinta/spark/components/component/Component.kt",
    examples = ComponentExamples,
    configurators = listOf(ComponentConfigurator),
)

// Add to the main components list at the bottom of the file
public val Components: List<Component> = listOf(
    // ... existing components
    ComponentName,
).sortedBy { it.name }
```

#### Interactive configuration support aka the Configurator 😈

For components with multiple configuration options, provide interactive configurators that allow
real-time parameter adjustment:

```kotlin
// catalog/src/main/kotlin/com/adevinta/spark/catalog/configurator/samples/{package}/
public val ComponentNameConfigurator: ImmutableList<Configurator> = persistentListOf(
    Configurator(
        id = "componentname",
        name = "ComponentName",
        description = "ComponentName configuration",
        sourceUrl = "$SampleSourceUrl/ComponentNameSamples.kt",
    ) { _, _ ->
        ComponentNameSample()
    },
)

@Composable
private fun ColumnScope.ComponentNameSample() {
    // TODO: Add configurator implementation
}
```

### Code quality and standards enforcement

The Spark Design System enforces consistent code quality through automated tooling and custom lint
rules. This approach ensures maintainability and consistency across all component implementations.
🔧

#### 📏 Custom Lint Rules

The system includes custom Android Lint rules that enforce design system best practices:

**Material Component Usage Detection**

```kotlin
// ❌ Prohibited - Direct Material3 usage in public APIs
@Composable
fun MyButton() {
    Button(onClick = {}) { Text("Click me") }
}

// ✅ Correct - Spark component usage
@Composable
fun MyButton() {
    ButtonFilled(onClick = {}, text = "Click me")
}
```

**API Consistency Enforcement**

- Component naming must follow established patterns (`ComponentFilled`, `ComponentOutlined`,
  `ComponentGhost`) to make varaition/styles easier to discover
- Import statements must reference Spark packages (`com.adevinta.spark.*`)

#### 🎨 Automated code formatting

Code formatting is enforced through Spotless with KtLint integration:

```bash
# Apply formatting automatically
./gradlew spotlessApply

# Verify formatting compliance
./gradlew spotlessCheck
```

or run it directly from your IDE with the provided run configuration.

#### Architectural compliance

All components must adhere to the established architectural patterns:

**Parameter Ordering**

1. Required behavioral parameters
2. Standard Compose parameters (`modifier`, `enabled`)
3. Design system semantic parameters (`intent`, `size`)
4. Advanced behavioral parameters (`interactionSource`)
5. Content slots (always last)

**API Stability Annotations**

- 🔒 Use `@InternalSparkApi` for internal implementations (not intended for external consumption
  unless it's needed for very specific usecase that would result in dramatic breaking changes)
- 🧪 Use `@ExperimentalSparkApi` for unstable public APIs
- ✅ Leave stable public APIs unmarked

## Contribution Workflow

### Development Process

1. **Design Review** - Ensure component aligns with design system principles
2. **API Design** - Draft public API following established patterns
3. **Implementation** - Build component following architectural guidelines
4. **Testing** - Implement comprehensive test coverage
5. **Documentation** - Create complete API and usage documentation
6. **Catalog Integration** - Add interactive examples and configurators
7. **Review Process** - Submit PR with complete implementation

### Quality Gates

All component contributions must pass automated quality checks:

- **Visual regression tests** pass without unintended changes
- **Lint checks** enforce architectural compliance
- **Code formatting** meets established standards
- **Documentation** includes comprehensive API coverage
- **Accessibility** meets WCAG 2.1 AA guidelines

### Design System Governance

Component additions and modifications follow the design system governance process:

- **Design approval** from design system team
- **API review** for namings, slots or restrictive api, etc.
- **Breaking change assessment** for existing component modifications
- **Documentation review** for clarity and completeness
