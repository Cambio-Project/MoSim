package cambio.monitoring.mosim.analysis

import cambio.monitoring.mosim.data.Metrics
import cambio.tltea.parser.core.ASTNode

/**
 * Analyzes given formulae and extracts the required metrics.
 */
interface MetricsAnalyzer {

    /**
     * Extracts the required metrics from the top level nodes of stimuli formulae.
     */
    fun extract(parsedStimuli: List<ASTNode>): Metrics

}