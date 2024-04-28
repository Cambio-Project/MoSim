package cambio.monitoring.mosim

import cambio.monitoring.mosim.analysis.MetricsAnalyzer
import cambio.monitoring.mosim.evaluation.Evaluator
import cambio.monitoring.mosim.export.Exporter
import cambio.monitoring.mosim.import.*
import cambio.monitoring.mosim.search.SearchExecutor
import cambio.monitoring.mosim.search.SearchInitializer
import cambio.tltea.interpreter.BehaviorInterpretationResult2
import cambio.tltea.parser.core.temporal.TimeInstance

class StimuliSearchOrchestrator(
    private val metricsAnalyzer: MetricsAnalyzer,
    private val dataSplitter: DataSplitter,
    private val searchInitializer: SearchInitializer,
    private val searchExecutor: SearchExecutor,
    private val stimuliParser: StimuliParser,
    private val evaluator: Evaluator,
    private val exporter: Exporter

) {
    fun search(dataImporter: DataImporter, stimuliImporter: StimuliImporter) {
        val rawStimuli = stimuliImporter.import()
        val parsedStimuli = stimuliParser.parse(rawStimuli)
        val metrics = metricsAnalyzer.extract(parsedStimuli)
        val data = dataImporter.import(metrics)
        val splitData = dataSplitter.split(data)

        val results = mutableListOf<Pair<TimeInstance, List<BehaviorInterpretationResult2>>>()
        for (dataSet in splitData) {
            val (simulator, result) = searchInitializer.prepareSimulator(rawStimuli)
            searchExecutor.execute(simulator, dataSet.second)
            results.add(Pair(dataSet.first, result))
        }

        val occurrences = evaluator.evaluate(results)
        exporter.export(occurrences)
    }
}