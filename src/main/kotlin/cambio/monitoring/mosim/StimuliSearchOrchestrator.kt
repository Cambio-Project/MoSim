package cambio.monitoring.mosim

import cambio.monitoring.mosim.analysis.MetricsAnalyzer
import cambio.monitoring.mosim.evaluation.Evaluator
import cambio.monitoring.mosim.export.Exporter
import cambio.monitoring.mosim.import.*
import cambio.monitoring.mosim.preprocessing.DefaultCommandPreprocessor
import cambio.monitoring.mosim.search.SearchExecutor
import cambio.monitoring.mosim.search.SearchInitializer
import cambio.tltea.interpreter.BehaviorInterpretationResult2
import cambio.tltea.parser.core.temporal.TimeInstance

class StimuliSearchOrchestrator(
    private val metricsAnalyzer: MetricsAnalyzer,
    private val commandSubstitutor: DefaultCommandPreprocessor,
    private val dataSplitter: DataSplitter,
    private val endModeDataManipulator: EndModeDataManipulator,
    private val searchInitializer: SearchInitializer,
    private val searchExecutor: SearchExecutor,
    private val stimuliParser: StimuliParser,
    private val evaluator: Evaluator,
    private val exporters: List<Exporter>

) {
    fun search(dataImporter: DataImporter, stimuliImporter: StimuliImporter) {
        search(dataImporter, stimuliImporter, false)
    }

    /**
     * Assumption: endModeConfig is a list of booleans - one for each stimulus - indicating whether the end mode should
     * be applied for that stimulus or not. Further, there must be stimuli with and without end mode, otherwise the
     * endMode parameter must be used.
     */
    fun search(dataImporter: DataImporter, stimuliImporter: StimuliImporter, endModeConfig: List<Boolean>) {
        var rawStimuli = stimuliImporter.import()
        rawStimuli = this.commandSubstitutor.substitute(rawStimuli)

        // Split: Create two lists
        val (endTrueStimuli, endFalseStimuli) = partitionStimuli(rawStimuli, endModeConfig)

        // Execute minimal search for both lists
        val endFalseResults = minimalSearch(endFalseStimuli.map { it.stimulus }, dataImporter, false)
        val endTrueResults = minimalSearch(endTrueStimuli.map { it.stimulus }, dataImporter, true)

        // Combine the results - we can simply combine the results for the same time instances,
        // as the same data is used for both searches.
        val mergedResults = mergeResults(endFalseResults, endTrueResults)

        val occurrences = evaluator.evaluate(mergedResults)

        for (exporter in exporters) {
            exporter.export(occurrences)
        }
    }

    fun search(dataImporter: DataImporter, stimuliImporter: StimuliImporter, endMode: Boolean) {
        var rawStimuli = stimuliImporter.import()
        rawStimuli = this.commandSubstitutor.substitute(rawStimuli)

        val results = minimalSearch(rawStimuli, dataImporter, endMode);
        val occurrences = evaluator.evaluate(results)

        for (exporter in exporters) {
            exporter.export(occurrences)
        }
    }

    private fun minimalSearch(
        rawStimuli: List<String>,
        dataImporter: DataImporter,
        endMode: Boolean
    ): List<Pair<TimeInstance, List<BehaviorInterpretationResult2>>> {
        val parsedStimuli = stimuliParser.parse(rawStimuli)
        val metrics = metricsAnalyzer.extract(parsedStimuli)
        val requiredMetrics = this.commandSubstitutor.getRequiredMetrics()
        val data = dataImporter.import(metrics, endMode)
        val requiredData = dataImporter.import(requiredMetrics)
        this.commandSubstitutor.extend(data, requiredData)
        var splitData = dataSplitter.split(data)

        if (endMode) {
            splitData = endModeDataManipulator.manipulate(splitData)
        }

        val results = mutableListOf<Pair<TimeInstance, List<BehaviorInterpretationResult2>>>()
        for (dataSet in splitData) {
            val (simulator, result) = searchInitializer.prepareSimulator(rawStimuli)
            searchExecutor.execute(simulator, dataSet.second)
            results.add(Pair(dataSet.first, result))
        }
        return results;
    }

    data class IndexedStimulus(val index: Int, val stimulus: String)

    fun partitionStimuli(raw: List<String>, flags: List<Boolean>): Pair<List<IndexedStimulus>, List<IndexedStimulus>> {
        val trueList = mutableListOf<IndexedStimulus>()
        val falseList = mutableListOf<IndexedStimulus>()
        for ((idx, stimulus) in raw.withIndex()) {
            if (flags[idx]) {
                trueList.add(IndexedStimulus(idx, stimulus))
            } else {
                falseList.add(IndexedStimulus(idx, stimulus))
            }
        }
        return Pair(trueList, falseList)
    }

    private fun mergeResults(
        endFalseResults: List<Pair<TimeInstance, List<BehaviorInterpretationResult2>>>,
        endTrueResults: List<Pair<TimeInstance, List<BehaviorInterpretationResult2>>>
    ): List<Pair<TimeInstance, List<BehaviorInterpretationResult2>>> {
        val mergedResults = mutableListOf<Pair<TimeInstance, List<BehaviorInterpretationResult2>>>()
        for ((idx, endFalseResult) in endFalseResults.withIndex()) {
            require(endFalseResult.first == endTrueResults[idx].first) {
                "The time instances of the results for end mode and non-end mode do not match. This should not happen, as the same data is used for both searches. Please check the implementation of the data importer and the data splitter."
            }
            val currentResults: MutableList<BehaviorInterpretationResult2> = mutableListOf()
            currentResults.addAll(endFalseResult.second)
            currentResults.addAll(endTrueResults[idx].second)

            mergedResults.add(Pair(endFalseResult.first, currentResults))
        }
        return mergedResults
    }
}