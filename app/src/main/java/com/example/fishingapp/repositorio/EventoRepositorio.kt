package com.example.fishingapp.repositorio

import androidx.lifecycle.LiveData
import com.example.fishingapp.dao.EventoDAO
import com.example.fishingapp.models.Evento

class EventoRepositorio(private val eventoDAO: EventoDAO) {
    val allEventos: LiveData<List<Evento>> = eventoDAO.getAll()

    suspend fun insert(evento: Evento) {
        eventoDAO.insert(evento)
    }

    fun update(evento: Evento) {
        eventoDAO.update(evento)
    }
}