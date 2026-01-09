package cat.montilivi.model

import cat.montilivi.dades.gestorDeRecursos

data class TrasaIngredient(
    val nomIngredient: String,
    val recursUtilitzat: gestorDeRecursos.TipusDeRecurs,
    val tempsTotal: Long
)
