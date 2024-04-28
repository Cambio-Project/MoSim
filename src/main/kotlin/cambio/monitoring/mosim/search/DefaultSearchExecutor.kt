package cambio.monitoring.mosim.search

import cambio.monitoring.mosim.search.engine.EventList
import cambio.monitoring.mosim.search.engine.EventSimulator

class DefaultSearchExecutor : SearchExecutor {

    override fun execute(simulator: EventSimulator, data: EventList) {
        simulator.events = data
        simulateScenario(simulator)
    }

    private fun simulateScenario(simulator: EventSimulator) {
        while (simulator.hasNext()) {
            simulator.handleNext()
        }
        simulator.triggerEndRound()
        simulator.triggerEndExperiment()
    }
}