package pp.ipp.estg.urbanmarket.retrofit.models.produtoRetrofit

data class Produto(
    val id: Int,
    val nome: String,
    val linkImagem: String,
    var categoriaProdutoId: Int,
    val unidMedidaId: Int
)