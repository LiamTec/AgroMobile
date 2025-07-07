package com.agromobile.data.models

data class AuthRequest(
    val idToken: String
)

data class AuthResponse(
    val success: Boolean,
    val message: String? = null,
    val user: UserInfo? = null
)

data class UserInfo(
    val id: String,
    val name: String,
    val email: String,
    val googleId: String
)

data class ApiResponse<T>(
    val success: Boolean,
    val data: T? = null,
    val message: String? = null,
    val error: String? = null
) 