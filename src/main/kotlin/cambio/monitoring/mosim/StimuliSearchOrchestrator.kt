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
    private val searchInitializer: SearchInitializer,
    private val searchExecutor: SearchExecutor,
    private val stimuliParser: StimuliParser,
    private val evaluator: Evaluator,
    private val exporters: List<Exporter>

) {
    fun search(dataImporter: DataImporter, stimuliImporter: StimuliImporter) {
        var rawStimuli = stimuliImporter.import()
        rawStimuli = this.commandSubstitutor.substitute(rawStimuli)
        val parsedStimuli = stimuliParser.parse(rawStimuli)
        val metrics = metricsAnalyzer.extract(parsedStimuli)
        val requiredMetrics = this.commandSubstitutor.getRequiredMetrics()
        val data = dataImporter.import(metrics)
        val requiredData = dataImporter.import(requiredMetrics)
        this.commandSubstitutor.extend(data, requiredData)
        val splitData = dataSplitter.split(data)

        val results = mutableListOf<Pair<TimeInstance, List<BehaviorInterpretationResult2>>>()
        for (dataSet in splitData) {
            val (simulator, result) = searchInitializer.prepareSimulator(rawStimuli)
            searchExecutor.execute(simulator, dataSet.second)
            results.add(Pair(dataSet.first, result))
        }

        val occurrences = evaluator.evaluate(results)

        for(exporter in exporters){
            exporter.export(occurrences)
        }
    }
}