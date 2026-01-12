package cat.montilivi.model
import cat.montilivi.dades.gestorDeRecursos
import io.github.aakira.napier.LogLevel
import io.github.aakira.napier.Napier
import kotlinx.coroutines.CoroutineName
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Deferred
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.TimeoutCancellationException
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.delay
import kotlinx.coroutines.joinAll
import kotlinx.coroutines.sync.withPermit
import kotlinx.coroutines.withTimeout
import model.Ingredient
import kotlin.coroutines.CoroutineContext
import kotlin.random.Random

class Restaurant : CoroutineScope {
    val magatzemDeResultats = MagatzemDeResultats()
    val job = SupervisorJob()
    override val coroutineContext: CoroutineContext
        get() = job + Dispatchers.Default + CoroutineName ("Corrutina del Restaurant")
    val random = Random
    val percentProbabilitatIngredient = 50
    val percentProbabilitatPlat = 95
    val MAX_INTENTS_INGREDIENT = 3

    suspend fun repComanda(comanda: Comanda) {
        val trasaPlats = mutableListOf<TrasaPlat>()
        val cronometre = System.currentTimeMillis()
        var tempsPreparacioComanda = 0L
        val jobs = mutableListOf<Job>()

        try {


            withTimeout(comanda.tempsEspera)
            {
                comanda.plats.forEach { plat ->
                    val trasaPlat = preparaPlat(plat)
                    magatzemDeResultats.afegeixPlatAcabat(trasaPlat)
                    tempsPreparacioComanda += trasaPlat.tempsTotalDePreparacio
                }
                magatzemDeResultats.afegeixComandaAcabada(
                    TrasaComanda(
                        comanda.idComanda,
                        tempsPreparacioComanda,
                        trasaPlats
                    )
                )
                jobs.joinAll()


                val resum = "Id de comanda: ${comanda.idComanda}\nPlats de la comanda: \n" +
                        trasaPlats.joinToString(separator = "\n") {
                            "\t- ${it.nomPlat}: ${it.tempsTotalDePreparacio} ms"
                        } +
                        "Temps emprat: ${System.currentTimeMillis() - cronometre} ms"

                Napier.log(priority = LogLevel.ASSERT, tag = "COMANDA", message = resum)
            }
        }catch (e: TimeoutCancellationException) {
            Napier.log(priority = LogLevel.ASSERT, tag = "COMANDA", message = "El client de la comanda ${comanda.idComanda} ha marxat abans d'hora per temps d'espera excedit.")
        }
    }
    private suspend fun preparaPlat(
        plat: Plat
    ): TrasaPlat {
        val trasaIngredients = mutableListOf<TrasaIngredient>()
        val resultats = mutableListOf<Deferred<TrasaIngredient>>()
        var platAcabat: TrasaPlat

        try {
            plat.ingredients.forEach { ingredient ->
                resultats.add(
                    async {
                        var intentActual = 0
                        var cuinat = false
                        var trasaIngredient = TrasaIngredient(
                            nomIngredient = ingredient.nom,
                            recursUtilitzat = ingredient.recursNecessari,
                            tempsTotal = 0L
                        )
                        while (intentActual < MAX_INTENTS_INGREDIENT && !cuinat) {
                            try {
                                trasaIngredient = cuinaIngredient(ingredient)
                                cuinat = true
                            } catch (e: IngredientCrematException) {
                                intentActual++
                                Napier.log(
                                    priority = LogLevel.WARNING,
                                    tag = "INGREDIENT",
                                    message = "L'ingredient ${ingredient.nom} s'ha cremat. Reintentant ($intentActual/$MAX_INTENTS_INGREDIENT)."
                                )
                            }
                        }
                        if (!cuinat) {
                            Napier.log(
                                priority = LogLevel.ERROR,
                                tag = "INGREDIENT",
                                message = "L'ingredient ${ingredient.nom} ha fallat definitivament després de $MAX_INTENTS_INGREDIENT intents."
                            )
                            throw IngredientCrematException(ingredient.nom)
                        }
                        trasaIngredient
                    }
                )
            }
            resultats.awaitAll()
            resultats.forEach { trasaIngredients.add(it.await()) }

            platAcabat = emplata(plat = plat, ingredientsCuinats = trasaIngredients)
        }
        catch (e:IngredientCrematException) {
            Napier.log (priority = LogLevel.ERROR,tag = "PLAT",message =  "El plat ${plat.nom} no es pot acabar perquè algun ingredient s'ha cremat.")
            platAcabat = TrasaPlat(plat.nom,0L, emptyList<TrasaIngredient>(), plat.idComanda, cancellat = true)
        }
        catch(e: PlatCaigutException){
            Napier.log (priority = LogLevel.ERROR,tag = "PLAT",message =  "El plat ${plat.nom} no es pot acabar perquè s'ha caigut en l'emplatat.")
            platAcabat = TrasaPlat(plat.nom,0L, trasaIngredients, plat.idComanda, cancellat = true)
        }
        return platAcabat
    }

    private suspend fun emplata(plat: Plat, ingredientsCuinats: List<TrasaIngredient>): TrasaPlat
    {
        var cronometre = System.currentTimeMillis()

        Napier.log (priority = LogLevel.VERBOSE,tag = "EMPLATA",message =  "El plat ${plat.nom} demana un cuiner per emplatar.")
        gestorDeRecursos.cuiners.withPermit {
            Napier.log (priority = LogLevel.VERBOSE,tag = "EMPLATA",message =  "El plat ${plat.nom} té un cuiner assignat per emplatar.")
            delay(plat.tempsEmplatat)
            if (random.nextInt(0,101) < percentProbabilitatPlat)
            {
                throw PlatCaigutException(plat.nom)
            }
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
                if (random.nextInt(0,101) < percentProbabilitatIngredient)
                {
                    throw IngredientCrematException(ingredient.nom)
                }
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