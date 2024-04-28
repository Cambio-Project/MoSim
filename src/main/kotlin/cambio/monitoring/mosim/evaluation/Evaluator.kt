package cambio.monitoring.mosim.evaluation

import cambio.tltea.interpreter.BehaviorInterpretationResult2
import cambio.tltea.parser.core.temporal.TimeInstance

/**
 * Evaluates the results by TlTea to identify monitoring occurrences of the MTL Stimuli.
 */
interface Evaluator {
    /**
     * Lists time intervals (including start and end) for which the search detected all specified stimuli.
     */
    fun evaluate(results: List<Pair<TimeInstance, List<BehaviorInterpretationResult2>>>): List<Pair<TimeInstance, TimeInstance>>

}