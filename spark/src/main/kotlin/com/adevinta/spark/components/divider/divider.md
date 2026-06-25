# Package com.adevinta.spark.components.divider

[Dividers](https://spark.adevinta.com/1186e1705/p/867b47-divider)
The Divider component provides a thin, unobtrusive line that separates and distinguishes sections of content to reinforce visual hierarchy. A few common uses are:
Separating sections on a page.
Creating a visual contrast between 2 sides of a page.

Dividers are typically placed throughout your UI in places like:
- Lists
- Forms
- Cards
- Toolbars
- Dialogs

#### Horizontal Divider
```kotlin
Column {
    Title()
    HorizontalDivider()
    Description()
}
```

## Horizontal Divider with Label
```kotlin
HorizontalDivider(
    intent = DividerIntent.Outline, 
    label = "Label", 
    labelHorizontalAlignment = LabelHorizontalAlignment.Center
)
```

#### Vertical Divider
```kotlin
Row {
    VerticalDivider()
}
```

## Vertical Divider with Label
```kotlin
VerticalDivider(
    intent = DividerIntent.OutlineHigh,
    label = "Label",
    labelVerticalAlignment = LabelVerticalAlignment.Center
)
```
