package com.agromobile

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.ProgressBar
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import kotlinx.coroutines.launch

class RecomendacionFragment : Fragment() {
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_recomendaciones, container, false)
        val etId = view.findViewById<EditText>(R.id.etCultivoId)
        val etPregunta = view.findViewById<EditText>(R.id.etPregunta)
        val etContext = view.findViewById<EditText>(R.id.etUserContext)
        val btn = view.findViewById<Button>(R.id.btnRecomendar)
        val tv = view.findViewById<TextView>(R.id.tvRecomendacionResultado)
        val progressBar = view.findViewById<ProgressBar>(R.id.progressBarRecomendacion)

        btn.setOnClickListener {
            val id = etId.text.toString().toLongOrNull() ?: 0L
            val pregunta = etPregunta.text.toString().trim()
            val contexto = etContext.text.toString().trim()
            if (id == 0L || pregunta.isEmpty() || contexto.isEmpty()) {
                tv.text = "Completa todos los campos."
                return@setOnClickListener
            }
            val token = (activity as? HomeActivity)?.getAuthToken() ?: ""
            progressBar.visibility = View.VISIBLE
            btn.isEnabled = false
            lifecycleScope.launch {
                try {
                    val response = RetrofitClient.apiService.recomendarCultivo("Bearer $token", id, pregunta, contexto)
                    if (response.isSuccessful) {
                        tv.text = response.body()?.recomendacion ?: "Sin recomendación"
                    } else {
                        tv.text = "Error: ${response.body()?.recomendacion ?: "Error desconocido"}"
                    }
                } catch (e: Exception) {
                    tv.text = "Error de red: ${e.message}"
                } finally {
                    progressBar.visibility = View.GONE
                    btn.isEnabled = true
                }
            }
        }
        return view
    }
} 