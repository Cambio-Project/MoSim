package cambio.monitoring.mosim.search.event

import cambio.tltea.interpreter.connector.value.MetricDescriptor

data class BooleanEvent(val descriptor: MetricDescriptor, val value: Boolean) : Event