package com.agromobile.data.models

data class PrecioInfo(
    val id: Long,
    val nombre: String,
    val precioActualPorKilo: Double,
    val precioFuturoPorKilo: Double,
    val fechaCosecha: String
)

data class PrecioResponse(
    val success: Boolean,
    val data: PrecioInfo? = null,
    val message: String? = null
) 