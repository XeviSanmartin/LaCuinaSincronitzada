package cat.montilivi

import cat.montilivi.dades.LoggerCuina
import cat.montilivi.dades.generadorIIDD
import model.Ingredient
import cat.montilivi.dades.gestorDeRecursos
import cat.montilivi.model.Comanda
import cat.montilivi.model.MagatzemDeResultats
import cat.montilivi.model.Plat
import cat.montilivi.model.Restaurant
import cat.montilivi.model.TrasaComanda
import cat.montilivi.model.TrasaIngredient
import cat.montilivi.model.TrasaPlat
import io.github.aakira.napier.LogLevel
import io.github.aakira.napier.Napier
import kotlinx.coroutines.Deferred
import kotlinx.coroutines.Job
import kotlinx.coroutines.async
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.joinAll
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.sync.withPermit

//TIP To <b>Run</b> code, press <shortcut actionId="Run"/> or
// click the <icon src="AllIcons.Actions.Execute"/> icon in the gutter.
fun main() {

    Napier.base(LoggerCuina())

    var comanda = Comanda(
        idComanda = generadorIIDD.obtenIdComanda(),
        plats = emptyList()
    )

    val plat = Plat(
        nom = "Macarrons Gratinats",
        ingredients = listOf(
            Ingredient(
                nom = "Pasta",
                tempsCoccio = 1000,
                recursNecessari = gestorDeRecursos.TipusDeRecurs.FOGO
            ),
            Ingredient(
                nom = "Sofregit",
                tempsCoccio = 1000,
                recursNecessari = gestorDeRecursos.TipusDeRecurs.FOGO
            ),Ingredient(
                nom = "Gratinat",
                tempsCoccio = 2000,
                recursNecessari = gestorDeRecursos.TipusDeRecurs.FORN
            ),
        ),
        tempsEmplatat = 500L,
        idComanda = comanda.idComanda
    )

    comanda  = comanda.copy(plats = listOf(plat))

    val restaurant = Restaurant()
    runBlocking {
        restaurant.repComanda(comanda)
    }

}

