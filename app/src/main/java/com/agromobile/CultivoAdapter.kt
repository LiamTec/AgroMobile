package com.agromobile

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class CultivoAdapter(private var cultivos: List<CultivoRequest>) : RecyclerView.Adapter<CultivoAdapter.CultivoViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CultivoViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_cultivo, parent, false)
        return CultivoViewHolder(view)
    }

    override fun onBindViewHolder(holder: CultivoViewHolder, position: Int) {
        val cultivo = cultivos[position]
        holder.tvNombre.text = cultivo.nombre
        holder.tvTipo.text = cultivo.tipoTerreno
    }

    override fun getItemCount(): Int = cultivos.size

    fun updateData(newCultivos: List<CultivoRequest>) {
        cultivos = newCultivos
        notifyDataSetChanged()
    }

    class CultivoViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val tvNombre: TextView = itemView.findViewById(R.id.tvNombreCultivo)
        val tvTipo: TextView = itemView.findViewById(R.id.tvTipoTerreno)
    }
} 