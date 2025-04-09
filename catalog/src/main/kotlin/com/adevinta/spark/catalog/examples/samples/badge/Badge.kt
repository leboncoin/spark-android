override val examples: List<Example> = listOf(
    Example(
        id = "badge-no-stroke",
        name = stringResource(R.string.badge_example_no_stroke_title),
        description = "Example of a Badge without stroke",
        sourceUrl = BadgeExampleSourceUrl,
    ) {
        Badge(
            modifier = Modifier.padding(16.dp),
            content = { Text("1") },
        )
    },
    Example(
        id = "badge-with-stroke",
        name = stringResource(R.string.badge_example_with_stroke_title),
        description = "Example of a Badge with stroke",
        sourceUrl = BadgeExampleSourceUrl,
    ) {
        Badge(
            modifier = Modifier.padding(16.dp),
            content = { Text("1") },
            hasStroke = true,
        )
    },
) 