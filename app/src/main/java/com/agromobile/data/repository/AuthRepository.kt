package com.agromobile.data.repository

import com.agromobile.data.SessionManager
import com.agromobile.data.api.RetrofitClient
import com.agromobile.data.models.AuthResponse
import com.agromobile.data.models.UserInfo
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class AuthRepository {
    
    suspend fun authenticate(): Result<AuthResponse> = withContext(Dispatchers.IO) {
        try {
            val response = RetrofitClient.apiService.authenticate()
            
            if (response.isSuccessful) {
                val authResponse = response.body()
                if (authResponse?.success == true && authResponse.user != null) {
                    // Guardar información del usuario en la sesión
                    SessionManager.saveUserInfo(authResponse.user)
                    Result.success(authResponse)
                } else {
                    Result.failure(Exception(authResponse?.message ?: "Error de autenticación"))
                }
            } else {
                val errorBody = response.errorBody()?.string()
                Result.failure(Exception(errorBody ?: "Error del servidor: ${response.code()}"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
    
    fun isUserLoggedIn(): Boolean {
        return SessionManager.isLoggedIn()
    }
    
    fun getCurrentUser(): UserInfo? {
        return SessionManager.getUserInfo()
    }
    
    fun logout() {
        SessionManager.clearSession()
    }
} 