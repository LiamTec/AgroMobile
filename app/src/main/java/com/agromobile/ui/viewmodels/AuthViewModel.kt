package com.agromobile.ui.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.agromobile.data.models.AuthResponse
import com.agromobile.data.models.UserInfo
import com.agromobile.data.repository.AuthRepository
import kotlinx.coroutines.launch

class AuthViewModel : ViewModel() {
    
    private val authRepository = AuthRepository()
    
    private val _authState = MutableLiveData<AuthState>()
    val authState: LiveData<AuthState> = _authState
    
    private val _currentUser = MutableLiveData<UserInfo?>()
    val currentUser: LiveData<UserInfo?> = _currentUser
    
    init {
        checkAuthState()
    }
    
    private fun checkAuthState() {
        if (authRepository.isUserLoggedIn()) {
            _currentUser.value = authRepository.getCurrentUser()
            _authState.value = AuthState.Authenticated
        } else {
            _authState.value = AuthState.NotAuthenticated
        }
    }
    
    fun authenticate() {
        _authState.value = AuthState.Loading
        
        viewModelScope.launch {
            try {
                val result = authRepository.authenticate()
                result.fold(
                    onSuccess = { authResponse ->
                        _currentUser.value = authResponse.user
                        _authState.value = AuthState.Authenticated
                    },
                    onFailure = { exception ->
                        _authState.value = AuthState.Error(exception.message ?: "Error de autenticación")
                    }
                )
            } catch (e: Exception) {
                _authState.value = AuthState.Error(e.message ?: "Error inesperado")
            }
        }
    }
    
    fun logout() {
        authRepository.logout()
        _currentUser.value = null
        _authState.value = AuthState.NotAuthenticated
    }
}

sealed class AuthState {
    object Loading : AuthState()
    object Authenticated : AuthState()
    object NotAuthenticated : AuthState()
    data class Error(val message: String) : AuthState()
} 