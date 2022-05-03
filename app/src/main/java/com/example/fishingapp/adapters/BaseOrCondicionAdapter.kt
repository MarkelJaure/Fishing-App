package com.example.fishingapp.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.fishingapp.R
import com.example.fishingapp.models.BaseOrCondicion
import com.example.fishingapp.models.Reglamentacion

class BaseOrCondicionAdapter : RecyclerView.Adapter<BaseOrCondicionAdapter.BaseOrCondicionViewHolder>() {

    var basesOrCondiciones = listOf<BaseOrCondicion.BaseOrCondicion>()
        set(value) {
            field = value
            notifyDataSetChanged()
        }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BaseOrCondicionViewHolder {
        val view = LayoutInflater
            .from(parent.context)
            .inflate(R.layout.base_or_condicion_item, parent, false)
        return BaseOrCondicionViewHolder(view)
    }

    override fun onBindViewHolder(holder: BaseOrCondicionViewHolder, position: Int) {
        holder.bind(basesOrCondiciones[position])
    }

    class BaseOrCondicionViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val articulo: TextView = view.findViewById(R.id.baseOrCondicion_articulo)
        val descripcion: TextView = view.findViewById(R.id.baseOrCondicion_descripcion)

        fun bind(baseOrCondicion: BaseOrCondicion.BaseOrCondicion) {
            articulo.text = baseOrCondicion.articulo
            descripcion.text = baseOrCondicion.descripcion
        }
    }

    override fun getItemCount() = basesOrCondiciones.size
}