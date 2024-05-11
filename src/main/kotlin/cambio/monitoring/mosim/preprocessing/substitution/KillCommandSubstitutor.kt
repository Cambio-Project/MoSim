package cambio.monitoring.mosim.preprocessing.substitution

import cambio.monitoring.mosim.search.engine.EventList
import cambio.monitoring.mosim.search.event.BooleanEvent
import cambio.monitoring.mosim.search.event.DoubleEvent
import cambio.monitoring.mosim.util.MetricUtils
import cambio.tltea.interpreter.connector.value.MetricDescriptor

class KillCommandSubstitutor(private val command: String) : CommandSubstitutor {
    private val optionDelimiter: String = ","
    private val metricNameSuffix = "_InstanceCount"

    private val substitutionMetric: MetricDescriptor
    private val metricOfInterest: MetricDescriptor
    private val substitutionMetricName: String
    private val targetServiceName: String
    private val changeAmount: Int

    private fun extractParams(command: String): List<String> {
        val startIndex = this.command.indexOfFirst { c -> c == '[' }
        val endIndex = this.command.indexOfLast { c -> c == ']' }
        if (startIndex < 0 || endIndex < 0) {
            throw IllegalArgumentException("$command does not contain options. Thus, it is not a proper command.")
        }
        val paramsString = command.substring(startIndex + 1, endIndex)
        val params = paramsString.split(optionDelimiter)
        if (params.size != 2) {
            throw IllegalArgumentException("$command does not have exactly 2 arguments.")
        }
        return params
    }

    init {
        val params = extractParams(command)
        this.targetServiceName = params[0].trim()
        this.changeAmount = params[1].toInt()

        val metricOfInterestName = MetricUtils.sanitize(this.targetServiceName + this.metricNameSuffix)
        this.metricOfInterest =
            MetricUtils.convertStringToDoubleMetric(MetricUtils.addDoubleListenerSymbol(metricOfInterestName))

        this.substitutionMetricName = metricOfInterestName + "_" + this.changeAmount
        this.substitutionMetric =
            MetricUtils.convertStringToBooleanMetric(MetricUtils.addBooleanListenerSymbol(substitutionMetricName))
    }

    override fun getCommandText(): String {
        return this.command
    }

    override fun getSubstitutionText(): String {
        return "(" + MetricUtils.addBooleanListenerSymbol(this.substitutionMetricName) + ") == (true)"
    }

    override fun getSubstitutionMetric(): MetricDescriptor {
        return this.substitutionMetric
    }

    override fun getRequiredMetrics(): Set<MetricDescriptor> {
        return setOf(metricOfInterest)
    }

    override fun computeSubstitutionMetricData(events: EventList, requiredEvents: EventList): EventList {
        val metricOfInterestData = requiredEvents.getEventList(this.metricOfInterest)

        var lastValue = 0.0
        for (dataPoint in metricOfInterestData) {
            val currentValue = (dataPoint.second as DoubleEvent).value
            val valueDropped = currentValue <= (lastValue - this.changeAmount)
            events.addEvent(dataPoint.first, BooleanEvent(this.substitutionMetric, valueDropped))
            lastValue = currentValue
        }
        return events
    }

}