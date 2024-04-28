package cambio.monitoring.mosim.search.engine

import cambio.tltea.interpreter.connector.value.IMetricListener
import cambio.tltea.interpreter.connector.value.IMetricRegistrationStrategy
import cambio.tltea.interpreter.connector.value.MetricDescriptor

/**
 * Registers metric listeners at the event simulator.
 */
class MetricRegistrationStrategy(private val simulator: EventSimulator) : IMetricRegistrationStrategy {
    override fun register(listener: IMetricListener<*>, descriptor: MetricDescriptor) {
        if (descriptor.boolean) {
            simulator.addBooleanListener(descriptor, listener as IMetricListener<Boolean>)
        } else {
            simulator.addDoubleListener(descriptor, listener as IMetricListener<Double>)
        }
    }
}