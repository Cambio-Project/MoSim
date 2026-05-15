package cambio.monitoring.mosim.import

import cambio.monitoring.mosim.search.engine.EventList
import cambio.monitoring.mosim.search.event.BooleanEvent
import cambio.monitoring.mosim.util.EndModeMetricDescriptorProvider
import cambio.tltea.parser.core.temporal.TimeInstance

/**
 * Increases max time by one and adds an event at the additional time point.
 */
class DefaultEndModeDataManipulator : EndModeDataManipulator {
    override fun manipulate(splittedData: List<Pair<TimeInstance, EventList>>): List<Pair<TimeInstance, EventList>> {
        for(dataRow in splittedData){
            val newMaxTime : TimeInstance = dataRow.second.maxTime.add(TimeInstance(1))
            dataRow.second.maxTime = newMaxTime
            dataRow.second.addEvent(newMaxTime, BooleanEvent(EndModeMetricDescriptorProvider.END_MODE_METRIC_DESCRIPTOR, true))
        }
        return splittedData
    }
}