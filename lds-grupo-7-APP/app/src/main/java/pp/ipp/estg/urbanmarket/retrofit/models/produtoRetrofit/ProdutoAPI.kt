package pp.ipp.estg.urbanmarket.retrofit.models.produtoRetrofit

import pp.ipp.estg.urbanmarket.retrofit.models.feiranteRetrofit.Feirante
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Path
import retrofit2.http.Query

interface ProdutoAPI {

    @GET("Produto")
    fun getProducts(): Call<List<Produto>>

    @GET("Produto/{id}")
    fun getProduct(@Path("id") id: Int): Call<Produto>

    @GET("Produto/Feirantes/{id}")
    fun getFeirantesByProduct(@Path("id") id: Int): Call<List<Feirante>>

    @POST("Produto")
    fun postProduct(@Body product: Produto): Call<Produto>

    @GET("Produto")
    fun getProductsByCategoria(@Query("categoria_id") categoria_id: Int): Call<List<Produto>>

    @PUT("Produto/{id}")
    fun putProduto(@Path("id") id: Int, @Body produto: Produto): Call<Produto>

    @DELETE("Produto/{id}")
    fun deleteProduto(@Path("id") id: Int): Call<Produto>

}