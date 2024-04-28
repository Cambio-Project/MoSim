package cambio.monitoring.mosim.search.event

import cambio.tltea.interpreter.connector.value.MetricDescriptor

data class DoubleEvent(val descriptor: MetricDescriptor, val value: Double) : Event
