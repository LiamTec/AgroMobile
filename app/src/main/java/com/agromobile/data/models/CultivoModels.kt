package com.agromobile.data.models

data class Cultivo(
    val id: Long? = null,
    val nombre: String,
    val tipo: String,
    val fechaSiembra: String,
    val area: Double,
    val ubicacion: String,
    val estado: String = "ACTIVO"
)

data class CultivoRequest(
    val nombre: String,
    val tipo: String,
    val fechaSiembra: String,
    val area: Double,
    val ubicacion: String
)

data class CultivoResponse(
    val id: Long,
    val nombre: String,
    val tipo: String,
    val fechaSiembra: String,
    val area: Double,
    val ubicacion: String,
    val estado: String,
    val fechaCreacion: String
) 