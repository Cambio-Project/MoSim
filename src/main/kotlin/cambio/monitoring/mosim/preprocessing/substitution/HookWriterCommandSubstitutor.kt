package cambio.monitoring.mosim.preprocessing.substitution

import cambio.monitoring.mosim.search.engine.EventList
import cambio.monitoring.mosim.search.event.BooleanEvent
import cambio.monitoring.mosim.util.MetricUtils
import cambio.tltea.interpreter.connector.value.MetricDescriptor

class HookWriterCommandSubstitutor(private val fullCommand: String) : CommandSubstitutor {
    private val substitutionMetric: MetricDescriptor
    private val metricOfInterest: MetricDescriptor
    private val targetMetricName: String
    private val killCommandText: String

    private fun splitIntoContextAndCommand(fullCommand: String): Pair<String, String> {
        val both = fullCommand.split("&", "∧")
        return Pair(both[0], both[1])
    }

    private fun extractParamsString(command: String): String {
        val startIndex = command.indexOfFirst { c -> c == '[' }
        val endIndex = command.indexOfLast { c -> c == ']' }
        if (startIndex < 0 || endIndex < 0) {
            throw IllegalArgumentException("$command does not contain options. Thus, it is not a proper command.")
        }
        return command.substring(startIndex + 1, endIndex)
    }

    init {
        val (killCommandText, command) = splitIntoContextAndCommand(fullCommand)
        this.killCommandText = killCommandText

        val killCommandSubstitution = KillCommandSubstitutor(killCommandText)
        this.metricOfInterest = killCommandSubstitution.getSubstitutionMetric()

        this.targetMetricName = extractParamsString(command)
        this.substitutionMetric =
            MetricUtils.convertStringToBooleanMetric(MetricUtils.addBooleanListenerSymbol(targetMetricName))
    }

    override fun getCommandText(): String {
        return this.fullCommand
    }

    override fun getSubstitutionText(): String {
        return this.killCommandText
    }

    override fun getSubstitutionMetric(): MetricDescriptor {
        return this.substitutionMetric
    }

    override fun getRequiredMetrics(): Set<MetricDescriptor> {
        return setOf(metricOfInterest)
    }

    override fun computeSubstitutionMetricData(events: EventList, requiredEvents: EventList): EventList {
        val metricOfInterestData = events.getEventList(this.metricOfInterest)

        // Simply copy the metric of interest data into parameter of new name
        for (dataPoint in metricOfInterestData) {
            val currentValue = (dataPoint.second as BooleanEvent).value
            events.addEvent(dataPoint.first, BooleanEvent(this.substitutionMetric, currentValue))
        }

        return events
    }

}