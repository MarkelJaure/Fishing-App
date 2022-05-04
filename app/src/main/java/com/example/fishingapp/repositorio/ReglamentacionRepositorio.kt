package com.example.fishingapp.repositorio

import androidx.lifecycle.LiveData
import com.example.fishingapp.dao.ReglamentacionDAO
import com.example.fishingapp.models.Reglamentacion

class ReglamentacionRepositorio(private val reglamentacionDAO: ReglamentacionDAO) {
    val allReglamentaciones: LiveData<List<Reglamentacion>> = reglamentacionDAO.getAll()

    suspend fun insert(reglamentacion: Reglamentacion) {
        reglamentacionDAO.insert(reglamentacion)
    }
}