package com.agromobile.ui.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.agromobile.data.models.CultivoResponse
import com.agromobile.databinding.ItemCultivoBinding
import java.text.SimpleDateFormat
import java.util.*

class CultivoAdapter : ListAdapter<CultivoResponse, CultivoAdapter.CultivoViewHolder>(CultivoDiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CultivoViewHolder {
        val binding = ItemCultivoBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return CultivoViewHolder(binding)
    }

    override fun onBindViewHolder(holder: CultivoViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    class CultivoViewHolder(private val binding: ItemCultivoBinding) : 
        RecyclerView.ViewHolder(binding.root) {

        fun bind(cultivo: CultivoResponse) {
            binding.apply {
                tvNombre.text = cultivo.nombre
                tvTipo.text = cultivo.tipo
                tvArea.text = "${cultivo.area} hectáreas"
                tvUbicacion.text = cultivo.ubicacion
                tvEstado.text = cultivo.estado
                
                // Formatear fecha
                try {
                    val inputFormat = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
                    val outputFormat = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())
                    val date = inputFormat.parse(cultivo.fechaSiembra)
                    tvFechaSiembra.text = outputFormat.format(date!!)
                } catch (e: Exception) {
                    tvFechaSiembra.text = cultivo.fechaSiembra
                }
            }
        }
    }

    private class CultivoDiffCallback : DiffUtil.ItemCallback<CultivoResponse>() {
        override fun areItemsTheSame(oldItem: CultivoResponse, newItem: CultivoResponse): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: CultivoResponse, newItem: CultivoResponse): Boolean {
            return oldItem == newItem
        }
    }
} 