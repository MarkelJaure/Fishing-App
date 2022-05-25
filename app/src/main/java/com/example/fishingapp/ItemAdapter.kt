package com.example.fishingapp


import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.fishingapp.R
import com.example.fishingapp.models.Concurso

class ItemAdapter(private val onClick: (HomeItem) -> Unit) : RecyclerView.Adapter<ItemAdapter.ItemsViewHolder>() {

    var items = listOf<HomeItem>()
        set(value) {
            field = value
            notifyDataSetChanged()
        }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemsViewHolder {
        val view = LayoutInflater
            .from(parent.context)
            .inflate(R.layout.home_item, parent, false)
        return ItemsViewHolder(view, onClick)
    }

    override fun onBindViewHolder(holder: ItemsViewHolder, position: Int) {
        holder.bind(items[position])
    }

    class ItemsViewHolder(view: View, val onClick: (HomeItem) -> Unit) : RecyclerView.ViewHolder(view) {
        val nombre: TextView = view.findViewById(R.id.item_nombre)
        val descripcion: TextView = view.findViewById(R.id.item_descripcion)
        private var currentArticle: HomeItem? = null

        init {
            view.setOnClickListener {
                currentArticle?.let {
                    onClick(it)
                }
            }
        }
        fun bind(item: HomeItem) {
            currentArticle = item
            nombre.text = item.nombre
            descripcion.text = item.descripcion
        }
    }

    override fun getItemCount() = items.size
}