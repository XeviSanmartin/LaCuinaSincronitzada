package cat.montilivi.model

import Ingredient

data class Plat(
    val nom: String,
    val idComanda:Long,
    val ingredients: List<Ingredient>,
    val tempsEmplatat: Long
)
