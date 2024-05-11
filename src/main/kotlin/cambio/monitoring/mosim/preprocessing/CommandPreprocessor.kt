package cambio.monitoring.mosim.preprocessing

import cambio.monitoring.mosim.data.Metrics
import cambio.monitoring.mosim.search.engine.EventList

interface CommandPreprocessor {
    fun substitute(stimuli: List<String>): List<String>
    fun extend(data: EventList, requiredData: EventList)
    fun getRequiredMetrics(): Metrics
}