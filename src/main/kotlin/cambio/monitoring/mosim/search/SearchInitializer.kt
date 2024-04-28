package cambio.monitoring.mosim.search

import cambio.monitoring.mosim.search.engine.EventSimulator
import cambio.tltea.interpreter.BehaviorInterpretationResult2

/**
 * Initializes a simulator for the search.
 */
interface SearchInitializer {
    fun prepareSimulator(rawTopNode: List<String>): Pair<EventSimulator, List<BehaviorInterpretationResult2>>

}