package cambio.monitoring.mosim.api

import cambio.monitoring.mosim.StimuliSearchOrchestrator
import cambio.monitoring.mosim.analysis.DefaultMetricsAnalyzer
import cambio.monitoring.mosim.config.SearchConfiguration
import cambio.monitoring.mosim.evaluation.DefaultEvaluator
import cambio.monitoring.mosim.export.ConsoleExporter
import cambio.monitoring.mosim.import.CSVDataImporter
import cambio.monitoring.mosim.import.DefaultDataSplitter
import cambio.monitoring.mosim.import.DefaultStimuliParser
import cambio.monitoring.mosim.import.FileStimuliImporter
import cambio.monitoring.mosim.search.DefaultSearchExecutor
import cambio.monitoring.mosim.search.DefaultSearchInitializer
import com.google.common.collect.Multimap
import org.springframework.stereotype.Service
import java.nio.file.Path

@Service
class SearchRunningService {

    fun runSearch(inputFiles: Multimap<String, String>, outPutDir: Path /* TODO: Export */) {
        val monitoringDataPathCollection = inputFiles["monitoring-data"]
        val mtlPathCollection = inputFiles["mtl"]
        if (monitoringDataPathCollection.isEmpty()) {
            throw IllegalArgumentException("You have to provide monitoring data.")
        } else if (mtlPathCollection.isEmpty()) {
            throw IllegalArgumentException("You have to provide mtl formulae.")
        }
        val mtlPath = mtlPathCollection.iterator().next()
        val monitoringDataPath = monitoringDataPathCollection.iterator().next()

        val config = SearchConfiguration()

        val orchestrator = StimuliSearchOrchestrator(
            DefaultMetricsAnalyzer(),
            DefaultDataSplitter(config),
            DefaultSearchInitializer(),
            DefaultSearchExecutor(),
            DefaultStimuliParser(),
            DefaultEvaluator(config),
            ConsoleExporter()
        )

        orchestrator.search(CSVDataImporter(monitoringDataPath), FileStimuliImporter(mtlPath, config))
    }

}