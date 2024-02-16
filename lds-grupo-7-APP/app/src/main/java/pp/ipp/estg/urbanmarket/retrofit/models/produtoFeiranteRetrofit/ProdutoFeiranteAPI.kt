package pp.ipp.estg.urbanmarket.retrofit.models.produtoFeiranteRetrofit

import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Path

interface ProdutoFeiranteAPI {

    @GET("ProdutoFeirante")
    fun getProdutoFeirantes(): Call<List<ProdutoFeirante>>

    @GET("ProdutoFeirante/{id}")
    fun getProdutoFeirante(@Path("id") id: Int): Call<ProdutoFeirante>

    @GET("ProdutoFeirante/Feirante/{id}")
    fun getProdutosByFeirante(@Path("id") id: Int): Call<List<ProdutoFeirante>>

    @POST("ProdutoFeirante")
    fun postProdutoFeirante(@Body produtoFeirante: ProdutoFeirante): Call<ProdutoFeirante>

    @PUT("ProdutoFeirante/{id}")
    fun putProdutoFeirante(@Path("id") id: Int, @Body produtoFeirante: ProdutoFeirante): Call<ProdutoFeirante>

    @DELETE("ProdutoFeirante/{id}")
    fun deleteProdutoFeirante(@Path("id") id: Int): Call<ProdutoFeirante>

}