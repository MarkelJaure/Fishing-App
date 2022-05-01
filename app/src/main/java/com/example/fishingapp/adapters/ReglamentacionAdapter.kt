package com.example.fishingapp.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.fishingapp.R
import com.example.fishingapp.models.Reglamentacion

class ReglamentacionAdapter : RecyclerView.Adapter<ReglamentacionAdapter.ReglamentacionViewHolder>() {

    var reglamentaciones = listOf<Reglamentacion.Reglamentacion>()
        set(value) {
            field = value
            notifyDataSetChanged()
        }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ReglamentacionViewHolder {
        val view = LayoutInflater
            .from(parent.context)
            .inflate(R.layout.reglamentacion_item, parent, false)
        return ReglamentacionViewHolder(view)
    }

    override fun onBindViewHolder(holder: ReglamentacionViewHolder, position: Int) {
        holder.bind(reglamentaciones[position])
    }

    class ReglamentacionViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val nombre: TextView = view.findViewById(R.id.reglamentacion_nombre)
        val descripcion: TextView = view.findViewById(R.id.reglamentacion_descripcion)
        val ubicacion: TextView = view.findViewById(R.id.reglamentacion_ubicacion)

        fun bind(reglamentacion: Reglamentacion.Reglamentacion) {
            nombre.text = reglamentacion.nombre
            descripcion.text = reglamentacion.descripcion
            ubicacion.text = reglamentacion.ubicacion
        }
    }

    override fun getItemCount() = reglamentaciones.size
}