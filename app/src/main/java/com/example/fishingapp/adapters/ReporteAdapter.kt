package com.example.fishingapp.adapters

import android.graphics.BitmapFactory
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.fishingapp.R
import com.example.fishingapp.models.Reporte
import com.google.android.gms.maps.model.LatLng
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.ktx.storage
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

            if(reporte.image != "") {
                Log.w("ImagenReporte", reporte.nombre + " " + reporte.image)
                val imageRef = Firebase.storage.getReferenceFromUrl("gs://fishingapp-44a54.appspot.com/reportes/" + reporte.image)
                val localFile = File.createTempFile("RE_", "_list")
                Log.w("img reporte path", localFile.absolutePath)
                imageRef.getFile(localFile).addOnSuccessListener {
                    image.setImageBitmap(BitmapFactory.decodeFile(localFile.absolutePath))
                }
            } else {
                image.setImageResource(R.drawable.default_reporte)
            }

            LatLng(reporte.latitud, reporte.longitud)
            date.text= reporte.date
        }
    }

    override fun getItemCount() = reportes.size

}