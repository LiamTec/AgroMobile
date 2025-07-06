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

class PreciosFragment : Fragment() {
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_precios, container, false)
        val etId = view.findViewById<EditText>(R.id.etPrecioId)
        val btn = view.findViewById<Button>(R.id.btnConsultarPrecio)
        val tv = view.findViewById<TextView>(R.id.tvPrecioResultado)
        val progressBar = view.findViewById<ProgressBar>(R.id.progressBarPrecio)

        btn.setOnClickListener {
            val id = etId.text.toString().toLongOrNull() ?: 0L
            if (id == 0L) {
                tv.text = "Ingresa el ID del cultivo."
                return@setOnClickListener
            }
            val token = (activity as? HomeActivity)?.getAuthToken() ?: ""
            progressBar.visibility = View.VISIBLE
            btn.isEnabled = false
            lifecycleScope.launch {
                try {
                    val response = RetrofitClient.apiService.obtenerPrecios("Bearer $token", id)
                    if (response.isSuccessful) {
                        val body = response.body()
                        tv.text = "Cultivo: ${body?.nombre}\nActual: ${body?.precioActualPorKilo}\nFuturo: ${body?.precioFuturoPorKilo}\nCosecha: ${body?.fechaCosecha}"
                    } else {
                        tv.text = "Error: ${response.errorBody()?.string() ?: "Error desconocido"}"
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