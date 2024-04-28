package cambio.monitoring.mosim.export

import cambio.tltea.parser.core.temporal.TimeInstance

class ConsoleExporter : Exporter {
    override fun export(occurrences: List<Pair<TimeInstance, TimeInstance>>) {
        println("STIMULI DETECTED:")
        println("====================================:")
        for (occurrence in occurrences) {
            println("${occurrence.first.time} - ${occurrence.second.time}")
        }
        println("====================================:")
    }
}