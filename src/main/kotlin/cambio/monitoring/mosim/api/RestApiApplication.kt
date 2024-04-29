package cambio.monitoring.mosim.api

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

@SpringBootApplication
open class RestApiApplication

fun main(args: Array<String>) {
    runApplication<RestApiApplication>(*args)
}

