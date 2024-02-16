package pp.ipp.estg.urbanmarket.retrofit.models.pedidoRetrofit

data class Pedido(
    val id: Int,
    val produtoId: Int,
    val qtd: Int,
    val idFrom: Int,
    val idTo: Int,
    val aprovado: Boolean
)
