package cat.montilivi.dades

import io.github.aakira.napier.Antilog
import io.github.aakira.napier.LogLevel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.withContext


class LoggerCuina: Antilog() {

    override fun performLog(priority: LogLevel, tag: String?, throwable: Throwable?, message: String?) {
        if (throwable != null) {
            runBlocking {
                withContext(Dispatchers.IO) {
                    println("[${priority.name}] - [${tag ?: "NoTag"}]: ${message ?: ""} ${throwable.localizedMessage ?: ""} - (Context: $coroutineContext - Fil: ${Thread.currentThread().name})")
                    throwable.printStackTrace()
                }
            }
        }
        else {
            runBlocking {
                withContext(Dispatchers.IO) {
                    println("[${priority.name}] - [${tag ?: "NoTag"}]: ${message ?: ""} ${throwable?.localizedMessage ?: ""} - (Context: $coroutineContext -Fil: ${Thread.currentThread().name})")
                }
            }
        }
    }


}