package cambio.monitoring.mosim.import

import cambio.monitoring.mosim.search.engine.EventList
import cambio.tltea.parser.core.temporal.TimeInstance

/**
 * Adds the END indicator in an additional column.
 */
interface EndModeDataManipulator {
    fun manipulate(splittedData: List<Pair<TimeInstance, EventList>>): List<Pair<TimeInstance, EventList>>
}