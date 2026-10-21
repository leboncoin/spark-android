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
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.DpRect
import androidx.compose.ui.unit.LayoutDirection
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.height
import androidx.compose.ui.unit.width
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
    fun addon_overflows_bottom_end_by_a_quarter_of_its_size() {
        val sizes = AvatarSize.entries - AvatarSize.XS
        setAvatars(LayoutDirection.Ltr, sizes)

        sizes.forEach { size ->
            val (avatar, addon) = boundsOf(size)
            assertTrue("addon has no size at $size", addon.width > 0.dp && addon.height > 0.dp)
            assertDpEquals(
                "end overflow at $size",
                overflow(addon.width),
                addon.right - avatar.right,
                PIXEL_ROUNDING,
            )
            assertDpEquals(
                "bottom overflow at $size",
                overflow(addon.height),
                addon.bottom - avatar.bottom,
                PIXEL_ROUNDING,
            )
        }
    }

    @Test
    fun addon_is_at_bottom_end_in_ltr() {
        setAvatars(LayoutDirection.Ltr, listOf(AvatarSize.XL))

        val (avatar, addon) = boundsOf(AvatarSize.XL)
        assertDpEquals(avatar.right + overflow(addon.width), addon.right, PIXEL_ROUNDING)
        assertDpEquals(avatar.bottom + overflow(addon.height), addon.bottom, PIXEL_ROUNDING)
    }

    @Test
    fun addon_is_at_bottom_start_in_rtl() {
        setAvatars(LayoutDirection.Rtl, listOf(AvatarSize.XL))

        val (avatar, addon) = boundsOf(AvatarSize.XL)
        assertDpEquals(avatar.left - overflow(addon.width), addon.left, PIXEL_ROUNDING)
        assertDpEquals(avatar.bottom + overflow(addon.height), addon.bottom, PIXEL_ROUNDING)
    }

    private fun setAvatars(layoutDirection: LayoutDirection, sizes: List<AvatarSize>) {
        composeTestRule.setContent {
            PreviewTheme {
                CompositionLocalProvider(LocalLayoutDirection provides layoutDirection) {
                    sizes.forEach { size ->
                        Avatar.Company(
                            modifier = Modifier.testTag(avatarTag(size)),
                            size = size,
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

    private fun overflow(addonSize: Dp): Dp = addonSize * 0.25f

    private fun assertDpEquals(expected: Dp, actual: Dp, delta: Dp) =
        assertDpEquals(null, expected, actual, delta)

    private fun assertDpEquals(message: String?, expected: Dp, actual: Dp, delta: Dp) =
        assertEquals(message, expected.value, actual.value, delta.value)

    private companion object {
        // The placement rounds 75% of the addon size to whole pixels.
        val PIXEL_ROUNDING = 0.5.dp
    }
}
