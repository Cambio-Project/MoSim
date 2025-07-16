package cambio.monitoring.mosim.export

import cambio.monitoring.mosim.api.util.TempFileUtils
import cambio.monitoring.mosim.config.SearchConfiguration
import cambio.tltea.parser.core.temporal.TimeInstance
import kotlinx.serialization.json.Json
import java.io.File
import kotlinx.serialization.encodeToString

class MetaDataFileExporter( private val config: SearchConfiguration) : Exporter {

    override fun export(occurrences: List<Pair<TimeInstance, TimeInstance>>) {
        if (occurrences.isNotEmpty()) {
            TempFileUtils.createOutputDir(TempFileUtils.OUTPUT_DIR, config.id)
        }
        val allMetadata = occurrences.withIndex().map { (index, pair) ->
            val startValue = pair.first.time
            val endValue = pair.second.time
            OccurrenceMetaData(index, "${index}.csv", startValue, endValue)
        }

        writeAllMetaData(allMetadata)
    }

    private fun writeAllMetaData(metadataList: List<OccurrenceMetaData>) {
        val jsonString = Json.encodeToString(metadataList)

        val outputDir = File("${TempFileUtils.OUTPUT_DIR}/${config.id}")
        val outputFile = File(outputDir, "metadata.json")
        outputFile.writeText(jsonString)
    }
}