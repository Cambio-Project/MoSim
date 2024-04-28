package cambio.monitoring.mosim.import

import cambio.tltea.parser.core.ASTNode

/**
 * Parses the stimuli formulae into an object-oriented representation.
 */
interface StimuliParser {
    fun parse(stimuli: List<String>): List<ASTNode>
}