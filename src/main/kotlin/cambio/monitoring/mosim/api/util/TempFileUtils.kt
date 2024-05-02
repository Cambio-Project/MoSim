package cambio.monitoring.mosim.api.util

import com.google.common.collect.Multimap
import org.springframework.util.StringUtils
import org.springframework.web.multipart.MaxUploadSizeExceededException
import org.springframework.web.multipart.MultipartFile
import java.io.IOException
import java.nio.file.FileSystems
import java.nio.file.Files
import java.nio.file.Path
import java.util.*
import java.util.function.Consumer

class TempFileUtils {
    companion object {
        const val OUTPUT_DIR = "search_results"
        private val SEPARATOR: String = FileSystems.getDefault().separator

        @Throws(
            IOException::
            class
        )
        /**
         * Creates a new file and write the given content to it.
         */
        private fun createFile(tmpDir: Path, originalName: String, content: ByteArray): Path {
            val filePath = tmpDir.toString() + SEPARATOR + originalName
            val file = Files.createFile(Path.of(filePath))
            return Files.write(file, content)
        }

        @Throws(Exception::class)
        private fun saveFile(file: MultipartFile, path: Path): Path {
            requireNotNull(file.originalFilename) {
                "No file detected"
            }
            val fileName = StringUtils.cleanPath(file.originalFilename)
            return try {
                if (fileName.contains("..")) {
                    throw Exception("Filename contains invalid path sequence: $fileName")
                } else if (file.isEmpty) {
                    throw Exception(String.format("The uploaded file <%s> is empty.", fileName))
                }
                val content = file.bytes
                createFile(path, fileName, content)
            } catch (e: MaxUploadSizeExceededException) {
                throw MaxUploadSizeExceededException(file.size)
            }
        }

        fun saveFile(
            filesPaths: Multimap<String, String>, type: String?,
            files: Array<MultipartFile>?,
            temDir: Path
        ): Multimap<String, String> {
            if (files != null) {
                listOf(*files).forEach(Consumer { file: MultipartFile? ->
                    try {
                        val tmpFile = saveFile(file!!, temDir)
                        val filePath = tmpFile.toString()
                        filesPaths.put(type, filePath)
                    } catch (e: Exception) {
                        throw RuntimeException(e)
                    }
                })
            }
            return filesPaths
        }

        // Create a temp directory in the default OS's /tmp folder.
        @Throws(IOException::class)
        fun createDefaultTempDir(prefix: String?): Path {
            return Files.createTempDirectory(prefix)
        }

        @Throws(IOException::class)
        fun createOutputDir(outputDirName: String, simulationId: String): Path {
            val outPutDirPath = Path.of(outputDirName)
            if (!Files.exists(outPutDirPath)) {
                Files.createDirectory(outPutDirPath)
            }
            val simulationOutputDirPath = getOutputDir(outputDirName, simulationId)
            return Files.createDirectory(simulationOutputDirPath)
        }

        fun getOutputDir(outputDirName: String, simulationId: String): Path {
            return Path.of(outputDirName + SEPARATOR + simulationId)
        }

        fun existsSimulationId(simulationId: String): Boolean {
            val simulationOutputDirPath = OUTPUT_DIR + SEPARATOR + simulationId
            return Files.exists(Path.of(simulationOutputDirPath))
        }
    }
}