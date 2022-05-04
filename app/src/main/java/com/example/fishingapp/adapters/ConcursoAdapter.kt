package com.example.fishingapp.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.fishingapp.R
import com.example.fishingapp.models.Concurso

class ConcursoAdapter(private val onClick: (Concurso) -> Unit) : RecyclerView.Adapter<ConcursoAdapter.ConcursoViewHolder>() {

    var concursos = listOf<Concurso>()
        set(value) {
            field = value
            notifyDataSetChanged()
        }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ConcursoViewHolder {
        val view = LayoutInflater
            .from(parent.context)
            .inflate(R.layout.concurso_item, parent, false)
        return ConcursoViewHolder(view, onClick)
    }

    override fun onBindViewHolder(holder: ConcursoViewHolder, position: Int) {
        val concurso = concursos[position]
        holder.bind(concurso)
    }

    class ConcursoViewHolder(view: View, val onClick: (Concurso) -> Unit) : RecyclerView.ViewHolder(view) {
        val nombre: TextView = view.findViewById(R.id.concurso_nombre)
        //val basesAndCondiciones: TextView = view.findViewById(R.id.concurso_basesAndCondiciones)
        val premio: TextView = view.findViewById(R.id.concurso_premio)
        //val ranking: TextView = view.findViewById(R.id.concurso_ranking)
        private var currentArticle: Concurso? = null

        init {
            view.setOnClickListener {
                currentArticle?.let {
                    onClick(it)
                }
            }
        }
        fun bind(concurso: Concurso) {
            currentArticle = concurso
            nombre.text = concurso.nombre
            //basesAndCondiciones.text = concurso.basesAndCondiciones
            premio.text = concurso.premio
            //ranking.text= concurso.ranking TODO: agregar recicler view de reportes
        }
    }

    override fun getItemCount() = concursos.size

}