package com.agromobile

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.agromobile.data.SessionManager
import com.agromobile.databinding.ActivityMainBinding
import com.agromobile.ui.viewmodels.AuthState
import com.agromobile.ui.viewmodels.AuthViewModel
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import kotlinx.coroutines.launch

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var googleSignInClient: GoogleSignInClient
    
    private val authViewModel: AuthViewModel by viewModels()

    companion object {
        private const val RC_SIGN_IN = 9001
        private const val TAG = "MainActivity"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        
        // Inicializar SessionManager
        SessionManager.init(this)
        
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupGoogleSignIn()
        setupObservers()
        checkInitialAuthState()

        binding.btnSignIn.setOnClickListener {
            signInWithGoogle()
        }
    }

    private fun setupGoogleSignIn() {
        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken(getString(R.string.web_client_id))
            .requestEmail()
            .build()

        googleSignInClient = GoogleSignIn.getClient(this, gso)
    }

    private fun setupObservers() {
        authViewModel.authState.observe(this) { state ->
            when (state) {
                is AuthState.Loading -> {
                    showLoading(true)
                }
                is AuthState.Authenticated -> {
                    showLoading(false)
                    navigateToHome()
                }
                is AuthState.NotAuthenticated -> {
                    showLoading(false)
                    showLoginUI()
                }
                is AuthState.Error -> {
                    showLoading(false)
                    showError(state.message)
                }
            }
        }
    }

    private fun checkInitialAuthState() {
        if (authViewModel.isUserLoggedIn()) {
            // Si ya está autenticado, ir directamente a Home
            navigateToHome()
        } else {
            showLoginUI()
        }
    }

    private fun signInWithGoogle() {
        val signInIntent = googleSignInClient.signInIntent
        startActivityForResult(signInIntent, RC_SIGN_IN)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == RC_SIGN_IN) {
            val task = GoogleSignIn.getSignedInAccountFromIntent(data)
            try {
                val account = task.getResult(ApiException::class.java)
                val idToken = account?.idToken

                if (idToken != null) {
                    // Guardar el token en SessionManager
                    SessionManager.saveAuthToken(idToken)
                    // Autenticar con el backend
                    authenticateWithBackend()
                } else {
                    showError("Error: No se pudo obtener el token de Google")
                }
            } catch (e: ApiException) {
                Log.e(TAG, "Error en Google Sign-In: ${e.message}")
                showError("Error en autenticación de Google: ${e.message}")
            }
        }
    }

    private fun authenticateWithBackend() {
        lifecycleScope.launch {
            authViewModel.authenticate()
        }
    }

    private fun showLoading(show: Boolean) {
        binding.progressBar.visibility = if (show) View.VISIBLE else View.GONE
        binding.btnSignIn.isEnabled = !show
    }

    private fun showLoginUI() {
        binding.progressBar.visibility = View.GONE
        binding.btnSignIn.visibility = View.VISIBLE
        binding.btnSignIn.isEnabled = true
    }

    private fun showError(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_LONG).show()
        Log.e(TAG, "Error: $message")
    }

    private fun navigateToHome() {
        val intent = Intent(this, HomeActivity::class.java)
        startActivity(intent)
        finish()
    }

    override fun onDestroy() {
        super.onDestroy()
        // Limpiar recursos si es necesario
    }
}
