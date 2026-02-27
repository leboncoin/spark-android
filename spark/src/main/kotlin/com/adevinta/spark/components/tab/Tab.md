# Package com.adevinta.spark.components.tab

[Tabs](https://spark.adevinta.com/1186e1705/p/7461a4-tabs/b/98915d) are used to group different but
related content, allowing users to navigate views without leaving the page. They always contain at
least two items and one tab is active at a time. Tabs can be used on full page layouts or in
components such as modals, cards, or side panels.

![](../../images/com.adevinta.spark.tab_TabsDocScreenshot_tabsIntentShowcase__light.png)

Most commonly chip contains an `leadingIcon` and/ or the `text`, but you can also provide a
custom `trailingContent`. It is mandatory to provide `contentDescription` for `icon` only tabs.

### Styles

The tab can have one of the `TabSize` values, that will affect the Tabs typography size:

- `ExtraSmall`
- `Small`
- `Medium` (default size)

![](../../images/com.adevinta.spark.tab_TabsDocScreenshot_tabsSizeShowcase.png)

The size of the icon is the same as the size of the text. For icon-only tabs the icon size
is `IconSize.Small`.

The tabs accept 2 colors `TabIntent`s:

- Basic (default)
- Main
- Support

| Light                                                                                    | Dark                                                                                                   |
|------------------------------------------------------------------------------------------|--------------------------------------------------------------------------------------------------------|
| ![](../../images/com.adevinta.spark.tab_TabsDocScreenshot_tabsIntentShowcase__light.png) | ![](../../images/com.adevinta.spark.tab_TabsDocScreenshot_tabsIntentShowcase__dark.png) |

### TabGroup

The tabs occupy just as much space as they need, but they can also be full-width.

Depending on the available place and parameter `spacedEvenly`, a `TabGroup` places its
tabs evenly spaced along the entire row, with each tab taking up an equal amount of space unless
content cannot be fully displayed. In this case each larger tab is attributed a needed space and the
rest is distributed evenly among smaller tabs. If there is not enough screen space to display all
content, TabGroup will not enforce equal size, and allows scrolling to tabs that do not fit on
screen. Each tab will take the needed space ensuring the minimum tab size constraint is met.

### Code Samples
To draw tabs

```kotlin
var selectedIndex by remember { mutableIntStateOf(0) }

TabGroup(
    selectedTabIndex = selectedIndex,
    intent = intent,
) {
    tabs.forEachIndexed { index, tab -> // use your tab data
        Tab(
            intent = intent,
            selected = selectedIndex == index,
            onClick = { selectedIndex = index }, // handle click
            enabled = true,
            icon = tab.icon,
            text = tab.text,
            size = TabSize.ExtraSmall,
            trailingContent = {
                Badge(count = tab.unreadCount)
            },
        )
    }
}
```
