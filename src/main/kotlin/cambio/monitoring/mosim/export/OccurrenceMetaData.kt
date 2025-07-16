package cambio.monitoring.mosim.export

import kotlinx.serialization.Serializable

@Serializable
data class OccurrenceMetaData(
    val id: Int,
    val fileName: String,
    val RelativeStartTime: Double,
    val RelativeEndTime: Double
)