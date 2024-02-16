package pp.ipp.estg.urbanmarket.retrofit.models.produtoRetrofit

import com.google.gson.annotations.SerializedName

data class ProdutoRetrofitResponse(
    @SerializedName("\$id") val id: String,
    @SerializedName("\$values") val values: List<Produto>
)