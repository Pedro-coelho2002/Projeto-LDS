package pp.ipp.estg.urbanmarket.retrofit.models.encomendaFeiranteRetrofit

import java.util.Date

data class EncomendaFeirante(
    val id: Int,
    val pedidoId: Int,
    val pago: Boolean,
    val data: Date,
    val valorFinal: Float
)
