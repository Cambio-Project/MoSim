package cambio.monitoring.mosim.preprocessing.detection

import cambio.monitoring.mosim.preprocessing.*
import cambio.monitoring.mosim.preprocessing.substitution.CommandSubstitutor
import cambio.monitoring.mosim.preprocessing.substitution.HookWriterCommandSubstitutor

class HookWriterCommandDetector : Detector {
    private val hookWriterMatcher = "\\(kill\\[[^]]*]\\)\\s*[&∧]\\s*\\(event\\[[^]]*]\\)".toRegex()

    override fun getCommandType(): Command {
        return Command.HOOK_WRITE
    }

    override fun detectCommand(text: String): Set<String> {
        return hookWriterMatcher.findAll(text).map { match -> match.value }.toSet()
    }

    override fun getSubstitutor(command: String): CommandSubstitutor {
        return HookWriterCommandSubstitutor(command)
    }
}