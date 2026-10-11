package paper

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
import cambio.monitoring.mosim.search.DefaultSearchInitializer
import cambio.monitoring.mosim.search.DefaultSearchExecutor
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.fail
import org.apache.commons.csv.CSVFormat
import org.apache.commons.csv.CSVParser
import java.io.File
import java.io.FileReader
import java.nio.charset.StandardCharsets

/**
 * Base class for parameterized pattern evaluator tests.
 *
 * Reads a master CSV file where:
 * - First row contains headings
 * - "Applicable?" column: only rows with value 1 are considered
 * - "ID" column: identifier for the test
 * - "Exp. Result" column: 1 = test should return true, 0 = test should return false
 * - "MTL" column: ID to construct MTL filename (format: "master_csv_name"_"MTL ID".mtl)
 * - Data file format: "master_csv_name"_data_"test_id".csv
 *
 * All files are searched in the same directory as the master CSV file.
 * The test runs all applicable tests and fails if any subtest doesn't match expectations.
 * A summary of all failures is printed at the end.
 */
abstract class PatternEvaluatorBase(private val masterCsvPath: String) {
    private data class TestCase(
        val id: String,
        val mtlId: String,
        val expectedResult: Boolean,
        val dataFile: String,
        val mtlFile: String
    )

    private data class TestResult(
        val testCase: TestCase,
        val success: Boolean,
        val actualResult: Boolean? = null,
        val error: Exception? = null
    ) {
        fun getFailureReason(): String = when {
            error != null -> "ERROR: ${error.message}"
            else -> "Expected ${testCase.expectedResult} but got $actualResult"
        }
    }

    companion object {
        /**
         * Delimiter used in the data CSV files (comma). This should not be changed unless MoSIM is also reconfigured.
         */
        private const val DATA_CSV_DELIMITER = ','

        /**
         * This is the delimiter used in the master CSV file to separate columns.
         * It is set to semicolon as this is the export from Excel in some locales.
         * If you change this, make sure to also update the master CSV file accordingly.
         */
        private const val MASTER_CSV_DELIMITER = ';'
        private const val PRINT_SEPARATOR_LENGTH = 80
        private val PRINT_SEPARATOR = "=".repeat(PRINT_SEPARATOR_LENGTH)
        private const val RESULTS_DIR = "search_results/test"
    }

    @BeforeEach
    fun cleanUpDirectory() {
        File(RESULTS_DIR).deleteRecursively()
    }

    @Test
    fun runAllPatternTests() {
        val testCases = loadTestCases()

        if (testCases.isEmpty()) {
            fail("No applicable test cases found in $masterCsvPath")
        }

        printTestHeader(testCases.size)
        val results = executeAllTests(testCases)
        printTestSummary(results)

        val failedTests = results.filter { !it.success }
        if (failedTests.isNotEmpty()) {
            printFailedTests(failedTests)
            fail("${failedTests.size} test(s) failed")
        }
    }

    private fun printTestHeader(totalTests: Int) {
        println(PRINT_SEPARATOR)
        println("Running Pattern Evaluator Tests")
        println("Master CSV: $masterCsvPath")
        println("Total test cases: $totalTests")
        println(PRINT_SEPARATOR)
    }

    private fun executeAllTests(testCases: List<TestCase>): List<TestResult> {
        return testCases.map { testCase ->
            printTestStart(testCase)
            try {
                val actualResult = executeTest(testCase.dataFile, testCase.mtlFile)
                val success = actualResult == testCase.expectedResult
                println("Actual Result: $actualResult")
                println(if (success) "✓ PASS" else "✗ FAIL - Expected ${testCase.expectedResult} but got $actualResult")
                TestResult(testCase, success, actualResult)
            } catch (e: Exception) {
                println("✗ ERROR - ${e.message}")
                TestResult(testCase, false, error = e)
            }
        }
    }

    private fun printTestStart(testCase: TestCase) {
        println("\n--- Running Test: ${testCase.id} ---")
        println("Data File: ${testCase.dataFile}")
        println("MTL File: ${testCase.mtlFile}")
        println("Expected Result: ${testCase.expectedResult}")
    }

    private fun printTestSummary(results: List<TestResult>) {
        println("\n$PRINT_SEPARATOR")
        println("Test Summary")
        println(PRINT_SEPARATOR)

        val passedCount = results.count { it.success }
        println("Passed: $passedCount / ${results.size}")
        println("Failed: ${results.size - passedCount} / ${results.size}")
    }

    private fun printFailedTests(failedTests: List<TestResult>) {
        println("\n--- Failed Tests ---")
        failedTests.forEach { result ->
            println("  • ${result.testCase.id}: ${result.getFailureReason()}")
        }
    }

    private fun loadTestCases(): List<TestCase> {
        val file = File(masterCsvPath)
        if (!file.exists()) {
            fail("Master CSV file not found: $masterCsvPath")
        }

        val baseDir = file.parentFile ?: fail("Invalid master CSV path: $masterCsvPath")
        val fileName = file.nameWithoutExtension

        return FileReader(file, StandardCharsets.UTF_8).use { reader ->
            val format =
                CSVFormat.DEFAULT.builder().setHeader().setSkipHeaderRecord(true).setIgnoreSurroundingSpaces(true)
                    .setDelimiter(MASTER_CSV_DELIMITER)
                    .build()
            CSVParser(reader, format).use { parser ->
                val headerNames = parser.headerMap.keys.toList()
                val requiredColumns = listOf("Applicable?", "ID", "Exp. Result", "MTL")

                for (column in requiredColumns) {
                    if (column !in headerNames) {
                        fail("Missing required column: '$column'")
                    }
                }

                parser.records
                    .filter { it.get("Applicable?") == "1" }
                    .mapNotNull { record -> parseCsvRecord(record, baseDir, fileName) }
            }
        }
    }

    private fun parseCsvRecord(
        record: org.apache.commons.csv.CSVRecord,
        baseDir: File,
        fileName: String
    ): TestCase? {
        val testId = record.get("ID").takeIf { it.isNotBlank() } ?: return null
        val expectedResult = record.get("Exp. Result") == "1"
        val mtlId = record.get("MTL").takeIf { it.isNotBlank() } ?: return null

        val dataFile = "${fileName}_data_${testId}.csv"
        val mtlFile = "${fileName}_${mtlId}.mtl"

        val dataPath = File(baseDir, dataFile)
        val mtlPath = File(baseDir, mtlFile)

        validateFilesExist(dataPath, mtlPath)

        return TestCase(
            id = testId,
            mtlId = mtlId,
            expectedResult = expectedResult,
            dataFile = dataPath.absolutePath,
            mtlFile = mtlPath.absolutePath
        )
    }

    private fun validateFilesExist(dataPath: File, mtlPath: File) {
        if (!dataPath.exists()) fail("Data file not found: ${dataPath.absolutePath}")
        if (!mtlPath.exists()) fail("MTL file not found: ${mtlPath.absolutePath}")
    }

    /**
     * Computes the search window size based on the number of rows in the data CSV file.
     * Window size = (number of data rows) - 1 (excluding header).
     */
    private fun computeSearchWindowSize(dataFilePath: String): Double {
        val file = File(dataFilePath)
        if (!file.exists()) {
            fail("Data file not found: $dataFilePath")
        }
        return FileReader(file, StandardCharsets.UTF_8).use { reader ->
            val format =
                CSVFormat.DEFAULT.builder().setHeader().setSkipHeaderRecord(true).setIgnoreSurroundingSpaces(true)
                    .setDelimiter(DATA_CSV_DELIMITER)
                    .build()
            CSVParser(reader, format).use { parser ->
                (parser.records.size).toDouble()
            }
        }
    }

    /**
     * Helper method to create a search configuration.
     *
     * @param dataFilePath Path to data file. searchWindowSize is computed as (number of rows - 1).
     */
    protected fun createSearchConfiguration(dataFilePath: String): SearchConfiguration {
        val windowSize = computeSearchWindowSize(dataFilePath)
        return SearchConfiguration(id = "test", searchWindowSize = windowSize)
    }

    /**
     * Helper method to create a test orchestrator (can be overridden for custom configuration).
     */
    protected fun createOrchestrator(dataFilePath: String, config: SearchConfiguration): StimuliSearchOrchestrator {
        return StimuliSearchOrchestrator(
            DefaultMetricsAnalyzer(),
            DefaultCommandPreprocessor(),
            DefaultDataSplitter(config),
            DefaultEndModeDataManipulator(),
            DefaultSearchInitializer(),
            DefaultSearchExecutor(),
            DefaultStimuliParser(),
            DefaultEvaluator(config),
            listOf(
                CSVFileExporter(dataFilePath, config),
                MetaDataFileExporter(config)
            )
        )
    }

    /**
     * Execute the actual test with the provided data and MTL files.
     *
     * This is a placeholder method that subclasses must implement to perform
     * the actual test logic using the MTL file and data CSV file.
     *
     * @param dataFilePath Absolute path to the data CSV file
     * @param mtlFilePath Absolute path to the MTL file
     * @return Boolean indicating whether the test passed (true) or failed (false)
     */
    protected fun executeTest(dataFilePath: String, mtlFilePath: String): Boolean {
        val config = createSearchConfiguration(dataFilePath)
        val orchestrator = createOrchestrator(dataFilePath, config)

        try {
            orchestrator.search(
                CSVDataImporter(dataFilePath),
                FileStimuliImporter(mtlFilePath, config),
                endMode = determineEndMode(mtlFilePath)
            )
            cleanUpDirectory() // This is important as it ensures that the test environment is reset after each run
            // If no exception is thrown, the test passed
            return true
        } catch (e: Exception) {
            println("Test execution failed with error: ${e.message}")
            cleanUpDirectory() // This is important as it ensures that the test environment is reset after each run
            // This is the expected error for the test case where the MTL file is missing, so we return false to indicate the test failed as expected
            if (!e.message.equals("search_results\\test\\metadata.json (Das System kann den angegebenen Pfad nicht finden)")) {
                error("Test execution failed with error: ${e.message}")
            }
            return false
        }
    }

    private fun determineEndMode(mtlFilePath: String): Boolean {
        val file = File(mtlFilePath)
        return FileReader(file, StandardCharsets.UTF_8).use { reader ->
            reader.readLines().any() { line -> line.trim().contains("\$b:end") }
        }
    }
}
