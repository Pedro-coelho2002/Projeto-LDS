package pp.ipp.estg.urbanmarket.retrofit

import pp.ipp.estg.urbanmarket.retrofit.models.categoriaProdutoRetrofit.CategoriaProdutoAPI
import pp.ipp.estg.urbanmarket.retrofit.models.feiranteRetrofit.FeiranteAPI
import pp.ipp.estg.urbanmarket.retrofit.models.produtoFeiranteRetrofit.ProdutoFeiranteAPI
import pp.ipp.estg.urbanmarket.retrofit.models.produtoRetrofit.ProdutoAPI
import pp.ipp.estg.urbanmarket.retrofit.models.unidadeMedidaRetrofit.UnidadeMedidaAPI
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object RetrofitHelper {

    val baseUrl = "https://urbanmarketapi.azurewebsites.net/api/"

    fun getInstance(): Retrofit {
        return Retrofit.Builder().baseUrl(baseUrl)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }
}

val produtoFeiranteAPI: ProdutoFeiranteAPI = RetrofitHelper.getInstance().create(ProdutoFeiranteAPI::class.java)
val produtoAPI: ProdutoAPI = RetrofitHelper.getInstance().create(ProdutoAPI::class.java)
val feiranteAPI: FeiranteAPI = RetrofitHelper.getInstance().create(FeiranteAPI::class.java)

val unidadeMedidaAPI: UnidadeMedidaAPI = RetrofitHelper.getInstance().create(UnidadeMedidaAPI::class.java)
val categoriaProdutoAPI: CategoriaProdutoAPI = RetrofitHelper.getInstance().create(CategoriaProdutoAPI::class.java)
