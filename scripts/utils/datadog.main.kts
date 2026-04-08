@file:DependsOn("com.datadoghq:datadog-api-client:2.51.0")

import com.datadog.api.client.ApiClient
import com.datadog.api.client.v2.api.MetricsApi
import com.datadog.api.client.v2.model.MetricIntakeType.GAUGE
import com.datadog.api.client.v2.model.MetricPayload
import com.datadog.api.client.v2.model.MetricPoint
import com.datadog.api.client.v2.model.MetricSeries
import java.time.Instant

/**
 * Represents a metric to be submitted to Datadog.
 *
 * @property name The metric name (e.g. "spark.health.artifact.weight")
 * @property value The metric value
 * @property tags Metric-specific tags (common tags are added automatically)
 * @property unit The unit assigned to a metric @see https://docs.datadoghq.com/fr/metrics/units/#liste-dunit%C3%A9s
 */
data class SparkMetric(
    val name: String,
    val value: Double,
    private val tags: List<String> = emptyList(),
    val unit: String? = null,
) {
    val usableTags: List<String>
        get() = tags.map { "spark.health.$it" }.filter { it.isNotBlank() }
}

/**
 * Submits metrics to Datadog.
 *
 * Common tags `team:squad-foundation` and `service:spark-{serviceName}` are added automatically.
 *
 * @param metrics The list of metrics to submit
 * @param serviceName The service name suffix (e.g. "library-metrics" -> "service:spark-library-metrics")
 * Requires `DD_API_KEY` environment variable to be set for actual submission.
 *
 * @param dryRun If true, prints metrics to stdout instead of submitting
 * @param verbose If true, prints additional debug information
 */
fun submitMetrics(
    metrics: List<SparkMetric>,
    serviceName: String,
    dryRun: Boolean = false,
    verbose: Boolean = false,
) {
    if (metrics.isEmpty()) {
        if (verbose) println("No metrics to submit")
        return
    }

    val commonTags = listOf(
        "team:squad-foundation",
        "service:spark-$serviceName",
    )

    if (dryRun) {
        metrics.forEach { metric ->
            val allTags = commonTags + metric.usableTags
            println("[dry-run] ${metric.name} = ${metric.value}  tags=$allTags")
        }
        return
    }

    requireEnvVariable("DD_API_KEY")

    val series = metrics.map { metric ->
        MetricSeries()
            .metric(metric.name)
            .type(GAUGE)
            .run { if (metric.unit != null) this.unit(metric.unit) else this }
            .points(listOf(MetricPoint().timestamp(Instant.now().epochSecond).value(metric.value)))
            .tags(commonTags + metric.usableTags)
    }

    val payload = MetricPayload().series(series)

    val client = ApiClient.getDefaultApiClient().apply {
        enableRetry(true)
    }

    if (verbose) {
        println("Submitting ${metrics.size} metrics to Datadog...")
        metrics.forEach { println("  ${it.name} = ${it.value}") }
    }

    MetricsApi(client).submitMetrics(payload)

    if (verbose) println("Metrics submitted successfully")
}

/**
 * Guards that an environment variable is set, throwing an error if not.
 */
fun requireEnvVariable(name: String) {
    if (System.getenv(name).isNullOrBlank()) error("Required environment variable '$name' is not set.")
}
