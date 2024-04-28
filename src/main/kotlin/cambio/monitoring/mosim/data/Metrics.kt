package cambio.monitoring.mosim.data

import cambio.tltea.interpreter.connector.value.MetricDescriptor

/**
 * Collection of all metrics of a search.
 */
data class Metrics(
    val metricsPerFormula: List<List<MetricDescriptor>>
) {
    val allMetrics: MutableSet<MetricDescriptor> = mutableSetOf()

    init {
        for (metric in metricsPerFormula) {
            allMetrics.addAll(metric)
        }
    }
}
