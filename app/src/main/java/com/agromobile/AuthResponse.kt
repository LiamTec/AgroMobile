package com.agromobile
import retrofit2.Response
import retrofit2.http.Header
import retrofit2.http.POST

data class AuthResponse(
    val success: Boolean,
    val authenticated: Boolean?,
    val userId: String?,
    val email: String?,
    val name: String?,
    val picture: String?,
    val message: String?
)