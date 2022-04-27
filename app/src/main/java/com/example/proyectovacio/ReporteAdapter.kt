package com.example.proyectovacio

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class ReporteAdapter(private val onClick: (Report.Reporte) -> Unit) : RecyclerView.Adapter<ReporteAdapter.ReporteViewHolder>() {

    var reportes = listOf<Report.Reporte>()
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
        val reporte = reportes[position]
        holder.bind(reporte)
    }

    class ReporteViewHolder(view: View, val onClick: (Report.Reporte) -> Unit) : RecyclerView.ViewHolder(view) {
        val nombre: TextView = view.findViewById(R.id.reporte_nombre)
        val seleccionUser: TextView = view.findViewById(R.id.reporte_tipoPesca)
        val image: ImageView = view.findViewById(R.id.reporte_image)
        val date: TextView = view.findViewById(R.id.reporte_date)
        private var currentArticle: Report.Reporte? = null

        init {
            view.setOnClickListener {
                currentArticle?.let {
                    onClick(it)
                }
            }
        }
        fun bind(reporte: Report.Reporte) {
            currentArticle = reporte

            nombre.text = reporte.nombre
            seleccionUser.text = reporte.tipoPesca
            image.setImageResource(R.drawable.pesca) //TODO: hardcodeado a imagen de pesca
            date.text= reporte.date
        }
    }

    override fun getItemCount() = reportes.size

}