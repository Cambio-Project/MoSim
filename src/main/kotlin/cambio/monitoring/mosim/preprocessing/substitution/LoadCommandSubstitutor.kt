package cambio.monitoring.mosim.preprocessing.substitution

import cambio.monitoring.mosim.search.engine.EventList
import cambio.monitoring.mosim.search.event.BooleanEvent
import cambio.monitoring.mosim.search.event.DoubleEvent
import cambio.monitoring.mosim.search.event.Event
import cambio.monitoring.mosim.util.MetricUtils
import cambio.tltea.interpreter.connector.value.MetricDescriptor
import cambio.tltea.parser.core.temporal.TimeInstance

class LoadCommandSubstitutor(private val command: String) : CommandSubstitutor {
    private val optionDelimiter1: String = ","
    private val optionDelimiter2: String = ":"
    private val allowedConstantDeviation: Double = 0.2
    private val metricNameSuffix = "_ArrivalCount"

    private val substitutionMetric: MetricDescriptor
    private val metricOfInterest: MetricDescriptor
    private val substitutionMetricName: String

    private val startTime: Int
    private val endTime: Int
    private val changeFactor: Double
    private val targetEndpointName: String
    private val changeCurve: String
    private val isInverse: Boolean
    private val isConstant: Boolean

    private fun splitIntoTimingsAndParams(command: String): Pair<List<String>, List<String>> {
        val startIndex = command.indexOfFirst { c -> c == '[' }
        val endIndex = command.indexOfLast { c -> c == ']' }
        if (startIndex < 0 || endIndex < 0) {
            throw IllegalArgumentException("$command does not contain options. Thus, it is not a proper command.")
        }
        val paramsString = command.substring(startIndex + 1, endIndex).split("][")
        if (paramsString.size != 2) {
            throw IllegalArgumentException("$command does not have exactly 2 option parts.")
        }

        val timingParams = paramsString[0].split(optionDelimiter1)
        val params = paramsString[1].split(optionDelimiter2)
        return Pair(timingParams, params)
    }

    init {
        val (timingParams, params) = splitIntoTimingsAndParams(command)
        if (timingParams.size != 2) {
            throw IllegalArgumentException("$command does not have exactly 2 time arguments.")
        }
        if (params.size != 3) {
            throw IllegalArgumentException("$command does not have exactly 3 load arguments.")
        }

        this.startTime = timingParams[0].toInt()
        this.endTime = timingParams[1].toInt()

        this.changeFactor = params[0].replace("x", "").toDouble()
        this.changeCurve = params[1]
        this.targetEndpointName = params[2]

        this.isInverse = this.changeCurve.contains("inverse")
        this.isConstant = this.changeCurve.contains("constant")

        val metricOfInterestName = MetricUtils.sanitize(this.targetEndpointName + this.metricNameSuffix)
        this.metricOfInterest =
            MetricUtils.convertStringToDoubleMetric(MetricUtils.addDoubleListenerSymbol(metricOfInterestName))

        this.substitutionMetricName = metricOfInterestName + "_" + this.changeFactor + isConstant + isInverse
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
        // A window of metric data to check
        val metricCheckWindow = mutableListOf<Pair<TimeInstance, Event>>()

        // add new data as long as available
        for (dataPoint in metricOfInterestData.withIndex()) {
            extendMetricCheckWindow(dataPoint.value, metricCheckWindow, events)
            val windowsIsFilled = windowIsFilled(dataPoint.index, metricOfInterestData, metricCheckWindow)
            val foundLoad = checkWindow(metricCheckWindow, windowsIsFilled)
            if (foundLoad) {
                handleFoundLoad(events, metricCheckWindow)
            }
            // not found is handled when extending the window
        }

        // handle the remaining data
        while (metricCheckWindow.isNotEmpty()) {
            val foundLoad = checkWindow(metricCheckWindow, true)
            if (foundLoad) {
                handleFoundLoad(events, metricCheckWindow)
            } else {
                handleNotFoundLoad(events, metricCheckWindow)
            }
        }

        return events
    }

    /**
     * Extends the window of metrics to check and removes elements that are not within the time frame any longer.
     */
    private fun extendMetricCheckWindow(
        timeAndEvent: Pair<TimeInstance, Event>,
        metricCheckWindow: MutableList<Pair<TimeInstance, Event>>,
        events: EventList
    ) {
        metricCheckWindow.add(timeAndEvent)
        val minTimeWindow = timeAndEvent.first.subtract(TimeInstance(endTime)).add(TimeInstance(startTime))

        var removedElement = true
        while (removedElement) {
            if (metricCheckWindow.isNotEmpty() && metricCheckWindow.first().first < minTimeWindow) {
                events.addEvent(metricCheckWindow.first().first, BooleanEvent(this.substitutionMetric, false))
                metricCheckWindow.removeFirst()
            } else {
                removedElement = false
            }
        }
    }

    /**
     * The window is considered filled when the next data point would remove a datapoint.
     */
    private fun windowIsFilled(
        currentIndex: Int,
        metricOfInterestData: List<Pair<TimeInstance, Event>>,
        metricCheckWindow: List<Pair<TimeInstance, Event>>
    ): Boolean {
        val nextIndex = currentIndex + 1
        val windowsIsFilled = if (nextIndex >= metricOfInterestData.size) {
            true
        } else {
            val minTimeWindowNextDataPoint =
                metricOfInterestData[nextIndex].first.subtract(TimeInstance(endTime)).add(TimeInstance(startTime))
            val firstTimeWindowTime = metricCheckWindow.first().first
            firstTimeWindowTime < minTimeWindowNextDataPoint
        }
        return windowsIsFilled
    }

    private fun handleConstantFunction(
        firstValue: Double,
        metricCheckWindow: List<Pair<TimeInstance, Event>>,
        windowIsFilled: Boolean
    ): Boolean {
        val allowedDeviation = firstValue * this.allowedConstantDeviation
        val minLast = firstValue - allowedDeviation
        val maxLast = firstValue + allowedDeviation

        var valueSum = 0.0
        for (entry in metricCheckWindow) {
            valueSum += (entry.second as DoubleEvent).value
        }
        val avg = valueSum / metricCheckWindow.size
        val lastValue = (metricCheckWindow.last().second as DoubleEvent).value

        return (windowIsFilled && lastValue in minLast..maxLast && avg in minLast..maxLast)
    }

    private fun handleInverseFunction(
        firstValue: Double,
        metricCheckWindow: List<Pair<TimeInstance, Event>>,
    ): Boolean {
        for (pastDataPoint in metricCheckWindow.withIndex()) {
            val currentValue = (pastDataPoint.value.second as DoubleEvent).value
            val valueChangedSignificantly =
                firstValue >= currentValue * this.changeFactor
            if (valueChangedSignificantly) {
                return true
            }
        }
        return false
    }


    private fun handleNormalFunction(
        firstValue: Double,
        metricCheckWindow: List<Pair<TimeInstance, Event>>,
    ): Boolean {
        for (pastDataPoint in metricCheckWindow.withIndex()) {
            val currentValue = (pastDataPoint.value.second as DoubleEvent).value
            val valueChangedSignificantly =
                firstValue * this.changeFactor <= currentValue
            if (valueChangedSignificantly) {
                return true
            }
        }
        return false
    }


    private fun checkWindow(metricCheckWindow: List<Pair<TimeInstance, Event>>, windowIsFilled: Boolean): Boolean {
        val previousValue = (metricCheckWindow.first().second as DoubleEvent).value

        return if (this.isConstant) {
            handleConstantFunction(previousValue, metricCheckWindow, windowIsFilled)
        } else {
            if (this.isInverse) {
                handleInverseFunction(previousValue, metricCheckWindow)
            } else {
                handleNormalFunction(previousValue, metricCheckWindow)
            }
        }
    }

    private fun handleFoundLoad(
        events: EventList,
        metricCheckWindow: List<Pair<TimeInstance, Event>>
    ) {
        events.addEvent(metricCheckWindow.first().first, BooleanEvent(this.substitutionMetric, true))
        metricCheckWindow.removeFirst()
    }

    private fun handleNotFoundLoad(
        events: EventList,
        metricCheckWindow: List<Pair<TimeInstance, Event>>,
    ) {
        events.addEvent(metricCheckWindow.first().first, BooleanEvent(this.substitutionMetric, false))
        metricCheckWindow.removeFirst()
    }

}