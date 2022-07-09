package com.example.fishingapp.viewModels

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.fishingapp.database.FishingRoomDatabase
import com.example.fishingapp.models.Evento
import com.example.fishingapp.models.Reporte
import com.example.fishingapp.repositorio.EventoRepositorio
import com.google.android.gms.maps.model.LatLng
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class EventoViewModel(application: Application) : AndroidViewModel(application) {
    private val repository: EventoRepositorio
    val allEventos: LiveData<List<Evento>>

    init {
        val eventoDAO = FishingRoomDatabase.getDatabase(application, viewModelScope).eventoDao()

        repository = EventoRepositorio(eventoDAO)
        allEventos = repository.allEventos
    }

    fun insert(evento: Evento) = viewModelScope.launch(Dispatchers.IO) {
        repository.insert(evento)
    }

    fun update(evento: Evento) = viewModelScope.launch(Dispatchers.IO) {
        repository.update(evento)
    }

    fun load(evento: Evento) = viewModelScope.launch(Dispatchers.IO) {
        repository.load(evento)
    }

    fun clearCloudEventos() = viewModelScope.launch(Dispatchers.IO) {
        repository.clearCloudREventos()
    }
}