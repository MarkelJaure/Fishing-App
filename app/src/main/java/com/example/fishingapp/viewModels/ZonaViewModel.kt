package com.example.fishingapp.viewModels

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.viewModelScope
import com.example.fishingapp.database.FishingRoomDatabase
import com.example.fishingapp.models.Reglamentacion
import com.example.fishingapp.models.Zona
import com.example.fishingapp.repositorio.ReglamentacionRepositorio
import com.example.fishingapp.repositorio.ZonaRepositorio
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class ZonaViewModel(application: Application) : AndroidViewModel(application) {
    private val repository: ZonaRepositorio
    val allZonas: LiveData<List<Zona>>

    init {
        val zonaDao = FishingRoomDatabase.getDatabase(application, viewModelScope).zonaDao()

        repository = ZonaRepositorio(zonaDao)
        allZonas = repository.allZonas
    }

    fun insert(zona: Zona) = viewModelScope.launch(Dispatchers.IO) {
        repository.insert(zona)
    }

    fun borrarTodos() = viewModelScope.launch(Dispatchers.IO) {
        repository.borrarTodos()
    }
}