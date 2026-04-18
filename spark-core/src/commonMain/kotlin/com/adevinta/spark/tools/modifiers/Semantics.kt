/*
 * Copyright (c) 2025 Adevinta
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
package com.adevinta.spark.tools.modifiers

import androidx.compose.ui.Modifier
import androidx.compose.ui.semantics.SemanticsModifier
import androidx.compose.ui.semantics.clearAndSetSemantics
import androidx.compose.ui.semantics.hideFromAccessibility
import androidx.compose.ui.semantics.semantics

/**
 * If present, this node is considered hidden from accessibility services.
 *
 * For example, if the node is currently occluded by a dark semitransparent pane above it, then for
 * all practical purposes the node should not be announced to the user. Since the system cannot
 * automatically determine that, this property can be set to make the screen reader linear
 * navigation skip over this type of node.
 *
 * If looking for a way to clear semantics of small items from the UI tree completely because they
 * are redundant with semantics of their parent, consider [SemanticsModifier.clearAndSetSemantics]
 * instead.
 */
public fun Modifier.invisibleSemantic(): Modifier =
    semantics { hideFromAccessibility() }
