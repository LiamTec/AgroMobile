package com.agromobile

import android.app.DatePickerDialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textfield.TextInputLayout
import com.google.android.material.button.MaterialButton
import kotlinx.coroutines.launch
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.util.*

class CultivoFragment : Fragment() {
    private val tiposSuelo = listOf("Arcilloso", "Arenoso", "Franco", "Limoso")
    private val tiposSueloId = listOf(1L, 2L, 3L, 4L) // Ajusta estos IDs según tu backend

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_cultivos, container, false)
        val rvCultivos = view.findViewById<RecyclerView>(R.id.rvCultivos)
        val tvSinCultivos = view.findViewById<TextView>(R.id.tvSinCultivos)
        val fabNuevoCultivo = view.findViewById<FloatingActionButton>(R.id.fabNuevoCultivo)

        val cultivoAdapter = CultivoAdapter(emptyList())
        rvCultivos.layoutManager = LinearLayoutManager(requireContext())
        rvCultivos.adapter = cultivoAdapter

        fun mostrarSinCultivos(visible: Boolean) {
            tvSinCultivos.visibility = if (visible) View.VISIBLE else View.GONE
        }

        fun cargarCultivos() {
            mostrarSinCultivos(false)
            lifecycleScope.launch {
                try {
                    val token = (activity as? HomeActivity)?.getAuthToken() ?: ""
                    val response = RetrofitClient.apiService.obtenerCultivos("Bearer $token")
                    if (response.isSuccessful) {
                        val cultivos = response.body() ?: emptyList()
                        cultivoAdapter.updateData(cultivos)
                        mostrarSinCultivos(cultivos.isEmpty())
                    } else {
                        mostrarSinCultivos(true)
                        tvSinCultivos.text = "Error al obtener cultivos"
                    }
                } catch (e: Exception) {
                    mostrarSinCultivos(true)
                    tvSinCultivos.text = "Error de red: ${e.message}"
                }
            }
        }

        cargarCultivos()

        fabNuevoCultivo.setOnClickListener {
            mostrarDialogoNuevoCultivo { cargarCultivos() }
        }

        return view
    }

    private fun mostrarDialogoNuevoCultivo(onCultivoGuardado: () -> Unit) {
        val dialogView = LayoutInflater.from(requireContext()).inflate(R.layout.dialog_nuevo_cultivo, null)
        val etNombre = dialogView.findViewById<TextInputEditText>(R.id.etNombre)
        val etDescripcion = dialogView.findViewById<TextInputEditText>(R.id.etDescripcion)
        val etLocalidad = dialogView.findViewById<TextInputEditText>(R.id.etLocalidad)
        val spinnerTipoSuelo = dialogView.findViewById<Spinner>(R.id.spinnerTipoSuelo)
        val tvFechaSiembra = dialogView.findViewById<TextView>(R.id.tvFechaSiembra)
        val btnGuardar = dialogView.findViewById<MaterialButton>(R.id.btnGuardarCultivo)
        val progressBar = dialogView.findViewById<ProgressBar>(R.id.progressBarNuevoCultivo)

        // Spinner setup
        val adapter = ArrayAdapter(requireContext(), android.R.layout.simple_spinner_item, tiposSuelo)
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        spinnerTipoSuelo.adapter = adapter

        // Fecha de siembra
        var fechaSeleccionada: LocalDate = LocalDate.now()
        val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd")
        tvFechaSiembra.text = "Fecha de siembra: ${fechaSeleccionada.format(formatter)}"
        tvFechaSiembra.setOnClickListener {
            val hoy = Calendar.getInstance()
            val datePicker = DatePickerDialog(requireContext(), { _, year, month, dayOfMonth ->
                fechaSeleccionada = LocalDate.of(year, month + 1, dayOfMonth)
                tvFechaSiembra.text = "Fecha de siembra: ${fechaSeleccionada.format(formatter)}"
            }, hoy.get(Calendar.YEAR), hoy.get(Calendar.MONTH), hoy.get(Calendar.DAY_OF_MONTH))
            datePicker.datePicker.minDate = hoy.timeInMillis
            datePicker.show()
        }

        val alertDialog = android.app.AlertDialog.Builder(requireContext())
            .setTitle("Nuevo Cultivo")
            .setView(dialogView)
            .create()

        btnGuardar.setOnClickListener {
            val nombre = etNombre.text.toString().trim()
            val descripcion = etDescripcion.text.toString().trim()
            val localidad = etLocalidad.text.toString().trim()
            val tipoSueloPos = spinnerTipoSuelo.selectedItemPosition
            val tipoTerrenoId = tiposSueloId.getOrNull(tipoSueloPos) ?: 1L

            if (nombre.isEmpty() || descripcion.isEmpty() || localidad.isEmpty()) {
                Toast.makeText(requireContext(), "Completa todos los campos", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            progressBar.visibility = View.VISIBLE
            btnGuardar.isEnabled = false
            lifecycleScope.launch {
                try {
                    val token = (activity as? HomeActivity)?.getAuthToken() ?: ""
                    val payload = mapOf(
                        "nombre" to nombre,
                        "cultivo" to nombre, // O usa otro campo si corresponde
                        "descripcion" to descripcion,
                        "localidad" to localidad,
                        "tipoTerrenoId" to tipoTerrenoId,
                        "fechaSiembra" to fechaSeleccionada.format(formatter)
                    )
                    val response = RetrofitClient.apiService.guardarCultivoRaw("Bearer $token", payload)
                    if (response.isSuccessful) {
                        Toast.makeText(requireContext(), "Cultivo guardado", Toast.LENGTH_SHORT).show()
                        alertDialog.dismiss()
                        onCultivoGuardado()
                    } else {
                        Toast.makeText(requireContext(), "Error al guardar", Toast.LENGTH_SHORT).show()
                    }
                } catch (e: Exception) {
                    Toast.makeText(requireContext(), "Error de red: ${e.message}", Toast.LENGTH_SHORT).show()
                } finally {
                    progressBar.visibility = View.GONE
                    btnGuardar.isEnabled = true
                }
            }
        }

        alertDialog.show()
    }
} 