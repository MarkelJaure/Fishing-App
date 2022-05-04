package com.example.fishingapp.viewModels

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.viewModelScope
import com.example.fishingapp.database.FishingRoomDatabase
import com.example.fishingapp.models.Reglamentacion
import com.example.fishingapp.repositorio.ReglamentacionRepositorio
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class ReglamentacionViewModel(application: Application) : AndroidViewModel(application) {
    private val repository: ReglamentacionRepositorio
    val allReglamentaciones: LiveData<List<Reglamentacion>>
    init {
        val reglamentacionDao = FishingRoomDatabase.getDatabase(application, viewModelScope).reglamentacionDao()

        repository = ReglamentacionRepositorio(reglamentacionDao)
        allReglamentaciones = repository.allReglamentaciones
    }

    fun insert(concurso: Reglamentacion) = viewModelScope.launch(Dispatchers.IO) {
        repository.insert(concurso)
    }
}