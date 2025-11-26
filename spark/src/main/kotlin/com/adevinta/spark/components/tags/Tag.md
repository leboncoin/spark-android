# Package com.adevinta.spark.components.tags

[Tags](https://spark.adevinta.com/1186e1705/p/295e88-tag/b/86ead2) are used to label content and help users quickly recognize info about them: Categories, Status… Can be applied with different colors and designs that are associated with a content due to its characteristics: new content, unvisited content, featured content… Users can’t interact with Tags.

|       | Filled                                                                                                    | Outlined                                                                                                    | Tinted                                                                                                   |
|-------|-----------------------------------------------------------------------------------------------------------|-------------------------------------------------------------------------------------------------------------|----------------------------------------------------------------------------------------------------------|
| Light | ![](../../images/com.adevinta.spark_PreviewScreenshotTests_preview_tests_tags_tagfilledpreview_light.png) | ![](../../images/com.adevinta.spark_PreviewScreenshotTests_preview_tests_tags_tagoutlinedpreview_light.png) | ![](../../images/com.adevinta.spark_PreviewScreenshotTests_preview_tests_tags_tagtonalpreview_light.png) |
| Dark  | ![](../../images/com.adevinta.spark_PreviewScreenshotTests_preview_tests_tags_tagfilledpreview_dark.png)  | ![](../../images/com.adevinta.spark_PreviewScreenshotTests_preview_tests_tags_tagoutlinedpreview_dark.png)  | ![](../../images/com.adevinta.spark_PreviewScreenshotTests_preview_tests_tags_tagtonalpreview_dark.png)  |

The minimal usage of the component is the text.

```kotlin
TagFilled(text = "Main")
```

The tags can also have a decorative start icon to better identify the context of the tag.

### Layout

We don't provide any container for now but we recommend using a [`FlowRow`](https://developer.android.com/jetpack/compose/layouts/flow) to layout it in your screens.

```kotlin
FlowRow(
    horizontalArrangement = Arrangement.spacedBy(8.dp),
    verticalArrangement = Arrangement.spacedBy(8.dp),
    maxItemsInEachRow = 4,
) {
    TagFilled(text = "Tag 1", intent = TagIntent.Main)
    TagFilled(text = "Tag longer 2", intent = TagIntent.Accent)
    TagFilled(text = "Tag a bit longer 3", intent = TagIntent.Info)
    TagTinted(text = "Tag way more longer 4", intent = TagIntent.Main)
    TagTinted(text = "Tag small 5", intent = TagIntent.Main)
    TagOutlined(text = "Tag 6", intent = TagIntent.Main)
}
````

### Tag "Highlight"

The Highlight tag allows you to temporarily highlight new features of the product. It draws users attention to new features or important updates.

| Light                                                                                                    | Dark                                                                                                                                    |
|----------------------------------------------------------------------------------------------------------|-----------------------------------------------------------------------------------------------------------------------------------------|
| ![TagHighlight Light](../../images/images/com.adevinta.spark.tags_TagsScreenshot_tagHighlight_light.png) | ![TagHighlight Dark](../../images/images/com.adevinta.spark.tags_TagsScreenshot_tagHighlight_dark.png)      |

**Important Usage Guidelines:**

- Use this component exceptionally to avoid visual overload
- Always prioritize the elements being highlighted
- Implement with an End-of-Life Remote Config to prevent indefinite production presence

```kotlin
TagHighlight()
```
