package cat.montilivi.model

open class ErrorCuina (missatge:String):Exception(missatge)
class IngredientCrematException(nomIngredient:String): ErrorCuina("S'ha cremat l'ingredient: $nomIngredient")
class PlatCaigutException(nomPlat: String) : ErrorCuina("$nomPlat ha caigut a terra durant l'emplatat.")
