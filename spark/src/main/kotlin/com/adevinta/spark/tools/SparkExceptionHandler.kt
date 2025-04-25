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
package com.adevinta.spark.tools

/**
 * Implementations of this interface provide a centralized mechanism for
 * dealing with errors and exceptions that may arise when using a Spark component in an unexpected way.
 * This allows for custom error handling logic, such as log reporting for production or crash in development.
 */
public fun interface SparkExceptionHandler {
    public fun handleException(throwable: Throwable)
}

/**
 * Default implementation of [SparkExceptionHandler] that will rethrow the throwable from spark.
 */
public object DefaultSparkExceptionHandler : SparkExceptionHandler {
    override fun handleException(throwable: Throwable): Unit = throw throwable
}

/**
 * A no-operation implementation of [SparkExceptionHandler].
 */
public object NoOpSparkExceptionHandler : SparkExceptionHandler {
    override fun handleException(throwable: Throwable) {}
}
