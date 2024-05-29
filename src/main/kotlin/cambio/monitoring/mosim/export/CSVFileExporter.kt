package cambio.monitoring.mosim.export

import cambio.monitoring.mosim.api.util.TempFileUtils
import cambio.monitoring.mosim.config.SearchConfiguration
import cambio.tltea.parser.core.temporal.TimeInstance
import java.io.BufferedReader
import java.io.BufferedWriter
import java.io.File
import java.io.FileReader

class CSVFileExporter(private val monitoringCSVLoc: String, private val config: SearchConfiguration) : Exporter {
    private val columnSeparator: String = ","
    private val relativeTeamHeader = "time_relative"

    private data class MonitoringData(
        val headers: String,
        val times: MutableList<Double> = mutableListOf(),
        val data: MutableList<String> = mutableListOf()
    )

    override fun export(occurrences: List<Pair<TimeInstance, TimeInstance>>) {
        val monitoringData = readMonitoringData()
        for (occurrence in occurrences.withIndex()) {
            val startValue = occurrence.value.first.time
            val endValue = occurrence.value.second.time
            val (startIndex, endIndex) = findIndices(startValue, endValue, monitoringData)
            val writer = prepareOutputFile(occurrence.index)
            writeMonitoringData(startValue, startIndex, endIndex, monitoringData, writer)
        }
    }

    private fun readMonitoringData(): MonitoringData {
        val br = BufferedReader(FileReader(monitoringCSVLoc))
        var line = br.readLine()
        val monitoringData = MonitoringData(line)
        line = br.readLine()
        while (line != null) {
            val valueInLine = line.split(columnSeparator)
            val time = valueInLine[0].toDouble()
            monitoringData.times.add(time)
            monitoringData.data.add(line)
            line = br.readLine()
        }
        return monitoringData
    }

    private fun findIndices(startValue: Double, endValue: Double, monitoringData: MonitoringData): Pair<Int, Int> {
        var startIndex = -1
        var endIndex = 0
        for (time in monitoringData.times.withIndex()) {
            if (!isIndexSet(startIndex) && time.value >= startValue) {
                startIndex = time.index
            }
            if (isIndexSet(startIndex)) {
                if (time.value <= endValue) {
                    endIndex = time.index
                } else {
                    break
                }
            }
        }
        return Pair(startIndex, endIndex)
    }

    private fun isIndexSet(index: Int): Boolean {
        return index >= 0
    }

    private fun prepareOutputFile(occurrenceId: Int): BufferedWriter {
        val outputDir = TempFileUtils.getOutputDir(TempFileUtils.OUTPUT_DIR, config.id)
        val outputFile = File(outputDir.toFile(), "$occurrenceId.csv")
        outputFile.createNewFile()
        return outputFile.bufferedWriter()
    }

    private fun writeMonitoringData(
        startValue: Double,
        startIndex: Int,
        endIndex: Int,
        monitoringData: MonitoringData,
        writer: BufferedWriter
    ) {
        writer.write(relativeTeamHeader + columnSeparator + monitoringData.headers)
        val endIndexInclusive = (endIndex + 1).coerceAtMost(monitoringData.data.size)
        for (line in monitoringData.data.subList(startIndex, endIndexInclusive)) {
            val valueInLine = line.split(columnSeparator)
            val time = valueInLine[0].toDouble()
            val relativeTime = time - startValue

            writer.newLine()
            writer.write(relativeTime.toString())
            writer.write(columnSeparator)
            writer.write(line)
        }
        writer.close()
    }

}