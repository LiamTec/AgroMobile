package com.agromobile.data.repository

import com.agromobile.data.api.RetrofitClient
import com.agromobile.data.models.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class CultivoRepository {
    
    suspend fun getCultivos(): Result<List<CultivoResponse>> = withContext(Dispatchers.IO) {
        try {
            val response = RetrofitClient.apiService.getCultivos()
            
            if (response.isSuccessful) {
                val cultivos = response.body()
                if (cultivos != null) {
                    Result.success(cultivos)
                } else {
                    Result.failure(Exception("No se pudieron obtener los cultivos"))
                }
            } else {
                val errorBody = response.errorBody()?.string()
                Result.failure(Exception(errorBody ?: "Error del servidor: ${response.code()}"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
    
    suspend fun createCultivo(cultivo: CultivoRequest): Result<CultivoResponse> = withContext(Dispatchers.IO) {
        try {
            val response = RetrofitClient.apiService.createCultivo(cultivo)
            
            if (response.isSuccessful) {
                val apiResponse = response.body()
                if (apiResponse?.success == true && apiResponse.data != null) {
                    Result.success(apiResponse.data)
                } else {
                    Result.failure(Exception(apiResponse?.message ?: "Error al crear cultivo"))
                }
            } else {
                val errorBody = response.errorBody()?.string()
                Result.failure(Exception(errorBody ?: "Error del servidor: ${response.code()}"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
} 