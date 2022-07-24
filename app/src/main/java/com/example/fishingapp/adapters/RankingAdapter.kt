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

class RankingAdapter : RecyclerView.Adapter<RankingAdapter.RankingViewHolder>() {

    var rankings = listOf<Reporte>()
        set(value) {
            field = value
            notifyDataSetChanged()
        }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RankingViewHolder {
        val view = LayoutInflater
            .from(parent.context)
            .inflate(R.layout.reporte_item, parent, false)
        return RankingViewHolder(view)
    }

    override fun onBindViewHolder(holder: RankingViewHolder, position: Int) {
        holder.bind(rankings[position])
    }

    class RankingViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val nombre: TextView = view.findViewById(R.id.reporte_nombre)
        val tipoPesca: TextView = view.findViewById(R.id.reporte_tipoPesca)
        val tipoEspecie: TextView = view.findViewById(R.id.reporte_tipoEspecie)
        val image: ImageView = view.findViewById(R.id.reporte_image)
        val date: TextView = view.findViewById(R.id.reporte_date)

        fun bind(ranking: Reporte) {
            nombre.text = ranking.nombre
            tipoPesca.text = ranking.tipoPesca
            tipoEspecie.text = ranking.tipoEspecie

            if(ranking.image != "") {
                Log.w("ImagenReporte", ranking.nombre + " " + ranking.image)
                val imageRef = Firebase.storage.getReferenceFromUrl("gs://fishingapp-44a54.appspot.com/reportes/" + ranking.image)
                val localFile = File.createTempFile("RE_", "_list")
                Log.w("img reporte path", localFile.absolutePath)
                imageRef.getFile(localFile).addOnSuccessListener {
                    image.setImageBitmap(BitmapFactory.decodeFile(localFile.absolutePath))
                }
            } else {
                image.setImageResource(R.drawable.default_reporte)
            }
            LatLng(ranking.latitud, ranking.longitud)
            date.text= ranking.date
        }
    }

    override fun getItemCount() = rankings.size
}