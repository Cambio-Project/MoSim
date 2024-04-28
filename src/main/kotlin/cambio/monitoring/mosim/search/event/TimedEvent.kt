package cambio.monitoring.mosim.search.event

import cambio.tltea.interpreter.connector.time.TimedEvent

data class TimedEvent(val timedEvent: TimedEvent) : Event {
    override fun equals(other: Any?): Boolean {
        if (other is cambio.monitoring.mosim.search.event.TimedEvent) {
            return timedEvent == other.timedEvent
        }
        return false
    }

    override fun hashCode(): Int {
        return timedEvent.hashCode()
    }
}