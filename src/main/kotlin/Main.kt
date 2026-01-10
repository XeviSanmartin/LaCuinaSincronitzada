package cat.montilivi

import cat.montilivi.dades.generadorIIDD
import model.Ingredient
import cat.montilivi.dades.gestorDeRecursos
import cat.montilivi.model.Comanda
import cat.montilivi.model.MagatzemDeResultats
import cat.montilivi.model.Plat
import cat.montilivi.model.TrasaComanda
import cat.montilivi.model.TrasaIngredient
import cat.montilivi.model.TrasaPlat
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

    val magatzemDeResultats = MagatzemDeResultats()
    runBlocking {
        gestionaComanda(comanda, magatzemDeResultats)
    }

}
suspend fun cuinaIngredient (ingredient : Ingredient): TrasaIngredient {
    var cronometre = System.currentTimeMillis()

    println("L'ingredient ${ingredient.nom} demana ${ingredient.recursNecessari}.")
    gestorDeRecursos.obtenRecurs(ingredient.recursNecessari).withPermit {
        println("L'ingredient ${ingredient.nom} utilitza ${ingredient.recursNecessari}.")
        println ("L'ingredient ${ingredient.nom} demana un cuiner.")
        gestorDeRecursos.cuiners.withPermit  {
            println ("L'ingredient ${ingredient.nom} té un cuiner assignat.")
            delay(ingredient.tempsCoccio)
            println ("L'ingredient ${ingredient.nom} s'acabat de cuinar." )
       }
        println ("L'ingredient ${ingredient.nom} allibera ${ingredient.recursNecessari}.")
    }
    cronometre = System.currentTimeMillis() - cronometre
    println("L'ingredient ${ingredient.nom} ha trigat $cronometre ms a cuinar-se.")
    return TrasaIngredient(
        nomIngredient = ingredient.nom,
        recursUtilitzat = ingredient.recursNecessari,
        tempsTotal = (cronometre)
    )
}

suspend fun emplata(plat: Plat, ingredientsCuinats: List<TrasaIngredient>): TrasaPlat
{
    var cronometre = System.currentTimeMillis()

    println("El plat ${plat.nom} demana un cuiner per emplatar.")
    gestorDeRecursos.cuiners.withPermit {
        println("El plat ${plat.nom} té un cuiner assignat per emplatar.")
        delay(plat.tempsEmplatat)
    }
    println("El plat ${plat.nom} s'ha emplatat.")

    cronometre = System.currentTimeMillis() - cronometre
    cronometre += ingredientsCuinats.sumOf { it.tempsTotal }

    return TrasaPlat(
        nomPlat = plat.nom,
        tempsTotalDePreparacio = cronometre,
        detallIngredients = ingredientsCuinats,
        idComanda = plat.idComanda
    )
}

suspend fun gestionaComanda(comanda: Comanda, magatzemDeResultats: MagatzemDeResultats) {
    val trasaPlats = mutableListOf<TrasaPlat>()
    val cronometre = System.currentTimeMillis()
    var tempsPreparacioComanda = 0L
    val jobs = mutableListOf<Job>()

    coroutineScope {
        comanda.plats.forEach { plat ->
            val trasaPlat = preparaPlat(plat)
            magatzemDeResultats.afegeixPlatAcabat(trasaPlat)
            tempsPreparacioComanda += trasaPlat.tempsTotalDePreparacio
        }
        magatzemDeResultats.afegeixComandaAcabada(TrasaComanda(comanda.idComanda, tempsPreparacioComanda, trasaPlats))
    }
    jobs.joinAll()
    val resum = "Id de comanda: ${comanda.idComanda}\nPlats de la comanda: \n" +
            trasaPlats.joinToString (separator = "\n") {
                "\t- ${it.nomPlat}: ${it.tempsTotalDePreparacio} ms"
            }+
    "Temps emprat: ${System.currentTimeMillis() - cronometre} ms"

    println(resum)
}

private suspend fun preparaPlat(
    plat: Plat
): TrasaPlat {
    val trasaIngredients = mutableListOf<TrasaIngredient>()

    val resultats = mutableListOf<Deferred<TrasaIngredient>>()
    coroutineScope {
        plat.ingredients.forEach { ingredient ->
            resultats.add ( async {
                cuinaIngredient (ingredient)
            }
            )
        }

        resultats.forEach {trasaIngredients.add(it.await())}

    }
    val platAcabat = emplata(plat = plat, ingredientsCuinats = trasaIngredients)
    return platAcabat
}
