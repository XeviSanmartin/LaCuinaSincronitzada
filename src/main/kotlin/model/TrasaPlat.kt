package cat.montilivi.model

data class TrasaPlat(
    val nomPlat: String,
    val tempsTotalDePreparacio: Long,
    val detallIngredients: List<TrasaIngredient>,
    val idComanda: Long,
    val cancellat:Boolean = false
)
