package cambio.monitoring.mosim.preprocessing

import cambio.monitoring.mosim.data.Metrics
import cambio.monitoring.mosim.preprocessing.detection.*
import cambio.monitoring.mosim.preprocessing.substitution.CommandSubstitutor
import cambio.monitoring.mosim.search.engine.EventList
import cambio.tltea.interpreter.connector.value.MetricDescriptor

class DefaultCommandPreprocessor : CommandPreprocessor {
    private val earlyResolveCommandDetectors = mutableMapOf<Command, Detector>()
    private val earlyResolveCommandSubstitutors = mutableListOf<CommandSubstitutor>()
    private val commandDetectors = mutableMapOf<Command, Detector>()
    private val commandSubstitutors = mutableListOf<CommandSubstitutor>()
    private val requiredMetrics = mutableSetOf<MetricDescriptor>()

    init {
        registerEarly(HookWriterCommandDetector())
        register(HookReaderCommandDetector())
        register(KillCommandDetector())
        register(LoadCommandDetector())
    }

    private fun register(detector: Detector) {
        commandDetectors[detector.getCommandType()] = detector
    }

    private fun registerEarly(detector: Detector) {
        earlyResolveCommandDetectors[detector.getCommandType()] = detector
    }

    override fun substitute(stimuli: List<String>): List<String> {
        var alteredStimuli: MutableList<String> = mutableListOf()
        alteredStimuli.addAll(stimuli)
        runDetectors(alteredStimuli, earlyResolveCommandDetectors.values, earlyResolveCommandSubstitutors)
        alteredStimuli = runSubstitutors(alteredStimuli, earlyResolveCommandSubstitutors)
        runDetectors(alteredStimuli, commandDetectors.values, commandSubstitutors)
        alteredStimuli = runSubstitutors(alteredStimuli, commandSubstitutors)
        return alteredStimuli
    }


    private fun runDetectors(
        stimuli: Collection<String>,
        detectors: Collection<Detector>,
        substitutors: MutableList<CommandSubstitutor>
    ) {
        for (detector in detectors) {
            val allMatches = mutableSetOf<String>()
            for (stimulus in stimuli) {
                allMatches.addAll(detector.detectCommand(stimulus))
            }
            for (match in allMatches) {
                val substitutor = detector.getSubstitutor(match)
                substitutors.add(substitutor)
            }
        }
    }

    private fun runSubstitutors(
        alteredStimuli: MutableList<String>,
        substitutors: Collection<CommandSubstitutor>
    ): MutableList<String> {
        for (substitution in substitutors) {
            this.requiredMetrics.addAll(substitution.getRequiredMetrics())

            for (stimulus in alteredStimuli.withIndex()) {
                alteredStimuli[stimulus.index] =
                    stimulus.value.replace(substitution.getCommandText(), substitution.getSubstitutionText())
            }
        }
        return alteredStimuli
    }

    override fun extend(data: EventList, requiredData: EventList) {
        for (substitutor in this.commandSubstitutors) {
            substitutor.computeSubstitutionMetricData(data, requiredData)
        }
        for (substitutor in this.earlyResolveCommandSubstitutors) {
            substitutor.computeSubstitutionMetricData(data, requiredData)
        }
    }

    override fun getRequiredMetrics(): Metrics {
        return Metrics(listOf(requiredMetrics.toList()))
    }

}