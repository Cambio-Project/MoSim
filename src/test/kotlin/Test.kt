import cambio.monitoring.mosim.StimuliSearchOrchestrator
import cambio.monitoring.mosim.analysis.DefaultMetricsAnalyzer
import cambio.monitoring.mosim.config.SearchConfiguration
import cambio.monitoring.mosim.evaluation.DefaultEvaluator
import cambio.monitoring.mosim.export.ConsoleExporter
import cambio.monitoring.mosim.import.CSVDataImporter
import cambio.monitoring.mosim.import.DefaultDataSplitter
import cambio.monitoring.mosim.import.DefaultStimuliParser
import cambio.monitoring.mosim.import.FileStimuliImporter
import cambio.monitoring.mosim.search.DefaultSearchInitializer
import cambio.monitoring.mosim.search.DefaultSearchExecutor
import org.junit.jupiter.api.Test

class Test {

    @Test
    fun test() {
        println("Starting MoSIM!")

        val monitoringCSVLoc =
            "src/test/resources/data_example.csv"
        val mtlLoc = "src/test/resources/mtl_example.mtl"
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

        orchestrator.search(CSVDataImporter(monitoringCSVLoc), FileStimuliImporter(mtlLoc, config))

        println("Shutting Down MoSIM!")
    }
}