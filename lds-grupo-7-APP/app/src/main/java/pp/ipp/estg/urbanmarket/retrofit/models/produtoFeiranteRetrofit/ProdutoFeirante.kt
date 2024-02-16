package pp.ipp.estg.urbanmarket.retrofit.models.produtoFeiranteRetrofit
data class ProdutoFeirante(
    val id: Int,
    val produtoId: Int,
    val feiranteId: Int,
    val quantidade: Float,
    val precoUni: Float
)
