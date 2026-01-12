package cat.montilivi.dades

import cat.montilivi.model.Comanda
import cat.montilivi.model.Plat
import model.Ingredient

val llistaIngredients = listOf(
    // --- FOGONS (6 ingredients) ---
    Ingredient("Brou de pollastre", 3000L, gestorDeRecursos.TipusDeRecurs.FOGO),
    Ingredient("Sofregit de ceba", 1500L, gestorDeRecursos.TipusDeRecurs.FOGO),
    Ingredient("Pasta fresca", 1200L, gestorDeRecursos.TipusDeRecurs.FOGO),
    Ingredient("Filet de vedella", 1000L, gestorDeRecursos.TipusDeRecurs.FOGO),
    Ingredient("Gambes a l'allet", 800L, gestorDeRecursos.TipusDeRecurs.FOGO),
    Ingredient("Salsa bolonyesa", 2500L, gestorDeRecursos.TipusDeRecurs.FOGO),

    // --- FORN (5 ingredients) ---
    Ingredient("Pizza 4 formatges", 3500L, gestorDeRecursos.TipusDeRecurs.FORN),
    Ingredient("Pollastre a l'ast", 4000L, gestorDeRecursos.TipusDeRecurs.FORN),
    Ingredient("Pastís de formatge", 4500L, gestorDeRecursos.TipusDeRecurs.FORN),
    Ingredient("Pa de pagès", 5000L, gestorDeRecursos.TipusDeRecurs.FORN),
    Ingredient("Verdures escalivades", 3000L, gestorDeRecursos.TipusDeRecurs.FORN),

    // --- MICROONES (5 ingredients) ---
    Ingredient("Xocolata fosa", 500L, gestorDeRecursos.TipusDeRecurs.MICROONES),
    Ingredient("Llet calenta", 300L, gestorDeRecursos.TipusDeRecurs.MICROONES),
    Ingredient("Pèsols descongelats", 600L, gestorDeRecursos.TipusDeRecurs.MICROONES),
    Ingredient("Crispetes", 1500L, gestorDeRecursos.TipusDeRecurs.MICROONES),
    Ingredient("Puré instantani", 800L, gestorDeRecursos.TipusDeRecurs.MICROONES),

    // --- TAULA (Preparació manual) (5 ingredients) ---
    Ingredient("Amanida verda", 600L, gestorDeRecursos.TipusDeRecurs.TAULA),
    Ingredient("Tàrtar de tonyina", 1200L, gestorDeRecursos.TipusDeRecurs.TAULA),
    Ingredient("Macedònia de fruites", 900L, gestorDeRecursos.TipusDeRecurs.TAULA),
    Ingredient("Canapès variats", 1000L, gestorDeRecursos.TipusDeRecurs.TAULA),
    Ingredient("Sushi roll", 2000L, gestorDeRecursos.TipusDeRecurs.TAULA),

    // --- LLESCADOR (4 ingredients) ---
    Ingredient("Pernil salat", 400L, gestorDeRecursos.TipusDeRecurs.LLESCADOR),
    Ingredient("Formatge manxego", 400L, gestorDeRecursos.TipusDeRecurs.LLESCADOR),
    Ingredient("Mortadel·la", 300L, gestorDeRecursos.TipusDeRecurs.LLESCADOR),
    Ingredient("Carpaccio de vedella", 500L, gestorDeRecursos.TipusDeRecurs.LLESCADOR),

    // --- ESFERIFICADOR (Cuina molecular) (5 ingredients) ---
    Ingredient("Caviar de meló", 2000L, gestorDeRecursos.TipusDeRecurs.ESFERIFICADOR),
    Ingredient("Perles d'oli d'oliva", 2000L, gestorDeRecursos.TipusDeRecurs.ESFERIFICADOR),
    Ingredient("Esferes de iogurt", 1800L, gestorDeRecursos.TipusDeRecurs.ESFERIFICADOR),
    Ingredient("Perles de vinagre balsàmic", 1900L, gestorDeRecursos.TipusDeRecurs.ESFERIFICADOR),
    Ingredient("Ravioli líquid de mango", 2200L, gestorDeRecursos.TipusDeRecurs.ESFERIFICADOR)
)

// Assegura't que tens 'idComanda' i 'llistaIngredients' definits abans d'això
const val idComanda = 0L
val cartaDePlats = listOf(
    // --- PLATS DE CULLERA I ENTRANTS ---
    Plat("Sopa de la Iaia", idComanda, listOf(llistaIngredients[0], llistaIngredients[2]), 600L),
    Plat("Crema de Pèsols", idComanda, listOf(llistaIngredients[0], llistaIngredients[13], llistaIngredients[15]), 800L),
    Plat("Amanida Cèsar", idComanda, listOf(llistaIngredients[16], llistaIngredients[7], llistaIngredients[22]), 500L),
    Plat("Amanida Molecular", idComanda, listOf(llistaIngredients[16], llistaIngredients[28], llistaIngredients[26]), 800L),
    Plat("Carpaccio amb Parmesà", idComanda, listOf(llistaIngredients[24], llistaIngredients[22], llistaIngredients[26]), 600L),
    Plat("Tàrtar Fusió", idComanda, listOf(llistaIngredients[17], llistaIngredients[25]), 900L),
    Plat("Canapès Ibèrics", idComanda, listOf(llistaIngredients[19], llistaIngredients[21], llistaIngredients[26]), 700L),
    Plat("Canapès de la Casa", idComanda, listOf(llistaIngredients[19], llistaIngredients[23], llistaIngredients[22]), 700L),
    Plat("Sushi Tropical", idComanda, listOf(llistaIngredients[20], llistaIngredients[29]), 1000L),
    Plat("Verdures amb Romesco", idComanda, listOf(llistaIngredients[10], llistaIngredients[5]), 600L),

    // --- PASTA, PIZZA I ARROSSOS ---
    Plat("Macarrons Bolonyesa", idComanda, listOf(llistaIngredients[2], llistaIngredients[5], llistaIngredients[1]), 500L),
    Plat("Lassanya de Carn", idComanda, listOf(llistaIngredients[2], llistaIngredients[5], llistaIngredients[15]), 700L),
    Plat("Pizza 4 Estacions", idComanda, listOf(llistaIngredients[6], llistaIngredients[10], llistaIngredients[21]), 600L),
    Plat("Pizza Margarita", idComanda, listOf(llistaIngredients[6], llistaIngredients[1]), 600L),
    Plat("Pizza Americana", idComanda, listOf(llistaIngredients[6], llistaIngredients[7], llistaIngredients[5]), 700L),
    Plat("Arròs de Muntanya", idComanda, listOf(llistaIngredients[0], llistaIngredients[3], llistaIngredients[1]), 900L),

    // --- CARNS I PEIXOS ---
    Plat("Filet al Pebre", idComanda, listOf(llistaIngredients[3], llistaIngredients[26], llistaIngredients[15]), 800L),
    Plat("Pollastre amb Patates", idComanda, listOf(llistaIngredients[7], llistaIngredients[15]), 600L),
    Plat("Pollastre Mar i Muntanya", idComanda, listOf(llistaIngredients[7], llistaIngredients[4], llistaIngredients[1]), 900L),
    Plat("Gambes a la planxa", idComanda, listOf(llistaIngredients[4], llistaIngredients[26]), 500L),
    Plat("Bistec Rus", idComanda, listOf(llistaIngredients[3], llistaIngredients[17]), 900L),
    Plat("Entrepà de Pernil", idComanda, listOf(llistaIngredients[9], llistaIngredients[21], llistaIngredients[26]), 400L),
    Plat("Entrepà Vegetal", idComanda, listOf(llistaIngredients[9], llistaIngredients[16], llistaIngredients[22]), 400L),
    Plat("Serranito", idComanda, listOf(llistaIngredients[9], llistaIngredients[3], llistaIngredients[21]), 600L),
    Plat("Pollastre farcit", idComanda, listOf(llistaIngredients[7], llistaIngredients[23], llistaIngredients[22]), 1100L),

    // --- POSTRES ---
    Plat("Macedònia Esferificada", idComanda, listOf(llistaIngredients[18], llistaIngredients[29], llistaIngredients[27]), 700L),
    Plat("Pastís de la Casa", idComanda, listOf(llistaIngredients[8], llistaIngredients[11]), 500L),
    Plat("Xocolata amb Crispetes", idComanda, listOf(llistaIngredients[11], llistaIngredients[14]), 300L),
    Plat("Iogurt amb Fruita", idComanda, listOf(llistaIngredients[27], llistaIngredients[18]), 400L),
    Plat("Berenar de l'Àvia", idComanda, listOf(llistaIngredients[9], llistaIngredients[11]), 400L)
)

val llistaComandes = List<Comanda>(30) {

    val nouId = generadorIIDD.obtenIdComanda()
    val numPlats = kotlin.random.Random.nextInt(1, 16)
    val platsAjustats = cartaDePlats.shuffled().take(numPlats).map { platOriginal ->
        platOriginal.copy(idComanda = nouId)
    }

    Comanda(
        idComanda = nouId,
        plats = platsAjustats,
        // Calculem un temps d'espera raonable (ex: 2 segons per plat + 5 segons base)
        tempsEspera = 5000L + (numPlats * 5000L)
    )
}