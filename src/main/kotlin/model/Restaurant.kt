package cat.montilivi.model
import cat.montilivi.dades.gestorDeRecursos
import io.github.aakira.napier.LogLevel
import io.github.aakira.napier.Napier
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Deferred
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.delay
import kotlinx.coroutines.joinAll
import kotlinx.coroutines.sync.withPermit
import model.Ingredient
import kotlin.coroutines.CoroutineContext

class Restaurant : CoroutineScope {
    val magatzemDeResultats = MagatzemDeResultats()
    val job = SupervisorJob()
    override val coroutineContext: CoroutineContext
        get() = job + Dispatchers.Default


    suspend fun repComanda(comanda: Comanda) {
        val trasaPlats = mutableListOf<TrasaPlat>()
        val cronometre = System.currentTimeMillis()
        var tempsPreparacioComanda = 0L
        val jobs = mutableListOf<Job>()


            comanda.plats.forEach { plat ->
                val trasaPlat = preparaPlat(plat)
                magatzemDeResultats.afegeixPlatAcabat(trasaPlat)
                tempsPreparacioComanda += trasaPlat.tempsTotalDePreparacio
            }
            magatzemDeResultats.afegeixComandaAcabada(TrasaComanda(comanda.idComanda, tempsPreparacioComanda, trasaPlats))
            jobs.joinAll()


        val resum = "Id de comanda: ${comanda.idComanda}\nPlats de la comanda: \n" +
                trasaPlats.joinToString (separator = "\n") {
                    "\t- ${it.nomPlat}: ${it.tempsTotalDePreparacio} ms"
                }+
                "Temps emprat: ${System.currentTimeMillis() - cronometre} ms"

        Napier.log (priority = LogLevel.INFO,tag = "COMANDA",message =  resum)
    }
    private suspend fun preparaPlat(
        plat: Plat
    ): TrasaPlat {
        val trasaIngredients = mutableListOf<TrasaIngredient>()
        val resultats = mutableListOf<Deferred<TrasaIngredient>>()

        plat.ingredients.forEach { ingredient ->
                resultats.add (
                    async {cuinaIngredient (ingredient) }
                )
            }
            resultats.awaitAll()
            resultats.forEach {trasaIngredients.add(it.await())}

        val platAcabat = emplata(plat = plat, ingredientsCuinats = trasaIngredients)
        return platAcabat
    }

    private suspend fun emplata(plat: Plat, ingredientsCuinats: List<TrasaIngredient>): TrasaPlat
    {
        var cronometre = System.currentTimeMillis()

        Napier.log (priority = LogLevel.VERBOSE,tag = "EMPLATA",message =  "El plat ${plat.nom} demana un cuiner per emplatar.")
        gestorDeRecursos.cuiners.withPermit {
            Napier.log (priority = LogLevel.VERBOSE,tag = "EMPLATA",message =  "El plat ${plat.nom} té un cuiner assignat per emplatar.")
            delay(plat.tempsEmplatat)
        }
        Napier.log (priority = LogLevel.VERBOSE,tag = "EMPLATA",message =  "El plat ${plat.nom} s'ha emplatat.")

        cronometre = System.currentTimeMillis() - cronometre
        cronometre += ingredientsCuinats.sumOf { it.tempsTotal }

        return TrasaPlat(
            nomPlat = plat.nom,
            tempsTotalDePreparacio = cronometre,
            detallIngredients = ingredientsCuinats,
            idComanda = plat.idComanda
        )
    }

    private suspend fun cuinaIngredient (ingredient : Ingredient): TrasaIngredient {
        var cronometre = System.currentTimeMillis()

        Napier.log (priority = LogLevel.VERBOSE,tag = "INGREDIENT",message =  "L'ingredient ${ingredient.nom} demana ${ingredient.recursNecessari}.")
        gestorDeRecursos.obtenRecurs(ingredient.recursNecessari).withPermit {
            Napier.log (priority = LogLevel.VERBOSE,tag = "INGREDIENT",message = "L'ingredient ${ingredient.nom} utilitza ${ingredient.recursNecessari}.")
            Napier.log (priority = LogLevel.VERBOSE,tag = "INGREDIENT",message =  "L'ingredient ${ingredient.nom} demana un cuiner.")
            gestorDeRecursos.cuiners.withPermit  {
                Napier.log (priority = LogLevel.VERBOSE,tag = "INGREDIENT",message =  "L'ingredient ${ingredient.nom} té un cuiner assignat.")
                delay(ingredient.tempsCoccio)
                Napier.log (priority = LogLevel.VERBOSE,tag = "INGREDIENT",message =  "L'ingredient ${ingredient.nom} s'acabat de cuinar." )
            }
            Napier.log (priority = LogLevel.VERBOSE,tag = "INGREDIENT",message =  "L'ingredient ${ingredient.nom} allibera ${ingredient.recursNecessari}.")
        }
        cronometre = System.currentTimeMillis() - cronometre
        Napier.log (priority = LogLevel.VERBOSE,tag = "INGREDIENT",message =  "L'ingredient ${ingredient.nom} ha trigat $cronometre ms a cuinar-se.")
        return TrasaIngredient(
            nomIngredient = ingredient.nom,
            recursUtilitzat = ingredient.recursNecessari,
            tempsTotal = (cronometre)
        )
    }
}