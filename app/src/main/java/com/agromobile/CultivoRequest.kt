package com.agromobile

data class TipoTerreno(
    val id: Long,
    val nombre: String
)

data class CultivoApiResponse(
    val id: Long,
    val nombre: String,
    val descripcion: String,
    val localidad: String,
    val fechaSiembra: String,
    val tipoTerreno: TipoTerreno,
    val estado: String
)

data class CultivoRequest(
    val nombre: String,
    val descripcion: String,
    val localidad: String,
    val tipoTerrenoId: Long,
    val fechaSiembra: String // yyyy-MM-dd
) 