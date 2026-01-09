package cat.montilivi.dades

object generadorIIDD {
    var idComanda:Long = 0
    fun obtenIdComanda():Long {
        idComanda += 1
        return idComanda
    }
}