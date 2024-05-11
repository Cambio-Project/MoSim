package cambio.monitoring.mosim.util

import cambio.tltea.interpreter.connector.value.MetricDescriptor
import cambio.tltea.parser.core.ValueASTNode

class MetricUtils {
    companion object {
        private val listenerSymbolDouble: String = "\$"
        private val listenerSymbolBool: String = "\$b:"

        fun addDoubleListenerSymbol(name : String) : String{
            return this.listenerSymbolDouble + name
        }

        fun addBooleanListenerSymbol(name : String) : String{
            return this.listenerSymbolBool + name
        }

        fun sanitize(text: String): String {
            return text.replace('-', '_')
        }

        fun convertToDoubleMetric(node: ValueASTNode): MetricDescriptor {
            // TODO: this is highly fragile code
            return convertStringToDoubleMetric(node.eventName)
        }

        fun convertToBooleanMetric(node: ValueASTNode): MetricDescriptor {
            // TODO: this is highly fragile code
            return convertStringToBooleanMetric(node.eventName)
        }

        fun convertStringToDoubleMetric(name: String): MetricDescriptor {
            val parts = name.split("\$")
            val architectureName = parts[0]
            val metricIdentifier = parts[1]
            return MetricDescriptor(architectureName, metricIdentifier, false)
        }

        fun convertStringToBooleanMetric(name: String): MetricDescriptor {
            val parts = name.split("\$b:")
            val architectureName = parts[0]
            val metricIdentifier = parts[1]
            return MetricDescriptor(architectureName, metricIdentifier, true)
        }
    }
}