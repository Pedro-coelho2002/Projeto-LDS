package pp.ipp.estg.urbanmarket.retrofit.models.unidadeMedidaRetrofit

import pp.ipp.estg.urbanmarket.database.unidade.UnidadeMedida
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Path

interface UnidadeMedidaAPI {

    @GET("UnidadeMedida")
    fun getUnidadeMedida(): Call<List<UnidadeMedida>>

    @POST("UnidadeMedida")
    suspend fun postUnidadeMedida(@Body unidadeMedida: UnidadeMedida): Call<UnidadeMedida>?

    @GET("UnidadeMedida/{id}")
    fun getUnidadeMedida(@Path("id") id: Int): Call<UnidadeMedida>

    @PUT("UnidadeMedida/{id}")
    suspend fun putUnidadeMedida(@Path("id") id: Int, @Body unidadeMedida: UnidadeMedida): Call<UnidadeMedida>?

    @DELETE("UnidadeMedida/{id}")
    suspend fun deleteUnidadeMedida(@Path("id") id: Int): Call<UnidadeMedida>?

}