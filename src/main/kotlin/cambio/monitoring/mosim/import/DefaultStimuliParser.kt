package cambio.monitoring.mosim.import

import cambio.tltea.parser.core.ASTNode
import cambio.tltea.parser.mtl.generated.MTLParser

class DefaultStimuliParser : StimuliParser {
    override fun parse(stimuli: List<String>): List<ASTNode> {
        val parsedRootNodes = mutableListOf<ASTNode>()
        for (stimulus in stimuli) {
            val parsedStimulus = MTLParser.parse(stimulus)
            parsedRootNodes.add(parsedStimulus)
        }
        return parsedRootNodes
    }
}