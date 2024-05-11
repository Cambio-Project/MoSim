package cambio.monitoring.mosim.preprocessing.detection

import cambio.monitoring.mosim.preprocessing.Command
import cambio.monitoring.mosim.preprocessing.substitution.CommandSubstitutor

/**
 * Detects commands inside of MTL formulae and provides a transformation for the command.
 */
interface Detector {
    /**
     * The type command this detector is suitable for.
     */
    fun getCommandType(): Command

    /**
     * Detects command occurrences inside of an MTL formula.
     * Returns each occurrence once.
     * Occurrences can be turned into a transformation using [getSubstitutor].
     */
    fun detectCommand(text: String): Set<String>

    /**
     * Turns a command from text (as detected by this class) into a transformation.
     */
    fun getSubstitutor(command: String): CommandSubstitutor
}