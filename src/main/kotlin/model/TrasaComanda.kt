package cat.montilivi.model

data class TrasaComanda(
    val idComanda: Long,
    val tempsTotalDePreparacio: Long,
    val detallPlats: List<TrasaPlat>,
)
