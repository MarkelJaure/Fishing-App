package com.example.fishingapp.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.fishingapp.R
import com.example.fishingapp.models.Reporte

class ReporteAdapter(private val onClick: (Reporte) -> Unit) : RecyclerView.Adapter<ReporteAdapter.ReporteViewHolder>() {

    var reportes = listOf<Reporte>()
        set(value) {
            field = value
            notifyDataSetChanged()
        }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ReporteViewHolder {
        val view = LayoutInflater
            .from(parent.context)
            .inflate(R.layout.reporte_item, parent, false)
        return ReporteViewHolder(view, onClick)
    }

    override fun onBindViewHolder(holder: ReporteViewHolder, position: Int) {
        holder.bind(reportes[position])
    }

    class ReporteViewHolder(view: View, val onClick: (Reporte) -> Unit) : RecyclerView.ViewHolder(view) {
        val nombre: TextView = view.findViewById(R.id.reporte_nombre)
        val seleccionUser: TextView = view.findViewById(R.id.reporte_tipoPesca)
        val image: ImageView = view.findViewById(R.id.reporte_image)
        val date: TextView = view.findViewById(R.id.reporte_date)
        private var currentReglamentacion: Reporte? = null

        init {
            view.setOnClickListener {
                currentReglamentacion?.let {
                    onClick(it)
                }
            }
        }
        fun bind(reporte: Reporte) {
            currentReglamentacion = reporte

            nombre.text = reporte.nombre
            seleccionUser.text = reporte.tipoPesca
            image.setImageResource(reporte.image)
            date.text= reporte.date
        }
    }

    override fun getItemCount() = reportes.size

}