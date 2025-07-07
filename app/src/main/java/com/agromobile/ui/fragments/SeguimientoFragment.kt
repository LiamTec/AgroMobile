package com.agromobile.ui.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.agromobile.databinding.FragmentSeguimientoBinding

class SeguimientoFragment : Fragment() {

    private var _binding: FragmentSeguimientoBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentSeguimientoBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        
        // TODO: Implementar lógica de seguimiento
        binding.tvSeguimiento.text = "Funcionalidad de Seguimiento en desarrollo..."
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
} 