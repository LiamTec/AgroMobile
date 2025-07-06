package com.agromobile

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.agromobile.databinding.ActivityMainBinding
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import kotlinx.coroutines.launch
import retrofit2.HttpException

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var googleSignInClient: GoogleSignInClient

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupGoogleSignIn()

        binding.btnSignIn.setOnClickListener {
            signInWithGoogle()
        }
    }

    private fun setupGoogleSignIn() {
        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken(getString(R.string.web_client_id)) // Usa tu Client ID
            .requestEmail()
            .build()

        googleSignInClient = GoogleSignIn.getClient(this, gso)
    }

    private fun signInWithGoogle() {
        val signInIntent = googleSignInClient.signInIntent
        startActivityForResult(signInIntent, RC_SIGN_IN)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: android.content.Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == RC_SIGN_IN) {
            val task = GoogleSignIn.getSignedInAccountFromIntent(data)
            try {
                val account = task.getResult(ApiException::class.java)
                val idToken = account?.idToken

                if (idToken != null) {
                    authenticateWithBackend(idToken)
                } else {
                    showMessage("Error: No se pudo obtener el token")
                }
            } catch (e: ApiException) {
                Log.e(TAG, "Error en Google Sign-In: ${e.message}")
                showMessage("Error en autenticación: ${e.message}")
            }
        }
    }

    private fun authenticateWithBackend(idToken: String) {
        lifecycleScope.launch {
            try {
                val response = RetrofitClient.apiService.authenticate("Bearer $idToken")
                if (response.isSuccessful && response.body()?.success == true) {
                    val user = response.body()
                    val welcomeMsg = "¡Bienvenido ${user?.name}!\nEmail: ${user?.email}"
                    showMessage(welcomeMsg)
                    val intent = Intent(this@MainActivity, HomeActivity::class.java)
                    intent.putExtra("AUTH_TOKEN", idToken)
                    intent.putExtra("USER_NAME", user?.name ?: "")
                    intent.putExtra("USER_EMAIL", user?.email ?: "")
                    startActivity(intent)
                    finish()
                } else {
                    val errorMsg = try { response.errorBody()?.string() } catch (e: Exception) { null }
                    val bodyMsg = response.body()?.message
                    showMessage("Error al autenticar: ${bodyMsg ?: errorMsg ?: "Error desconocido"}")
                }
            } catch (e: HttpException) {
                showMessage("Error del servidor: ${e.message}")
            } catch (e: Exception) {
                showMessage("Error de red: ${e.message}")
            }
        }
    }

    private fun showMessage(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_LONG).show()
    }

    companion object {
        private const val RC_SIGN_IN = 9001
        private const val TAG = "MainActivity"
    }
}
