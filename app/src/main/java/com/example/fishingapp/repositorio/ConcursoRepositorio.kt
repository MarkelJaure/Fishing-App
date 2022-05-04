package com.example.fishingapp.repositorio

import androidx.lifecycle.LiveData
import com.example.fishingapp.dao.ConcursoDAO
import com.example.fishingapp.models.Concurso

class ConcursoRepositorio(private val concursoDAO: ConcursoDAO) {
    val allConcursos: LiveData<List<Concurso>> = concursoDAO.getAll()

    suspend fun insert(concurso: Concurso) {
        concursoDAO.insert(concurso)
    }
}