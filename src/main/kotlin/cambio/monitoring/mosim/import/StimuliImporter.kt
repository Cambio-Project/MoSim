package cambio.monitoring.mosim.import

/**
 * Imports stimuli formulae from a source. The source type is specified by the concrete implementation.
 */
interface StimuliImporter {
    fun import(): List<String>
}