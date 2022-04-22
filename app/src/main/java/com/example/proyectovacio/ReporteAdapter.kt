package com.example.proyectovacio

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class ReporteAdapter: RecyclerView.Adapter<ReporteAdapter.ReporteViewHolder>() {

    var reportes = listOf<Report.Reporte>()
        set(value) {
            field = value
            notifyDataSetChanged()
        }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ReporteViewHolder {
        val view = LayoutInflater
            .from(parent.context)
            .inflate(R.layout.reporte_item, parent, false)
        return ReporteViewHolder(view)
    }

    override fun onBindViewHolder(holder: ReporteViewHolder, position: Int) {
        val reporte = reportes[position]
        holder.bind(reporte)
    }

    class ReporteViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val nombre: TextView = view.findViewById(R.id.reporte_nombre)
        val seleccionUser: TextView = view.findViewById(R.id.reporte_tipoPesca)
        val image: ImageView = view.findViewById(R.id.reporte_image)

        fun bind(reporte: Report.Reporte) {
            nombre.text = reporte.nombre
            seleccionUser.text = reporte.tipoPesca
            image.setImageResource(R.drawable.pesca) //TODO: hardcodeado a imagen de pesca
        }
    }

    override fun getItemCount() = reportes.size

}