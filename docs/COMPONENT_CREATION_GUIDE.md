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

```text/plain
spark/src/main/kotlin/com/adevinta/spark/components/{component-name}/
├── Component.kt                 # Public API surface
├── ComponentDefaults.kt         # Design token mappings and default values
├── ComponentIntent.kt           # Semantic color variants
├── ComponentSize.kt            # Size specifications
├── ComponentState.kt           # Behavioral state management
└── Component.md                # API documentation and usage guidelines
```

This structure ensures:

- **Clear API boundaries** between public and internal implementations
- **Centralized design token management** through defaults
- **Semantic abstraction** of visual properties through intents
- **Consistent sizing systems** across components
- **Self-documenting** code organization

#### Reference Implementation: Button Component

The Button component exemplifies this structure:

- `Button.kt` - Composable API surface with five style variants
- `ButtonDefaults.kt` - Color mappings, elevation tokens, and shape definitions
- `ButtonIntent.kt` - Semantic color intentions (Basic, Main, Support, etc.)
- `ButtonSize.kt` - Standardized sizing scale (Small, Medium, Large)
- `ButtonShape.kt` - Shape abstractions (Square, Rounded, Pill)
- `Buttons.md` - Component documentation with usage examples

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

Components implement a two-layer architecture separating public APIs from internal implementations:

```kotlin
// Public API Layer - Semantic abstractions
@Composable
public fun ComponentName(
    intent: ComponentIntent = ComponentIntent.Support,
    size: ComponentSize = ComponentSize.Medium,
    // ... other semantic parameters
) {
    val colors = ComponentDefaults.colors(intent)
    val dimensions = ComponentDefaults.dimensions(size)
    
    SparkComponent(
        colors = colors,
        dimensions = dimensions,
        // ... mapped parameters
    )
}

// Internal Implementation Layer - Design token integration
@InternalSparkApi
@Composable
internal fun SparkComponent(
    colors: ComponentColors,
    dimensions: ComponentDimensions,
    // ... resolved design tokens
) {
    // Material3 integration with Spark theming
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
rendering across devices. This approach is standard practice in common compose design systems.

```kotlin
// spark-screenshot-testing/src/test/kotlin/com/adevinta/spark/{component}/
class ComponentScreenshot {
    @get:Rule
    val paparazzi = paparazziRule(
        deviceConfig = DefaultTestDevices.Tablet,
        renderingMode = RenderingMode.SHRINK,
    )

    @Test
    fun componentVariants() {
        paparazzi.sparkSnapshot {
            ComponentShowcase()
        }
    }
    
    @Test
    fun componentStates() {
        paparazzi.sparkSnapshotNightMode {
            ComponentStateShowcase()
        }
    }
}
```

**Coverage Requirements:**

- All visual variants (intents, sizes, shapes)
- Interactive states (default, hover, pressed, disabled)
- Theme variations (light/dark mode)
- Edge cases (empty content, overflow scenarios)

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

Each component requires structured documentation that balances comprehensive reference material with
practical usage guidance:

```markdown
# Package com.adevinta.spark.components.{component}

[Component Name](https://spark.adevinta.com/guidelines/component-path) serves as [primary purpose] in [context of use]. This component implements [relevant design principles] and provides [key benefits].

## When to Use
- [Primary use case]
- [Secondary use case]
- [Edge case consideration]

## API Overview

```kotlin
@Composable
fun ComponentName(
    required: RequiredType,
    modifier: Modifier = Modifier,
    intent: ComponentIntent = ComponentIntent.Support,
    // ... other parameters
)
```

## Variants

The component supports the following semantic variants:

|         | Light Theme                        | Dark Theme                        |
|---------|------------------------------------|-----------------------------------|
| Support | ![](screenshots/support-light.png) | ![](screenshots/support-dark.png) |
| Main    | ![](screenshots/main-light.png)    | ![](screenshots/main-dark.png)    |

// We reccomend to create screenshots test specific for documentation so that they're always up to
date with the documention and they're not bloated by the other tests that are too exhaustive.

## Usage Examples

### Basic Implementation

```kotlin
ComponentName(
    onClick = { /* Handle action */ },
    text = "Primary action"
)
```

### Advanced Configuration

```kotlin
ComponentName(
    onClick = viewModel::handleAction,
    intent = ComponentIntent.Main,
    size = ComponentSize.Large,
    enabled = state.isActionEnabled
)
```

## Accessibility

- [Screen reader considerations]
- [Keyboard navigation behavior]
- [Semantic markup details]

## Design Guidelines

Refer to the [official design specifications](design-system-url) for detailed visual and interaction
guidelines.

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
// catalog/src/main/kotlin/com/adevinta/spark/catalog/examples/samples/{component}/
public val ComponentExamples: ImmutableList<Example> = persistentListOf(
    Example(
        id = "basic-usage",
        name = "Basic Implementation",
        description = "Demonstrates default component behavior with minimal configuration",
        sourceUrl = "$SampleSourceUrl/ComponentSamples.kt",
    ) {
        BasicComponentExample()
    },
    Example(
        id = "variant-showcase",
        name = "Style Variants",
        description = "Shows all available visual styles and semantic intents",
        sourceUrl = "$SampleSourceUrl/ComponentSamples.kt",
    ) {
        ComponentVariantsExample()
    },
    Example(
        id = "interactive-states",
        name = "Interactive States",
        description = "Demonstrates component behavior across different interaction states",
        sourceUrl = "$SampleSourceUrl/ComponentSamples.kt",
    ) {
        ComponentInteractiveExample()
    },
    Example(
        id = "specific-usecase",
        name = "Specific Usecase",
        description = "Demonstrates a specific usecase for the component",
        sourceUrl = "$SampleSourceUrl/ComponentSamples.kt",
    ) {
        ComponentSpecificUsecaseExample()
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
// catalog/src/main/kotlin/com/adevinta/spark/catalog/configurator/{component}/
public val ComponentConfigurator = Configurator(
    id = "component-configurator",
    name = "Component Configuration",
    description = "Interactive component customization",
    sourceUrl = "$ConfiguratorSourceUrl/ComponentConfigurator.kt",
) {
    ConfiguratorComponentExample()
}

// If you need to add more configurators you can add them to the list
public val ComponentConfigurators: List<Configurator> = listOf(
    ComponentConfigurator,
    // ... other configurators
)
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
