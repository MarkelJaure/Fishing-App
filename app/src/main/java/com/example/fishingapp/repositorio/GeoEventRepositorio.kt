package com.example.fishingapp.repositorio

import androidx.lifecycle.LiveData
import com.example.fishingapp.dao.GeoEventDAO
import com.example.fishingapp.dao.ZonaDAO
import com.example.fishingapp.models.GeoEvent
import com.example.fishingapp.models.Zona

class GeoEventRepositorio(private val geoEventDAO: GeoEventDAO) {
    val allGeoEvents: LiveData<List<GeoEvent>> = geoEventDAO.getAll()

    suspend fun insert(geoEvent: GeoEvent) {
        geoEventDAO.insert(geoEvent)
    }

    suspend fun borrarTodos() {
        geoEventDAO.borrarTodos()
    }
}