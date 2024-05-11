package cambio.monitoring.mosim.preprocessing.substitution

import cambio.monitoring.mosim.search.engine.EventList
import cambio.monitoring.mosim.util.MetricUtils
import cambio.tltea.interpreter.connector.value.MetricDescriptor

class HookReaderCommandSubstitutor(private val command: String) : CommandSubstitutor {
    private val substitutionMetric: MetricDescriptor
    private val targetMetricName: String

    init {
        this.targetMetricName = extractTargetMetricName(command)
        this.substitutionMetric =
            MetricUtils.convertStringToBooleanMetric(MetricUtils.addBooleanListenerSymbol(targetMetricName))
    }

    private fun extractTargetMetricName(command: String): String {
        val startIndex = this.command.indexOfFirst { c -> c == '[' }
        val endIndex = this.command.indexOfLast { c -> c == ']' }
        if (startIndex < 0 || endIndex < 0) {
            throw IllegalArgumentException("$command does not contain options. Thus, it is not a proper command.")
        }
        return command.substring(startIndex + 1, endIndex)
    }

    override fun getCommandText(): String {
        return this.command
    }

    override fun getSubstitutionText(): String {
        return "(" + MetricUtils.addBooleanListenerSymbol(this.targetMetricName) + ") == (true)"
    }

    override fun getSubstitutionMetric(): MetricDescriptor {
        return this.substitutionMetric
    }

    override fun getRequiredMetrics(): Set<MetricDescriptor> {
        return setOf()
    }

    override fun computeSubstitutionMetricData(events: EventList, requiredEvents: EventList): EventList {
        // do nothing
        return events
    }

}