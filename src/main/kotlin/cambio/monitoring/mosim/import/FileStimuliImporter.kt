package cambio.monitoring.mosim.import

import cambio.monitoring.mosim.config.SearchConfiguration
import java.io.FileNotFoundException
import java.nio.charset.StandardCharsets
import java.nio.file.Files
import java.nio.file.Paths
import java.util.Objects

class FileStimuliImporter(private val mtlLoc: String, private val config: SearchConfiguration) : StimuliImporter {

    override fun import(): List<String> {
        Objects.requireNonNull(mtlLoc)
        if (!Paths.get(mtlLoc).toFile().exists()) {
            throw FileNotFoundException("[WARNING] Did not find MTL formula file at $mtlLoc")
        }
        return Files.readAllLines(Paths.get(mtlLoc), StandardCharsets.UTF_8)
    }

}