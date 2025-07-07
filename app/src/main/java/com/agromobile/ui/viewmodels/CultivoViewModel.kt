package com.agromobile.ui.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.agromobile.data.models.CultivoRequest
import com.agromobile.data.models.CultivoResponse
import com.agromobile.data.repository.CultivoRepository
import kotlinx.coroutines.launch

class CultivoViewModel : ViewModel() {
    
    private val cultivoRepository = CultivoRepository()
    
    private val _cultivos = MutableLiveData<List<CultivoResponse>>()
    val cultivos: LiveData<List<CultivoResponse>> = _cultivos
    
    private val _cultivoState = MutableLiveData<CultivoState>()
    val cultivoState: LiveData<CultivoState> = _cultivoState
    
    fun loadCultivos() {
        _cultivoState.value = CultivoState.Loading
        
        viewModelScope.launch {
            try {
                val result = cultivoRepository.getCultivos()
                result.fold(
                    onSuccess = { cultivosList ->
                        _cultivos.value = cultivosList
                        _cultivoState.value = CultivoState.Success
                    },
                    onFailure = { exception ->
                        _cultivoState.value = CultivoState.Error(exception.message ?: "Error al cargar cultivos")
                    }
                )
            } catch (e: Exception) {
                _cultivoState.value = CultivoState.Error(e.message ?: "Error inesperado")
            }
        }
    }
    
    fun createCultivo(cultivo: CultivoRequest) {
        _cultivoState.value = CultivoState.Loading
        
        viewModelScope.launch {
            try {
                val result = cultivoRepository.createCultivo(cultivo)
                result.fold(
                    onSuccess = { newCultivo ->
                        // Recargar la lista de cultivos
                        loadCultivos()
                        _cultivoState.value = CultivoState.CultivoCreated(newCultivo)
                    },
                    onFailure = { exception ->
                        _cultivoState.value = CultivoState.Error(exception.message ?: "Error al crear cultivo")
                    }
                )
            } catch (e: Exception) {
                _cultivoState.value = CultivoState.Error(e.message ?: "Error inesperado")
            }
        }
    }
}

sealed class CultivoState {
    object Loading : CultivoState()
    object Success : CultivoState()
    data class CultivoCreated(val cultivo: CultivoResponse) : CultivoState()
    data class Error(val message: String) : CultivoState()
} 