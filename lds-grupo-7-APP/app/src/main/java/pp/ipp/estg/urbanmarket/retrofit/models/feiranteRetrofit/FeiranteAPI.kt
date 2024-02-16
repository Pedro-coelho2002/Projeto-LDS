package pp.ipp.estg.urbanmarket.retrofit.models.feiranteRetrofit

import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Path

interface FeiranteAPI {

    @GET("Feirante/ByTelem/{telem}")
    fun getFeiranteByTelem(@Path("telem") telem: Int) : Call<Feirante>

    @GET("Feirante/ById/{id}")
    fun getFeirante(@Path("id") id: Int) : Call<Feirante>

    @POST("Feirante")
    fun postFeirante(@Body feirante: Feirante) : Call<Feirante>

    @PUT ("Feirante/{id}")
    fun putFeirante(@Path("id") id: Int, @Body feirante: Feirante) : Call<Feirante>

    @DELETE ("Feirante/{id}")
    fun deleteFeirante(@Path("id") id: Int) : Call<Feirante>

}