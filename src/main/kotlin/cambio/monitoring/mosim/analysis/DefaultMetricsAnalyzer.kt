package cambio.monitoring.mosim.analysis

import cambio.monitoring.mosim.data.Metrics
import cambio.monitoring.mosim.util.MetricUtils
import cambio.tltea.interpreter.connector.value.MetricDescriptor
import cambio.tltea.parser.core.ASTNode
import cambio.tltea.parser.core.ValueASTNode

class DefaultMetricsAnalyzer : MetricsAnalyzer {

    override fun extract(parsedStimuli: List<ASTNode>): Metrics {
        val allMetricDescriptors = mutableListOf<List<MetricDescriptor>>()
        for (parsedStimulus in parsedStimuli) {
            val metricsForFormula = mutableListOf<MetricDescriptor>()
            traverseNode(parsedStimulus, metricsForFormula)
            allMetricDescriptors.add(metricsForFormula)
        }
        return Metrics(allMetricDescriptors)
    }

    private fun traverseNode(node: ASTNode, formulaMetrics: MutableList<MetricDescriptor>) {
        checkNode(node, formulaMetrics)
        traverseChildren(node, formulaMetrics)
    }

    private fun traverseChildren(node: ASTNode, formulaMetrics: MutableList<MetricDescriptor>) {
        for (child in node.children) {
            traverseNode(child, formulaMetrics)
        }
    }

    private fun checkNode(node: ASTNode, metricsForFormula: MutableList<MetricDescriptor>) {
        if (node is ValueASTNode && node.containsPropertyAccess()) {
            if (isBooleanListener(node)) {
                handleBooleanListener(node, metricsForFormula)
            } else
                handleDoubleListener(node, metricsForFormula)
        }
    }


    private fun handleDoubleListener(node: ValueASTNode, metricsForFormula: MutableList<MetricDescriptor>) {
        val descriptor = MetricUtils.convertToDoubleMetric(node)
        metricsForFormula.add(descriptor)
    }

    private fun handleBooleanListener(node: ValueASTNode, metricsForFormula: MutableList<MetricDescriptor>) {
        val descriptor = MetricUtils.convertToBooleanMetric(node)
        metricsForFormula.add(descriptor)
    }


    private fun isBooleanListener(node: ValueASTNode): Boolean {
        return if (node.containsEventName()) {
            node.eventName.contains("b:")
        } else {
            false
        }
    }

}