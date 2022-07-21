package com.example.fishingapp.repositorio

import androidx.lifecycle.LiveData
import com.example.fishingapp.dao.ZonaDAO
import com.example.fishingapp.models.Zona

class ZonaRepositorio(private val zonaDao: ZonaDAO) {
    val allZonas: LiveData<List<Zona>> = zonaDao.getAll()

    suspend fun insert(zona: Zona) {
        zonaDao.insert(zona)
    }

    suspend fun borrarTodos() {
        zonaDao.borrarTodos()
    }
}