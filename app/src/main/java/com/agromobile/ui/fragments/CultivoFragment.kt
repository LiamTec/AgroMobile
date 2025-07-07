package com.agromobile.ui.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.viewModels
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.agromobile.data.models.CultivoRequest
import com.agromobile.databinding.FragmentCultivosBinding
import com.agromobile.ui.adapters.CultivoAdapter
import com.agromobile.ui.viewmodels.CultivoState
import com.agromobile.ui.viewmodels.CultivoViewModel
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import kotlinx.coroutines.launch

class CultivoFragment : Fragment() {

    private var _binding: FragmentCultivosBinding? = null
    private val binding get() = _binding!!
    
    private val cultivoViewModel: CultivoViewModel by viewModels()
    private lateinit var cultivoAdapter: CultivoAdapter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentCultivosBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        
        setupRecyclerView()
        setupObservers()
        setupClickListeners()
        
        // Cargar cultivos al iniciar
        cultivoViewModel.loadCultivos()
    }

    private fun setupRecyclerView() {
        cultivoAdapter = CultivoAdapter()
        binding.recyclerViewCultivos.apply {
            layoutManager = LinearLayoutManager(context)
            adapter = cultivoAdapter
        }
    }

    private fun setupObservers() {
        cultivoViewModel.cultivos.observe(viewLifecycleOwner) { cultivos ->
            cultivoAdapter.submitList(cultivos)
            binding.emptyState.visibility = if (cultivos.isEmpty()) View.VISIBLE else View.GONE
        }

        cultivoViewModel.cultivoState.observe(viewLifecycleOwner) { state ->
            when (state) {
                is CultivoState.Loading -> {
                    binding.progressBar.visibility = View.VISIBLE
                }
                is CultivoState.Success -> {
                    binding.progressBar.visibility = View.GONE
                }
                is CultivoState.CultivoCreated -> {
                    binding.progressBar.visibility = View.GONE
                    Toast.makeText(context, "Cultivo creado exitosamente", Toast.LENGTH_SHORT).show()
                }
                is CultivoState.Error -> {
                    binding.progressBar.visibility = View.GONE
                    Toast.makeText(context, state.message, Toast.LENGTH_LONG).show()
                }
            }
        }
    }

    private fun setupClickListeners() {
        binding.fabAddCultivo.setOnClickListener {
            showAddCultivoDialog()
        }
    }

    private fun showAddCultivoDialog() {
        val dialogBinding = layoutInflater.inflate(R.layout.dialog_nuevo_cultivo, null)
        
        MaterialAlertDialogBuilder(requireContext())
            .setTitle("Nuevo Cultivo")
            .setView(dialogBinding)
            .setPositiveButton("Crear") { _, _ ->
                // Obtener datos del formulario
                val nombre = dialogBinding.findViewById<android.widget.EditText>(R.id.etNombre).text.toString()
                val tipo = dialogBinding.findViewById<android.widget.EditText>(R.id.etTipo).text.toString()
                val fechaSiembra = dialogBinding.findViewById<android.widget.EditText>(R.id.etFechaSiembra).text.toString()
                val areaText = dialogBinding.findViewById<android.widget.EditText>(R.id.etArea).text.toString()
                val ubicacion = dialogBinding.findViewById<android.widget.EditText>(R.id.etUbicacion).text.toString()

                if (validateForm(nombre, tipo, fechaSiembra, areaText, ubicacion)) {
                    val area = areaText.toDouble()
                    val cultivo = CultivoRequest(nombre, tipo, fechaSiembra, area, ubicacion)
                    cultivoViewModel.createCultivo(cultivo)
                }
            }
            .setNegativeButton("Cancelar", null)
            .show()
    }

    private fun validateForm(nombre: String, tipo: String, fechaSiembra: String, areaText: String, ubicacion: String): Boolean {
        if (nombre.isBlank()) {
            Toast.makeText(context, "El nombre es requerido", Toast.LENGTH_SHORT).show()
            return false
        }
        if (tipo.isBlank()) {
            Toast.makeText(context, "El tipo es requerido", Toast.LENGTH_SHORT).show()
            return false
        }
        if (fechaSiembra.isBlank()) {
            Toast.makeText(context, "La fecha de siembra es requerida", Toast.LENGTH_SHORT).show()
            return false
        }
        if (areaText.isBlank()) {
            Toast.makeText(context, "El área es requerida", Toast.LENGTH_SHORT).show()
            return false
        }
        try {
            areaText.toDouble()
        } catch (e: NumberFormatException) {
            Toast.makeText(context, "El área debe ser un número válido", Toast.LENGTH_SHORT).show()
            return false
        }
        if (ubicacion.isBlank()) {
            Toast.makeText(context, "La ubicación es requerida", Toast.LENGTH_SHORT).show()
            return false
        }
        return true
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
} 