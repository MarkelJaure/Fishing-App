package com.example.fishingapp.viewModels

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.viewModelScope
import com.example.fishingapp.database.FishingRoomDatabase
import com.example.fishingapp.models.Concurso
import com.example.fishingapp.repositorio.ConcursoRepositorio
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class ConcursoViewModel(application: Application) : AndroidViewModel(application) {
    private val repository: ConcursoRepositorio
    val allConcursos: LiveData<List<Concurso>>
    init {
        val concursoDao = FishingRoomDatabase.getDatabase(application, viewModelScope).concursoDao()

        repository = ConcursoRepositorio(concursoDao)
        allConcursos = repository.allConcursos
    }

    fun insert(concurso: Concurso) = viewModelScope.launch(Dispatchers.IO) {
        repository.insert(concurso)
    }
}