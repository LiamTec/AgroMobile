package com.agromobile
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.Header
import retrofit2.http.POST
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query
import retrofit2.http.FieldMap

interface ApiService {
    @POST("api/mobile/auth")
    suspend fun authenticate(
        @Header("Authorization") authorization: String
    ): Response<AuthResponse>

    @POST("api/cultivos/guardar")
    suspend fun guardarCultivo(
        @Header("Authorization") authorization: String,
        @Body cultivo: CultivoRequest
    ): Response<GenericResponse>

    @POST("api/cultivos/guardar")
    suspend fun guardarCultivoRaw(
        @Header("Authorization") authorization: String,
        @Body payload: Map<String, @JvmSuppressWildcards Any>
    ): Response<Any>

    @GET("api/precios/cultivo/{id}")
    suspend fun obtenerPrecios(
        @Header("Authorization") authorization: String,
        @Path("id") id: Long
    ): Response<PrecioResponse>

    @GET("api/recomendaciones/cultivos/{cultivoId}/recomendar")
    suspend fun recomendarCultivo(
        @Header("Authorization") authorization: String,
        @Path("cultivoId") cultivoId: Long,
        @Query("pregunta") pregunta: String,
        @Query("userContext") userContext: String
    ): Response<RecomendacionResponse>

    @GET("api/seguimiento_cultivo")
    suspend fun seguimientoCultivo(
        @Header("Authorization") authorization: String,
        @Query("id") id: Long
    ): Response<SeguimientoResponse>

    @GET("api/cultivos")
    suspend fun obtenerCultivos(
        @Header("Authorization") authorization: String
    ): Response<List<CultivoRequest>>
}

data class GenericResponse(
    val success: Boolean,
    val message: String?
)

data class PrecioResponse(
    val precioFuturoPorKilo: Double,
    val precioActualPorKilo: Double,
    val fechaCosecha: String,
    val id: Long,
    val nombre: String
)

data class RecomendacionResponse(
    val recomendacion: String?
)

data class SeguimientoResponse(
    val estado: String?,
    val error: String?
)