# CHANGELOG STYLE GUIDE

*This style guide is partly inspired from [common-changelog](https://github.com/vweevers/common-changelog)*

First, these entries are meant for our consumers from all levels so we need to keep them accessible enough for them. To make it easy on our side to achieve this, we’ve created this style guide for creating a new Changelog on a new version release.
## General Format

### 1. Version Header:
- Format: `## [version-number]`
- Example: `## [0.11.0]`
  This header indicates which version the log entry description concern and follows [Semantic Versioning](https://semver.org/), it’s also sorted from the newest to latest semantic version.
### 2. Date:
- Format: `_YYYY-MM-DD_`
- Example: `_2024-06-24_`
  The date help to quickly get which version was available when and get the release timeline.
### 3. Section Headers:
Sections represent a group of changes scoped to an artefact or section.
For now, we restrict them to the following one to have consistency and avoid ambiguity:
-  `### Spark`
-  `### Catalog App`
-  `### Icons`
-  `### CI`
### 4. Subsections:
- Use subsections for specific features or changes within a component.
- Example: `#### 🆕 New Features`

### 5. Change Types:
Sort content by importance.
Skip content that isn't important.
Link each change to further information.

> [!NOTE]
> You can inspire yourself from [gitmoji ](https://gitmoji.dev/)

Use emojis and clear keywords to categorize changes:
- 🆕 New Feature:
- 🎨 Improvement:
- 💄 UI Change:
- 🐛 Bug Fix:
- 🔧 Maintenance:
- 🚀 Performance Improvement:
- 📝 Documentation:
- 💬 Localisation / Translation:
- ⬆️ Dependency Update:
- 🗑️ Deprecation/Removal:


## Detailed Entries

### 1. Feature/Change Description:
Provide a brief and clear description of the change.
You can use the convention commit from the base but you’ll have to reformat it into a natural language (in the imperative mood) because it fits all contexts, including explaining changes to a stakeholder in person.

You can add reference to a commit, issue or pull request at the end of the same line of a change.
- Example: `🆕 Chips can now be selectable and closed` (#192)
### 2.  Notice, Caution and Warning Notes:
A release might have a separate upgrade guide or blog post that is considered essential reading.

For these purposes, a _notice_ must be used. This is a single-sentence paragraph with otherwise arbitrary Markdown content. Adding Markdown emphasis markers is recommended. For example (links omitted):
```markdown
## [2.0.0]

_2024-06-21_

_If you are upgrading: please see [`UPGRADING.md`](UPGRADING.md)._

### Spark

- **Breaking:** `Progressbar` has been moved to another package (`01e3a64`)
```

Use `> [!CAUTION]` and `> [!WARNING]` for important notices that may impact users like breaking changes, being from compilation or ui.
- Example:
```markdown
> [!CAUTION] 
> The `Filled` style has been removed and may break your build if used.
> 
```

#### Remove noise
Exclude maintenance changes that are not interesting to consumers of the project (in its distributed form). To name a few:
- Dotfile changes (`.gitignore`, `.github`, `.gitlab` and so forth)
- Changes to development-only dependencies.
- Minor code style changes.
- Formatting changes in documentation.
- Change on the CI that don’t affect the final product.

However, changes such as the following must _not_ be excluded:
- Refactorings, which may have unintentional side effects. Let the community review them.
- Changes to supported runtime environments (which may be reflected only in dotfiles).
- Code style changes that use new language features.
- New documentation (if a feature was previously undocumented).

## Example Entry

```markdown
## [0.11.0]

_2024-06-24_

### Spark

#### 🆕 Chips can now be selectable and closed
> [!CAUTION]
> The `Filled` style has been removed and may break your build if used. You need to see with your UI team to know which styles to use instead of this one.

> [!WARNING]
> The styles for chips have been deprecated; you now need to use either the `Chip` or the `ChipSelectable` components and provide the style in the argument.

If you want to make your Chip closable then you will need to add a callback action in the new `onClose` parameter.


#### 🆕 BottomSheet now use the spark specs
> [!CAUTION]
> This change will most likely break your build since most of the API has changed. We now use the M3 `BottomSheet` instead of a fork from an alpha version of it we did when it was only available in M2.

> [!WARNING]
> The `BottomSheet` currently only accepts M3 snackbars; you won't be able to display a SparkSnackbar.


- 🆕 ProgressTracker is now available! It still has a few minor visual bugs but it can be tested by squads on their scope. Don't hesitate to give us feedback!
- 🆕 `TextLinkButton` will now use `LocalContentColor` when using the Surface intent. This will allow you to have an `onSurface` `TextLink` when needed.
- 🆕 `Popover` can now take an intent for its surface color.
- 🆕 `Image` has its `emptyIcon` and `errorIcon` parameters open now for special cases.
- 💬 A11y has been translated to German.
- 💄 `Rating` will now have a readable color when disabled.
- 💄 Badge now uses surface instead of onColor for its border color.
- 🐛 Filled and Contrast `Button` now have a clear disabled state when their content color is dark.
- 💄 New icons have been added.

### Catalog App

- 🎨 Brand colors have been updated to their latest values.
- 🔧 All Configurators are now scrollable.

### CI

- 🔧 Decorrelated spotless and ktlint.
- 🆕 Added Paparazzi as a manual workflow.
- 🆕 Ran Lava Vulnerability Scanner on CI workflow.
- 🔧 Moved code formatting tasks first in the contributing list.

```
