package pp.ipp.estg.urbanmarket.retrofit.models.feiranteRetrofit

data class Feirante(
    val id: Int,
    val nome: String,
    val email: String,
    val dataNasc: String,
    val sexo: Char,
    val telem: Int,
    val pin: Int
)