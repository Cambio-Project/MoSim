package cambio.monitoring.mosim.import

import cambio.monitoring.mosim.config.SearchConfiguration
import cambio.monitoring.mosim.search.engine.EventList
import cambio.tltea.parser.core.temporal.TimeInstance

class DefaultDataSplitter(private val config: SearchConfiguration) : DataSplitter {
    override fun split(completeList: EventList): List<Pair<TimeInstance, EventList>> {
        val duration = TimeInstance(config.searchWindowSize)
        val step = TimeInstance(config.searchInterval)
        val latestTime = if (completeList.hasNext()) {
            completeList.getLatestTime()
        } else {
            completeList.maxTime
        }

        val latestStart = latestTime.subtract(duration)

        val splitList = mutableListOf<Pair<TimeInstance, EventList>>()
        var currentStart = TimeInstance(0)
        while (currentStart <= latestStart) {
            val adaptedEventList = createSublist(completeList, currentStart, duration)
            splitList.add(Pair(currentStart, adaptedEventList))
            currentStart = currentStart.add(step)
        }

        return splitList
    }

    private fun createSublist(completeList: EventList, start: TimeInstance, duration: TimeInstance): EventList {
        val adaptedEventList = completeList.clone()
        adaptedEventList.sublist(start, duration)
        adaptedEventList.shiftLeft(start)
        return adaptedEventList
    }
}