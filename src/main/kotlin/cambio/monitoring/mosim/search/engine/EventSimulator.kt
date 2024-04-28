package cambio.monitoring.mosim.search.engine

import cambio.monitoring.mosim.search.event.BooleanEvent
import cambio.monitoring.mosim.search.event.DoubleEvent
import cambio.monitoring.mosim.search.event.Event
import cambio.monitoring.mosim.search.event.TimedEvent
import cambio.tltea.interpreter.connector.Brokers
import cambio.tltea.interpreter.connector.value.IMetricListener
import cambio.tltea.interpreter.connector.value.MetricDescriptor
import cambio.tltea.parser.core.temporal.TimeInstance
import kotlin.collections.HashMap

/**
 * Feeds Tl-Tea with monitoring data by sending events to and delegating events from Tl-Tea. The monitoring data is provided as a list of events.
 */
class EventSimulator(private val brokers: Brokers, internal var events: EventList = EventList()) {
    private val listenersDouble: HashMap<MetricDescriptor, IMetricListener<Double>> = HashMap()
    private val listenersBoolean: HashMap<MetricDescriptor, IMetricListener<Boolean>> = HashMap()
    private var currentTime: TimeInstance = TimeInstance(0)
    private var lastUpdate: TimeInstance = TimeInstance(0)

    fun addDoubleListener(descriptor: MetricDescriptor, listener: IMetricListener<Double>) {
        listenersDouble[descriptor] = listener
    }

    fun addBooleanListener(descriptor: MetricDescriptor, listener: IMetricListener<Boolean>) {
        listenersBoolean[descriptor] = listener
    }

    fun addEvent(time: TimeInstance, event: Event) {
        events.addEvent(time, event)
    }

    fun viewNext(): Event {
        return events.viewNext()
    }

    fun handleNext() {
        val firstEntry = events.pollNextEvent() ?: return
        val eventTime = firstEntry.first
        checkAndTriggerEndOfRound(eventTime)
        when (val event = firstEntry.second) {
            is TimedEvent -> handleTimedEvent(event)
            is DoubleEvent -> handleValueEvent(eventTime, event.descriptor, event.value)
            is BooleanEvent -> handleValueEvent(eventTime, event.descriptor, event.value)
        }
        this.lastUpdate = this.currentTime
    }

    fun hasNext(): Boolean {
        return events.hasNext()
    }

    fun triggerEndExperiment(time: TimeInstance) {
        brokers.timeManager.triggerExperimentEnded(time)
    }

    fun triggerEndExperiment() {
        brokers.timeManager.triggerExperimentEnded(lastUpdate)
    }

    fun triggerEndRound() {
        brokers.timeManager.triggerTimeInstanceEnded()
    }

    private fun checkAndTriggerEndOfRound(time: TimeInstance) {
        if (time != this.lastUpdate) {
            brokers.timeManager.triggerTimeInstanceEnded()
        }
    }

    private fun handleValueEvent(time: TimeInstance, descriptor: MetricDescriptor, value: Double) {
        currentTime = time
        val listener = listenersDouble[descriptor]!!
        listener.update(value, currentTime)
    }

    private fun handleValueEvent(time: TimeInstance, descriptor: MetricDescriptor, value: Boolean) {
        currentTime = time
        val listenerBoolean = listenersBoolean[descriptor]!!
        listenerBoolean.update(value, currentTime)
    }

    private fun handleTimedEvent(event: TimedEvent) {
        event.timedEvent.fire()
    }

}