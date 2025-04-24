package com.adevinta.spark.tools.logger

/**
 * Log messages within the Spark library.
 * Allows swapping the underlying logging mechanism.
 */
public fun interface SparkLogger {
    public fun report(throwable: Throwable)
}

/**
 * Default implementation of [SparkLogger] that will crash on unexpected states.
 */
public object DefaultSparkLogger : SparkLogger {
    override fun report(throwable: Throwable) {
        throw throwable
    }
}

/**
 * A no-operation implementation of [SparkLogger].
 */
public object NoOpSparkLogger : SparkLogger {
    override fun report(throwable: Throwable) {}

}
