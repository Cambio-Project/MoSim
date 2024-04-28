package cambio.monitoring.mosim.import

import cambio.monitoring.mosim.search.engine.EventList
import cambio.tltea.parser.core.temporal.TimeInstance

/**
 * Splits an event list into multiple sub lists for which searches should be conducted.
 */
interface DataSplitter {
    fun split(completeList: EventList): List<Pair<TimeInstance, EventList>>
}