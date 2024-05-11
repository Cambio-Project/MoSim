package cambio.monitoring.mosim.preprocessing.detection

import cambio.monitoring.mosim.preprocessing.*
import cambio.monitoring.mosim.preprocessing.substitution.CommandSubstitutor
import cambio.monitoring.mosim.preprocessing.substitution.HookReaderCommandSubstitutor

class HookReaderCommandDetector : Detector {
    private val hookReaderMatcher = "event\\[[^]]*]".toRegex()

    override fun getCommandType(): Command {
        return Command.HOOK_READ
    }

    override fun detectCommand(text: String): Set<String> {
        return hookReaderMatcher.findAll(text).map { match -> match.value }.toSet()
    }

    override fun getSubstitutor(command: String): CommandSubstitutor {
        return HookReaderCommandSubstitutor(command)
    }
}