package cambio.monitoring.mosim.import

import cambio.monitoring.mosim.data.Metrics
import cambio.monitoring.mosim.search.engine.EventList

/**
 * Imports monitoring data from a source. Source types are specified by implementing classes.
 */
interface DataImporter {
    fun import(metrics: Metrics): EventList
}