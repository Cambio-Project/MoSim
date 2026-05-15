package cambio.monitoring.mosim.util

import cambio.tltea.interpreter.connector.value.MetricDescriptor

class EndModeMetricDescriptorProvider {
    companion object {
        val END_MODE_METRIC_DESCRIPTOR = MetricDescriptor("", "end", true)
    }
}