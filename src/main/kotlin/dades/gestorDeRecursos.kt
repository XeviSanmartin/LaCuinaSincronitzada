package cat.montilivi.dades

import kotlinx.coroutines.sync.Semaphore


object gestorDeRecursos {
    val fogons = Semaphore(3)
    val forns = Semaphore(1)
    val taulesDeTall = Semaphore(2)
    val microones = Semaphore(2)
    val esferificadors = Semaphore(1)
    val llescadors = Semaphore(3)
    val cuiners = Semaphore(5)

    fun obtenRecurs(eina: TipusDeRecurs): Semaphore {
        return when (eina) {
            TipusDeRecurs.FOGO -> fogons
            TipusDeRecurs.FORN -> forns
            TipusDeRecurs.TAULA -> taulesDeTall
            TipusDeRecurs.ESFERIFICADOR -> esferificadors
            TipusDeRecurs.LLESCADOR -> llescadors
            TipusDeRecurs.MICROONES -> microones
            TipusDeRecurs.CUINER -> cuiners
        }
    }
    enum class TipusDeRecurs {
        FOGO, FORN, TAULA, ESFERIFICADOR, LLESCADOR, MICROONES, CUINER
    }
}