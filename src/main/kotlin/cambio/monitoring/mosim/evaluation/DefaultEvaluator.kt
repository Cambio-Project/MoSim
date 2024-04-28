package cambio.monitoring.mosim.evaluation

import cambio.monitoring.mosim.config.SearchConfiguration
import cambio.tltea.interpreter.BehaviorInterpretationResult2
import cambio.tltea.parser.core.temporal.TimeInstance

class DefaultEvaluator(private val config: SearchConfiguration) : Evaluator {
    private val zeroTime = TimeInstance(0)

    override fun evaluate(results: List<Pair<TimeInstance, List<BehaviorInterpretationResult2>>>): List<Pair<TimeInstance, TimeInstance>> {
        val evaluationResults = mutableListOf<Pair<TimeInstance, TimeInstance>>()
        val duration = TimeInstance(config.searchWindowSize)

        for (result in results) {
            val allFormulaSatisfaction = getAllFormulaeSatisfaction(result.second)
            if (allFormulaSatisfaction) {
                val startTime = result.first
                val endTime = startTime.add(duration)
                evaluationResults.add(Pair(startTime, endTime))
            }
        }

        return evaluationResults
    }

    private fun getAllFormulaeSatisfaction(result: List<BehaviorInterpretationResult2>): Boolean {
        for (formulaResult in result) {
            val formulaSatisfaction = formulaResult.treeDescription.causeASTRoot.getNodeLogic().getState(zeroTime)
            if (!formulaSatisfaction) {
                return false
            }
        }
        return true
    }
}