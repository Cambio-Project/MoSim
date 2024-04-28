package cambio.monitoring.mosim.export

import cambio.tltea.parser.core.temporal.TimeInstance

/**
 * Exports the result in a way specified by the concrete exporter.
 */
interface Exporter {
    fun export(occurrences: List<Pair<TimeInstance, TimeInstance>>)
}