package com.agromobile.data.api

import com.agromobile.data.models.*
import retrofit2.Response
import retrofit2.http.*

interface ApiService {
    
    // Autenticación
    @POST("api/mobile/auth")
    suspend fun authenticate(): Response<AuthResponse>
    
    // Cultivos
    @GET("api/cultivos")
    suspend fun getCultivos(): Response<List<CultivoResponse>>
    
    @POST("api/cultivos/guardar")
    suspend fun createCultivo(@Body cultivo: CultivoRequest): Response<ApiResponse<CultivoResponse>>
    
    // Precios
    @GET("api/precios/cultivo/{id}")
    suspend fun getPrecios(@Path("id") cultivoId: Long): Response<PrecioResponse>
    
    // Recomendaciones
    @GET("api/recomendaciones/cultivos/{cultivoId}/recomendar")
    suspend fun getRecomendacion(
        @Path("cultivoId") cultivoId: Long,
        @Query("pregunta") pregunta: String,
        @Query("userContext") userContext: String
    ): Response<RecomendacionResponse>
    
    // Seguimiento
    @GET("api/seguimiento_cultivo")
    suspend fun getSeguimiento(@Query("id") cultivoId: Long): Response<SeguimientoResponse>
} 