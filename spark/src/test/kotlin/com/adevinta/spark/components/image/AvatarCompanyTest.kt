/*
 * Copyright (c) 2026 Adevinta
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */
package com.adevinta.spark.components.image

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalLayoutDirection
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.semantics.SemanticsProperties
import androidx.compose.ui.test.SemanticsMatcher
import androidx.compose.ui.test.assert
import androidx.compose.ui.test.getBoundsInRoot
import androidx.compose.ui.test.junit4.v2.createComposeRule
import androidx.compose.ui.test.onNodeWithContentDescription
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.unit.DpRect
import androidx.compose.ui.unit.LayoutDirection
import androidx.compose.ui.unit.dp
import com.adevinta.spark.PreviewTheme
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner

@RunWith(RobolectricTestRunner::class)
class AvatarCompanyTest {

    @get:Rule
    val composeTestRule = createComposeRule()

    private val isImage = SemanticsMatcher.expectValue(SemanticsProperties.Role, Role.Image)

    @Test
    fun avatar_is_announced() {
        composeTestRule.setContent {
            PreviewTheme { Avatar.Company(model = null) }
        }

        composeTestRule.onNodeWithContentDescription("Profile photo").assertExists().assert(isImage)
    }

    @Test
    fun addon_is_inside_avatar_bounds() {
        setAvatars(LayoutDirection.Ltr, AvatarSize.entries)

        AvatarSize.entries.forEach { size ->
            val (avatar, addon) = boundsOf(size)
            assertTrue("addon $addon is outside avatar $avatar at $size", avatar.contains(addon))
        }
    }

    @Test
    fun addon_is_at_bottom_end_in_ltr() {
        setAvatars(LayoutDirection.Ltr, listOf(AvatarSize.XL))

        val (avatar, addon) = boundsOf(AvatarSize.XL)
        assertEquals(avatar.right, addon.right)
        assertEquals(avatar.bottom, addon.bottom)
    }

    @Test
    fun addon_is_at_bottom_start_in_rtl() {
        setAvatars(LayoutDirection.Rtl, listOf(AvatarSize.XL))

        val (avatar, addon) = boundsOf(AvatarSize.XL)
        assertEquals(avatar.left, addon.left)
        assertEquals(avatar.bottom, addon.bottom)
    }

    private fun setAvatars(layoutDirection: LayoutDirection, sizes: List<AvatarSize>) {
        composeTestRule.setContent {
            PreviewTheme {
                CompositionLocalProvider(LocalLayoutDirection provides layoutDirection) {
                    sizes.forEach { size ->
                        Avatar.Company(
                            modifier = Modifier.testTag(avatarTag(size)),
                            style = size,
                            addon = {
                                custom {
                                    Box(
                                        Modifier
                                            .testTag(addonTag(size))
                                            .size(size.badgeSize),
                                    )
                                }
                            },
                        )
                    }
                }
            }
        }
    }

    private fun boundsOf(size: AvatarSize): Pair<DpRect, DpRect> =
        composeTestRule.onNodeWithTag(avatarTag(size)).getBoundsInRoot() to
            composeTestRule.onNodeWithTag(addonTag(size)).getBoundsInRoot()

    private fun avatarTag(size: AvatarSize) = "avatar-$size"

    private fun addonTag(size: AvatarSize) = "addon-$size"

    private fun DpRect.contains(other: DpRect): Boolean =
        other.left >= left && other.top >= top && other.right <= right && other.bottom <= bottom
}
