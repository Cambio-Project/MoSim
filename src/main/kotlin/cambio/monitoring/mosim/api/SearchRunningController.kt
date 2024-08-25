package cambio.monitoring.mosim.api

import cambio.monitoring.mosim.api.util.TempFileUtils
import com.google.common.collect.ArrayListMultimap
import com.google.common.collect.Multimap
import org.apache.tomcat.util.http.fileupload.FileUtils
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController
import org.springframework.web.multipart.MultipartFile
import java.io.IOException
import java.nio.file.Path

@RestController
class SearchRunningController @Autowired constructor(private var simulationRunningService: SearchRunningService) {

    private var logger = LoggerFactory.getLogger(SearchRunningController::class.java)

    @PostMapping("/search/upload")
    @Throws(IOException::class)
            /**
             * For uploading the Multipart files and saving them to the file system. And then we run the search on them.
             */
    fun handleMultipleFilesUpload(
        @RequestParam("mtls") mtls: Array<MultipartFile>,
        @RequestParam("monitoring_data") monitoringData: Array<MultipartFile>,
        @RequestParam("simulation_id") id: String,
        @RequestParam("search_window_size") searchWindowSize: Double
    ): ResponseEntity<String?> {
        return runSearch(mtls, monitoringData, id, searchWindowSize)
    }

    // TODO: Handle this call in a non-blocking manner, taking into account that this implementation is not
    //  client friendly as it can time-out the request due to the long processing time.
    @Throws(IOException::class)
    private fun runSearch(
        mtls: Array<MultipartFile>, monitoringData: Array<MultipartFile>, id: String, searchWindowSize: Double
    ): ResponseEntity<String?> {
        return try {
            if (TempFileUtils.existsSimulationId(id)) {
                return ResponseEntity(
                    String.format(
                        "Simulation ID <%s> is already in use. " + "Please provide a unique new id.", id
                    ), HttpStatus.BAD_REQUEST
                )
            }

            val tmpFolder = prepareTmpFolder()
            val savedFiles = prepareFiles(mtls, monitoringData, tmpFolder)

            simulationRunningService.runSearch(savedFiles, id, searchWindowSize)
            if (!TempFileUtils.existsSimulationId(id)) {
                return ResponseEntity(
                    String.format(
                        "An Error happened when running the search with the ID: " + "%s.", id
                    ), HttpStatus.INTERNAL_SERVER_ERROR
                )
            }
            cleanUp(tmpFolder)
            ResponseEntity<String?>(
                "Files have been successfully uploaded, and the search is running.", HttpStatus.OK
            )
        } catch (e: Exception) {
            val errorMessage = e.message
            logger.error(errorMessage)
            ResponseEntity<String?>(errorMessage, HttpStatus.INTERNAL_SERVER_ERROR)
        }
    }

    private fun prepareTmpFolder(): Path {
        return TempFileUtils.createDefaultTempDir("mosim-")
    }

    private fun prepareFiles(
        mtls: Array<MultipartFile>,
        monitoringData: Array<MultipartFile>, folder: Path
    ): Multimap<String, String> {
        var savedFiles: Multimap<String, String> = ArrayListMultimap.create()
        savedFiles = TempFileUtils.saveFile(savedFiles, "mtl", mtls, folder)
        savedFiles = TempFileUtils.saveFile(savedFiles, "monitoring-data", monitoringData, folder)
        return savedFiles
    }

    private fun cleanUp(folder: Path) {
        FileUtils.deleteDirectory(folder.toFile())
    }

}