package cambio.monitoring.mosim.search

import cambio.monitoring.mosim.search.engine.EventList
import cambio.monitoring.mosim.search.engine.EventSimulator

/**
 * Runs the search on a concrete event list.
 */
interface SearchExecutor {
    fun execute(simulator: EventSimulator, data: EventList)
}