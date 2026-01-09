package cat.montilivi.model

import Ingredient

data class Comanda(
    val idComanda: Long,
    val plats: List<Plat>
)
