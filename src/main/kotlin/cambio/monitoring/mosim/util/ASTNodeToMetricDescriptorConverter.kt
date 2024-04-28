package cambio.monitoring.mosim.util

import cambio.tltea.interpreter.connector.value.MetricDescriptor
import cambio.tltea.parser.core.ValueASTNode

class ASTNodeToMetricDescriptorConverter {
    companion object {
        fun convertToDoubleMetric(node: ValueASTNode): MetricDescriptor {
            // TODO: this is highly fragile code
            val parts = node.eventName.split("\$")
            val architectureName = parts[0]
            val metricIdentifier = parts[1]
            return MetricDescriptor(architectureName, metricIdentifier, false)
        }

        fun convertToBooleanMetric(node: ValueASTNode): MetricDescriptor {
            // TODO: this is highly fragile code
            val parts = node.eventName.split("\$")
            val architectureName = parts[0]
            val metricIdentifier = parts[1].replace("b:", "")
            return MetricDescriptor(architectureName, metricIdentifier, true)
        }
    }
}