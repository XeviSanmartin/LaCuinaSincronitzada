package cat.montilivi.model

import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock

class MagatzemDeResultats() {
    val platsServits = mutableListOf<TrasaPlat>()
    val mutex = Mutex()

    suspend fun afegeixPlatAcabat(plat: TrasaPlat){
        mutex.withLock {
            platsServits.add(plat)
        }
    }
}
