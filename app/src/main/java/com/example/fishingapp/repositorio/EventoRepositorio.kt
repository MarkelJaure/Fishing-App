package com.example.fishingapp.repositorio

import androidx.lifecycle.LiveData
import com.example.fishingapp.dao.EventoDAO
import com.example.fishingapp.models.Evento
import com.example.fishingapp.models.Reporte

class EventoRepositorio(private val eventoDAO: EventoDAO) {
    val allEventos: LiveData<List<Evento>> = eventoDAO.getAll()

    suspend fun insert(evento: Evento) {
        eventoDAO.insert(evento)
    }

    suspend fun load(evento: Evento) {
        eventoDAO.load(evento)
    }

    suspend fun clearCloudREventos() {
        eventoDAO.borrarTodos()
    }

    fun update(evento: Evento) {
        eventoDAO.update(evento)
    }
}