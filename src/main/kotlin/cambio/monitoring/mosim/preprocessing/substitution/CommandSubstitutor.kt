package cambio.monitoring.mosim.preprocessing.substitution

import cambio.monitoring.mosim.search.engine.EventList
import cambio.tltea.interpreter.connector.value.MetricDescriptor

interface CommandSubstitutor {
    fun getCommandText(): String
    fun getSubstitutionText(): String
    fun getSubstitutionMetric(): MetricDescriptor
    fun getRequiredMetrics(): Set<MetricDescriptor>
    fun computeSubstitutionMetricData(events: EventList, requiredEvents: EventList): EventList
}