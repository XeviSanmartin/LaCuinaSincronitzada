package cat.montilivi.dades

import io.github.aakira.napier.Antilog
import io.github.aakira.napier.LogLevel


class LoggerCuina: Antilog() {

    override fun performLog(priority: LogLevel, tag: String?, throwable: Throwable?, message: String?) {
        println( "[${priority.name}] - [${tag ?: "NoTag"}]: ${message ?: ""} ${throwable?.localizedMessage ?: ""}")
    }


}