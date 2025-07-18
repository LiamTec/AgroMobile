package com.agromobile.ui.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.agromobile.databinding.FragmentRecomendacionesBinding

class RecomendacionFragment : Fragment() {

    private var _binding: FragmentRecomendacionesBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentRecomendacionesBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        
        // TODO: Implementar lógica de recomendaciones
        binding.tvRecomendaciones.text = "Funcionalidad de Recomendaciones en desarrollo..."
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
} 