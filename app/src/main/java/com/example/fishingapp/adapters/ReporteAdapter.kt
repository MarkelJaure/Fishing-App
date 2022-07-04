package com.example.fishingapp.adapters

import android.graphics.BitmapFactory
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.fishingapp.R
import com.example.fishingapp.models.Reporte
import com.google.android.gms.maps.model.LatLng
import java.io.File

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
        val tipoPesca: TextView = view.findViewById(R.id.reporte_tipoPesca)
        val tipoEspecie: TextView = view.findViewById(R.id.reporte_tipoEspecie)
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
            tipoPesca.text = reporte.tipoPesca
            tipoEspecie.text = reporte.tipoEspecie
            var imgFile = File(reporte.image)
            if(imgFile.exists()) {
                image.setImageBitmap(BitmapFactory.decodeFile(imgFile.absolutePath))
            } else{
                image.setBackgroundResource(R.drawable.reporte_default)
            }
            LatLng(reporte.latitud, reporte.longitud)
            date.text= reporte.date
        }
    }

    override fun getItemCount() = reportes.size

}