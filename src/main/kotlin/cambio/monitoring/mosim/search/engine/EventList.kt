package cambio.monitoring.mosim.search.engine

import cambio.monitoring.mosim.search.event.Event
import cambio.monitoring.mosim.search.event.TimedEvent
import cambio.tltea.parser.core.temporal.TimeInstance
import java.util.*

/**
 * A list of events sorted by time.
 *
 * Provides operations to manage events and to alter the list as a whole.
 */
class EventList() {
    private var events: TreeMap<TimeInstance, MutableList<Event>> = TreeMap()

    private constructor(events: TreeMap<TimeInstance, MutableList<Event>>) : this() {
        this.events = events
    }

    fun clone(): EventList {
        val eventCopy = events.mapValues {
            val listCopy = mutableListOf<Event>()
            listCopy.addAll(it.value); listCopy
        }
        return EventList(TreeMap(eventCopy))
    }

    /**
     * Creates a sublist of this event list. Duration is including the end time instance.
     */
    fun sublist(start: TimeInstance, duration: TimeInstance) {
        val startTime = TimeInstance(start)
        // plus epsilon to make it exclusive for sub-map operation
        val startTimePlusEpsilon = TimeInstance(start, true)
        val endTime = startTimePlusEpsilon.add(duration)
        events = TreeMap(events.subMap(startTime, endTime))
    }

    /**
     * Shifts all events in the event list to the left by the given time amount.
     */
    fun shiftLeft(shift: TimeInstance) {
        events = TreeMap<TimeInstance, MutableList<Event>>(events.mapKeys { it.key.subtract(shift) })
    }

    /**
     * Returns the latest time for which an event exists.
     */
    fun getLatestTime(): TimeInstance {
        return events.lastEntry().key
    }

    fun addEvent(time: TimeInstance, event: Event) {
        val valueList: MutableList<Event>
        if (events.containsKey(time)) {
            valueList = events[time]!!
        } else {
            valueList = mutableListOf()
            events[time] = valueList
        }
        valueList.add(event)
    }

    fun removeEvent(time: TimeInstance, event: TimedEvent) {
        val eventsAtTime = events[time]!!
        if (eventsAtTime.contains(event)) {
            if (eventsAtTime.size == 1) {
                events.remove(time)
            } else {
                eventsAtTime.remove(event)
            }
        }
    }

    fun viewNext(): Event {
        return events.firstEntry().value.first()
    }

    fun pollNextEvent(): Pair<TimeInstance, Event>? {
        val firstEntry = events.firstEntry()
        if (events.keys.isNotEmpty()) {
            val firstEventList = firstEntry.value
            val firstEvent = firstEventList.first()
            if (firstEventList.size == 1) {
                events.remove(firstEntry.key)
            } else {
                firstEventList.removeAt(0)
            }
            return Pair(firstEntry.key, firstEvent)
        }
        return null
    }

    fun hasNext(): Boolean {
        return events.isNotEmpty()
    }

}