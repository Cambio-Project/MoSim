package cambio.monitoring.mosim.search

import cambio.monitoring.mosim.search.engine.EventSimulator
import cambio.monitoring.mosim.search.engine.MetricRegistrationStrategy
import cambio.tltea.interpreter.BehaviorInterpretationResult2
import cambio.tltea.interpreter.Interpreter2
import cambio.tltea.interpreter.connector.Brokers

class DefaultSearchInitializer : SearchInitializer {
    override fun prepareSimulator(rawTopNode: List<String>): Pair<EventSimulator, List<BehaviorInterpretationResult2>> {
        val brokers = Brokers()
        val simulator = EventSimulator(brokers)
        brokers.metricBroker.listenerFactory.registrationStrategy = MetricRegistrationStrategy(simulator)
        val results = Interpreter2.interpretAllAsBehavior(rawTopNode, brokers)
        for (result in results) {
            result.activateProcessing()
        }
        return Pair(simulator, results)
    }
}