package cambio.monitoring.mosim.api

import cambio.monitoring.mosim.StimuliSearchOrchestrator
import cambio.monitoring.mosim.analysis.DefaultMetricsAnalyzer
import cambio.monitoring.mosim.config.SearchConfiguration
import cambio.monitoring.mosim.evaluation.DefaultEvaluator
import cambio.monitoring.mosim.export.CSVFileExporter
import cambio.monitoring.mosim.export.MetaDataFileExporter
import cambio.monitoring.mosim.import.CSVDataImporter
import cambio.monitoring.mosim.import.DefaultDataSplitter
import cambio.monitoring.mosim.import.DefaultEndModeDataManipulator
import cambio.monitoring.mosim.import.DefaultStimuliParser
import cambio.monitoring.mosim.import.FileStimuliImporter
import cambio.monitoring.mosim.preprocessing.DefaultCommandPreprocessor
import cambio.monitoring.mosim.search.DefaultSearchExecutor
import cambio.monitoring.mosim.search.DefaultSearchInitializer
import com.google.common.collect.Multimap
import org.springframework.stereotype.Service

@Service
class SearchRunningService {

    fun runSearch(
        inputFiles: Multimap<String, String>,
        id: String,
        searchWindowSize: Double,
        endMode: List<Boolean>? = null
    ) {
        val monitoringDataPathCollection = inputFiles["monitoring-data"]
        val mtlPathCollection = inputFiles["mtl"]
        require(monitoringDataPathCollection.isNotEmpty()) { "You have to provide monitoring data." }
        require(mtlPathCollection.isNotEmpty()) { "You have to provide mtl formulae." }
        if (endMode != null) {
            require(endMode.size == mtlPathCollection.size) { "If you provide end mode, you have to provide the same number of values as mtl formulae." }
        }
        val mtlPath = mtlPathCollection.iterator().next()
        val monitoringDataPath = monitoringDataPathCollection.iterator().next()

        val config = SearchConfiguration(id = id, searchWindowSize = searchWindowSize)

        val orchestrator = StimuliSearchOrchestrator(
            DefaultMetricsAnalyzer(),
            DefaultCommandPreprocessor(),
            DefaultDataSplitter(config),
            DefaultEndModeDataManipulator(),
            DefaultSearchInitializer(),
            DefaultSearchExecutor(),
            DefaultStimuliParser(),
            DefaultEvaluator(config),
            listOf(CSVFileExporter(monitoringDataPath, config), MetaDataFileExporter(config))
        )

        if (hasEndMode(endMode)) {
            if (hasMultipleEndModes(endMode!!) && hasDifferentEndModes(endMode)) {
                orchestrator.search(
                    CSVDataImporter(monitoringDataPath),
                    FileStimuliImporter(mtlPath, config),
                    endMode
                )
            } else {
                orchestrator.search(
                    CSVDataImporter(monitoringDataPath),
                    FileStimuliImporter(mtlPath, config),
                    endMode[0]
                )
            }
        } else {
            orchestrator.search(CSVDataImporter(monitoringDataPath), FileStimuliImporter(mtlPath, config), false)
        }
    }

    private fun hasEndMode(endMode: List<Boolean>?): Boolean {
        return !endMode.isNullOrEmpty()
    }

    private fun hasMultipleEndModes(endMode: List<Boolean>): Boolean {
        return endMode.size > 2
    }

    private fun hasDifferentEndModes(endMode: List<Boolean>): Boolean {
        if (endMode.size < 2) {
            return false
        }
        val firstValue = endMode[0]
        for (value in endMode) {
            if (value != firstValue) {
                return true
            }
        }
        return false
    }

}