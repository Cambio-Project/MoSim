import cambio.monitoring.mosim.StimuliSearchOrchestrator
import cambio.monitoring.mosim.analysis.DefaultMetricsAnalyzer
import cambio.monitoring.mosim.api.util.TempFileUtils
import cambio.monitoring.mosim.config.SearchConfiguration
import cambio.monitoring.mosim.evaluation.DefaultEvaluator
import cambio.monitoring.mosim.export.CSVFileExporter
import cambio.monitoring.mosim.import.CSVDataImporter
import cambio.monitoring.mosim.import.DefaultDataSplitter
import cambio.monitoring.mosim.import.DefaultStimuliParser
import cambio.monitoring.mosim.import.FileStimuliImporter
import cambio.monitoring.mosim.preprocessing.DefaultCommandPreprocessor
import cambio.monitoring.mosim.search.DefaultSearchInitializer
import cambio.monitoring.mosim.search.DefaultSearchExecutor
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import java.io.File

class Test {
    private fun test(monitoringLoc: String, mtlLoc: String) {
        val config = SearchConfiguration(id = "test")

        val orchestrator = StimuliSearchOrchestrator(
            DefaultMetricsAnalyzer(),
            DefaultCommandPreprocessor(),
            DefaultDataSplitter(config),
            DefaultSearchInitializer(),
            DefaultSearchExecutor(),
            DefaultStimuliParser(),
            DefaultEvaluator(config),
            CSVFileExporter(monitoringLoc, config)
        )

        orchestrator.search(CSVDataImporter(monitoringLoc), FileStimuliImporter(mtlLoc, config))
    }

    @BeforeEach
    fun cleanUpDirectory() {
        File("search_results/test").deleteRecursively()
    }

    @Test
    fun test() {
        println("Starting MoSIM!")

        val monitoringCSVLoc =
            "src/test/resources/data_example.csv"
        val mtlLoc = "src/test/resources/mtl_example.mtl"
        test(monitoringCSVLoc, mtlLoc)

        println("Shutting Down MoSIM!")
    }

    @Test
    fun emptyTest() {
        println("Starting MoSIM!")

        val monitoringCSVLoc =
            "src/test/resources/data_full_example.csv"
        val mtlLoc = "src/test/resources/mtl_empty.mtl"
        test(monitoringCSVLoc, mtlLoc)

        println("Shutting Down MoSIM!")
    }

    @Test
    fun killCommandTest() {
        println("Starting MoSIM!")

        val monitoringCSVLoc =
            "src/test/resources/data_kill-command_example.csv"
        val mtlLoc = "src/test/resources/mtl_kill-command_example.mtl"
        test(monitoringCSVLoc, mtlLoc)

        println("Shutting Down MoSIM!")
    }


    @Test
    fun loadCommandTest() {
        println("Starting MoSIM!")

        val monitoringCSVLoc =
            "src/test/resources/data_load-command_example.csv"
        val mtlLoc = "src/test/resources/mtl_load-command_example.mtl"
        test(monitoringCSVLoc, mtlLoc)

        println("Shutting Down MoSIM!")
    }

    @Test
    fun loadConstantCommandTest() {
        println("Starting MoSIM!")

        val monitoringCSVLoc =
            "src/test/resources/data_load-constant-command_example.csv"
        val mtlLoc = "src/test/resources/mtl_load-constant-command_example.mtl"
        test(monitoringCSVLoc, mtlLoc)

        println("Shutting Down MoSIM!")
    }

    @Test
    fun loadHookCommandTest() {
        println("Starting MoSIM!")

        val monitoringCSVLoc =
            "src/test/resources/data_hook-command_example.csv"
        val mtlLoc = "src/test/resources/mtl_hook-command_example.mtl"
        test(monitoringCSVLoc, mtlLoc)

        println("Shutting Down MoSIM!")
    }

    @Test
    fun loadMultiMTLTest() {
        println("Starting MoSIM!")

        val monitoringCSVLoc =
            "src/test/resources/data_multi-mtl_example.csv"
        val mtlLoc = "src/test/resources/mtl_multi-mtl_example.mtl"
        test(monitoringCSVLoc, mtlLoc)

        println("Shutting Down MoSIM!")
    }

    @Test
    fun fullMTLTest() {
        println("Starting MoSIM!")

        val monitoringCSVLoc =
            "src/test/resources/data_full_example.csv"
        val mtlLoc = "src/test/resources/mtl_full_example.mtl"
        test(monitoringCSVLoc, mtlLoc)

        println("Shutting Down MoSIM!")
    }

    @Test
    fun testMatcher() {
        val s1 = "(kill[example-service,1]) && (kill[example-service2,1])"
        val s2 = "(kill[example-service,1])"
        val list = listOf(s1, s2)
        println("Input:")
        println(list)
        println("================")

        val substitutor = DefaultCommandPreprocessor()
        substitutor.substitute(list)
    }

}