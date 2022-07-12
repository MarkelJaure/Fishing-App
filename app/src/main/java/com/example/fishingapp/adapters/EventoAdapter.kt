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
import com.example.fishingapp.models.Evento
import com.google.android.gms.maps.model.LatLng
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.ktx.storage
import java.io.File

class EventoAdapter(private val onClick: (Evento) -> Unit) : RecyclerView.Adapter<EventoAdapter.EventoViewHolder>() {

    var eventos = listOf<Evento>()
        set(value) {
            field = value
            notifyDataSetChanged()
        }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): EventoViewHolder {
        val view = LayoutInflater
            .from(parent.context)
            .inflate(R.layout.evento_item, parent, false)
        return EventoViewHolder(view, onClick)
    }

    override fun onBindViewHolder(holder: EventoViewHolder, position: Int) {
        holder.bind(eventos[position])
    }

    class EventoViewHolder(view: View, val onClick: (Evento) -> Unit) : RecyclerView.ViewHolder(view) {
        val nombre: TextView = view.findViewById(R.id.evento_nombre)
        val tipoEvento: TextView = view.findViewById(R.id.evento_tipoEvento)
        val image: ImageView = view.findViewById(R.id.evento_image)
        val date: TextView = view.findViewById(R.id.evento_date)
        private var currentEvento: Evento? = null

        init {
            view.setOnClickListener {
                currentEvento?.let {
                    onClick(it)
                }
            }
        }
        fun bind(evento: Evento) {
            currentEvento = evento

            nombre.text = evento.nombre
            tipoEvento.text = evento.tipoEvento
            image.setBackgroundResource(R.drawable.reporte_default)

            if(!evento.images.isNullOrEmpty()) {
                Log.w("ImagenEvento", evento.images[0])
                val imageRef =
                    Firebase.storage.getReferenceFromUrl("gs://fishingapp-44a54.appspot.com/eventos/" + evento.images[0])
                val localFile = File.createTempFile("EV_", "_list")

                imageRef.getFile(localFile).addOnSuccessListener {
                    image.setImageBitmap(BitmapFactory.decodeFile(localFile.absolutePath))
                }
            }

            LatLng(evento.latitud, evento.longitud)
            date.text= evento.date
        }
    }

    override fun getItemCount() = eventos.size

}