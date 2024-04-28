package cambio.monitoring.mosim.import

import cambio.monitoring.mosim.data.Metrics
import cambio.monitoring.mosim.search.engine.EventList
import cambio.monitoring.mosim.search.event.BooleanEvent
import cambio.monitoring.mosim.search.event.DoubleEvent
import cambio.tltea.interpreter.connector.value.MetricDescriptor
import cambio.tltea.parser.core.temporal.TimeInstance
import java.io.BufferedReader
import java.io.FileReader

/**
 * Imports monitoring data from a csv file.
 *
 * Use one instance per file. Not threat save!
 */
class CSVDataImporter(private val monitoringCSVLoc: String) : DataImporter {
    private val columnSeparator: String = ","

    private var valueColumnsOfInterest: MutableList<Pair<Int, MetricDescriptor>> = mutableListOf()
    private var booleanColumnsOfInterest: MutableList<Pair<Int, MetricDescriptor>> = mutableListOf()
    private var monitoringData = EventList()

    override fun import(metrics: Metrics): EventList {
        initiateState()
        val br = BufferedReader(FileReader(monitoringCSVLoc))
        var line: String? = br.readLine()
        identifyInterestingColumnHeaders(line, metrics)
        line = br.readLine()
        while (line != null) {
            val valueInLine = line.split(columnSeparator)
            addAllDoubleColumnsOfInterest(valueInLine)
            addAllBooleanColumnsOfInterest(valueInLine)
            line = br.readLine()
        }
        return monitoringData
    }

    private fun initiateState() {
        valueColumnsOfInterest = mutableListOf()
        booleanColumnsOfInterest = mutableListOf()
        monitoringData = EventList()
    }

    private fun identifyInterestingColumnHeaders(line: String?, metrics: Metrics) {
        if (line == null) {
            return
        }

        val headers = line.split(columnSeparator)
        for (indexedHeader in headers.withIndex()) {
            val descriptor = findDescriptor(indexedHeader.value, metrics)
            if (descriptor != null) {
                if (descriptor.boolean) {
                    booleanColumnsOfInterest.add(Pair(indexedHeader.index, descriptor))
                } else {
                    valueColumnsOfInterest.add(Pair(indexedHeader.index, descriptor))
                }
            }
        }

    }

    private fun findDescriptor(metricName: String, metrics: Metrics): MetricDescriptor? {
        for (metric in metrics.allMetrics) {
            if (isSameMetric(metricName, metric)) {
                return metric
            }
        }
        return null
    }

    private fun isSameMetric(metricName: String, metric: MetricDescriptor): Boolean {
        return metric.metricIdentifier.trim().lowercase() == metricName.trim().lowercase()
    }

    private fun toTimeInstance(time: String): TimeInstance {
        return TimeInstance(time.toDouble())
    }

    private fun addAllDoubleColumnsOfInterest(line: List<String>) {
        for (column in valueColumnsOfInterest) {
            monitoringData.addEvent(toTimeInstance(line[0]), toDoubleEvent(line[column.first], column.second))
        }
    }

    private fun addAllBooleanColumnsOfInterest(line: List<String>) {
        for (column in booleanColumnsOfInterest) {
            monitoringData.addEvent(toTimeInstance(line[0]), toBooleanEvent(line[column.first], column.second))
        }
    }

    private fun toDoubleEvent(event: String, metricDescriptor: MetricDescriptor): DoubleEvent {
        return DoubleEvent(metricDescriptor, event.toDouble())
    }

    private fun toBooleanEvent(event: String, metricDescriptor: MetricDescriptor): BooleanEvent {
        return BooleanEvent(metricDescriptor, event.toBoolean())
    }

}