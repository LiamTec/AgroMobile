package com.agromobile.data.models

data class SeguimientoInfo(
    val cultivoId: Long,
    val estado: String,
    val progreso: Int,
    val observaciones: String,
    val fechaActualizacion: String
)

data class SeguimientoResponse(
    val success: Boolean,
    val data: SeguimientoInfo? = null,
    val message: String? = null
) 