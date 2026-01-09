package cat.montilivi.model

import Ingredient

data class TrasaPlat(
    val nomPlat: String,
    val tempsTotalDePreparacio: Long,
    val detallIngredients: List<TrasaIngredient>,
    val idComanda: Long
)
