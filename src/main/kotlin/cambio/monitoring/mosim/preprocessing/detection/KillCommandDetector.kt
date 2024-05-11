package cambio.monitoring.mosim.preprocessing.detection

import cambio.monitoring.mosim.preprocessing.Command
import cambio.monitoring.mosim.preprocessing.substitution.CommandSubstitutor
import cambio.monitoring.mosim.preprocessing.substitution.KillCommandSubstitutor

class KillCommandDetector : Detector {
    private val killMatcher = "kill\\[[^]]*]".toRegex()

    override fun getCommandType(): Command {
        return Command.KILL
    }

    override fun detectCommand(text: String): Set<String> {
        return killMatcher.findAll(text).map { match -> match.value }.toSet()
    }

    override fun getSubstitutor(command: String): CommandSubstitutor {
        return KillCommandSubstitutor(command)
    }
}