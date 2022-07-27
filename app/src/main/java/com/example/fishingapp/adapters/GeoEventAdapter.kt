package com.example.fishingapp.adapters

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.fishingapp.R
import com.example.fishingapp.models.GeoEvent
import java.text.SimpleDateFormat
import java.util.*

class GeoEventAdapter(private val onClick: (GeoEvent) -> Unit) : RecyclerView.Adapter<GeoEventAdapter.ZonaViewHolder>() {

    var geoEvents = listOf<GeoEvent>()
        set(value) {
            field = value
            notifyDataSetChanged()
        }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ZonaViewHolder {
        val view = LayoutInflater
            .from(parent.context)
            .inflate(R.layout.geo_event_item, parent, false)
        return ZonaViewHolder(view, onClick)
    }

    override fun onBindViewHolder(holder: ZonaViewHolder, position: Int) {
        holder.bind(geoEvents[position])
    }

    class ZonaViewHolder(view: View, val onClick: (GeoEvent) -> Unit) : RecyclerView.ViewHolder(view) {
        val nombre: TextView = view.findViewById(R.id.geoevent_nombre)
        val descripcion: TextView = view.findViewById(R.id.geoevent_descripcion)
        val timestamp: TextView = view.findViewById(R.id.geoevent_timestamp)
        private var currentGeoEvent: GeoEvent? = null

        init {
            view.setOnClickListener {
                currentGeoEvent?.let {
                    onClick(it)
                }
            }
        }
        fun bind(geoEvent: GeoEvent) {
            currentGeoEvent = geoEvent

            nombre.text = geoEvent.nombre
            descripcion.text = geoEvent.descripcion
            timestamp.text = geoEvent.timestamp
        }
    }

    override fun getItemCount() = geoEvents.size

}