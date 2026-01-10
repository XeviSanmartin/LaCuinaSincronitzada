package cat.montilivi.model

import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock

class MagatzemDeResultats {
    val platsServits = mutableListOf<TrasaPlat>()
    val comandesServides = mutableListOf<TrasaComanda>()
    val mutex = Mutex()

    suspend fun afegeixPlatAcabat(plat: TrasaPlat){
        mutex.withLock {
            platsServits.add(plat)
        }
    }

    suspend fun afegeixComandaAcabada(comanda: TrasaComanda){
        mutex.withLock {
            comandesServides.add(comanda)
        }
    }
}
