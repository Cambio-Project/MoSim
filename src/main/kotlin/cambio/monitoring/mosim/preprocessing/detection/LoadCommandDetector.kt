package cambio.monitoring.mosim.preprocessing.detection

import cambio.monitoring.mosim.preprocessing.Command
import cambio.monitoring.mosim.preprocessing.substitution.CommandSubstitutor
import cambio.monitoring.mosim.preprocessing.substitution.LoadCommandSubstitutor

class LoadCommandDetector : Detector {
    private val loadMatcher = "load\\[[^\\[\\]]*]\\[[^\\[\\]]*]".toRegex()

    override fun getCommandType(): Command {
        return Command.LOAD
    }

    override fun detectCommand(text: String): Set<String> {
        return loadMatcher.findAll(text).map { match -> match.value }.toSet()
    }

    override fun getSubstitutor(command: String): CommandSubstitutor {
        return LoadCommandSubstitutor(command)
    }
}