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

package com.adevinta.spark.components.fileupload

/**
 * Represents the set of file extensions to be used when selecting the [FileUploadType.File] within a
 * file upload component.
 * Note that the provided list of extensions is not exhaustive and represents only the most commonly used formats.
 *
 * @see FileUploadType.File
 */
public enum class FileExtensionStandard(public val extensions: Set<String>) {
    All(emptySet()),
    PDF(setOf("pdf")),
    Document(setOf("doc", "docx")),
    Spreadsheet(setOf("xls", "xlsx")),
    Presentation(setOf("ppt", "pptx")),
    Text(setOf("txt")),
    Image(setOf("jpg", "jpeg", "png", "gif", "bmp", "webp")),
    Archive(setOf("zip", "rar", "7z", "tar", "gz")),
}
