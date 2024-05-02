package cambio.monitoring.mosim.config

import kotlin.random.Random

/**
 * The configuration of the overall search.
 */
data class SearchConfiguration(
    val searchWindowSize: Double = 5.0,
    val searchInterval: Double = 1.0,
    val isDebugOn: Boolean = true,
    val id: String = Random.nextInt().toString()
)