package model

import cat.montilivi.dades.gestorDeRecursos

data class Ingredient(
    val nom: String,
    val tempsCoccio: Long,
    val recursNecessari: gestorDeRecursos.TipusDeRecurs
)