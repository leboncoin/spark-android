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
package com.adevinta.spark.components.fileupload

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import com.adevinta.spark.LocalSparkExceptionHandler
import com.adevinta.spark.tools.SparkExceptionHandler
import io.github.vinceglb.filekit.FileKit
import io.github.vinceglb.filekit.PlatformFile
import io.github.vinceglb.filekit.bookmarkData
import io.github.vinceglb.filekit.delete
import io.github.vinceglb.filekit.div
import io.github.vinceglb.filekit.exists
import io.github.vinceglb.filekit.filesDir
import io.github.vinceglb.filekit.fromBookmarkData
import io.github.vinceglb.filekit.readBytes
import io.github.vinceglb.filekit.write

internal class BookmarkManager(val exceptionHandler: SparkExceptionHandler) {
    private val bookmarkFile = FileKit.filesDir / "saved_selectedfile_data.bin"

    suspend fun save(file: PlatformFile) {
        try {
            val bookmark = file.bookmarkData()
            bookmarkFile.write(bookmark.bytes)
        } catch (e: Exception) {
            // Handle exceptions, e.g., log the error
            exceptionHandler.handleException(RuntimeException("Error saving bookmark: ${e.message}"))
        }
    }

    suspend fun load(): PlatformFile? {
        if (!bookmarkFile.exists()) return null

        return try {
            val bytes = bookmarkFile.readBytes()
            val file = PlatformFile.fromBookmarkData(bytes)

            // Best practice: verify the file still exists
            if (file.exists()) {
                file
            } else {
                // The file was moved or deleted, so clean up the stale bookmark
                clear()
                null
            }
        } catch (e: Exception) {
            // Bookmark is invalid or corrupted, clean it up
            clear()
            null
        }
    }

    suspend fun clear() {
        try {
            if (bookmarkFile.exists()) {
                bookmarkFile.delete()
            }
        } catch (e: Exception) {
            exceptionHandler.handleException(IllegalStateException("Error clearing bookmark: ${e.message}"))
        }
    }
}

@Composable
internal fun rememberBookmark(
    exceptionHandler: SparkExceptionHandler = LocalSparkExceptionHandler.current,
): BookmarkManager =
    remember { BookmarkManager(exceptionHandler) }
