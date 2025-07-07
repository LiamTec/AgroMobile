package com.agromobile.data.models

data class RecomendacionRequest(
    val cultivoId: Long,
    val pregunta: String,
    val userContext: String
)

data class RecomendacionInfo(
    val recomendacion: String,
    val fechaGeneracion: String
)

data class RecomendacionResponse(
    val success: Boolean,
    val data: RecomendacionInfo? = null,
    val message: String? = null
) 