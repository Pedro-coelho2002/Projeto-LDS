package pp.ipp.estg.urbanmarket.retrofit.models.categoriaProdutoRetrofit

import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Path

interface CategoriaProdutoAPI {

    @GET("CategoriaProduto")
    fun getCategoriaProdutos() : Call<List<CategoriaProduto>>

    @POST("CategoriaProduto")
    fun postCategoriaProdutos(@Body categoriaProduto: CategoriaProduto) : Call<CategoriaProduto>

    @GET("CategoriaProduto/{id}")
    fun getCategoriaProdutos(@Path("id") id: Int) : Call<CategoriaProduto>

    @PUT ("CategoriaProduto/{id}")
    fun putCategoriaProduto(@Path("id") id: Int, @Body categoriaProduto: CategoriaProduto) : Call<CategoriaProduto>

    @DELETE ("CategoriaProduto/{id}")
    fun deleteCategoriaProduto(@Path("id") id: Int) : Call<CategoriaProduto>

}