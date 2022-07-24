package com.example.fishingapp.viewModels

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.fishingapp.database.FishingRoomDatabase
import com.example.fishingapp.models.Evento
import com.example.fishingapp.models.GeoEvent
import com.example.fishingapp.models.Reporte
import com.example.fishingapp.repositorio.EventoRepositorio
import com.example.fishingapp.repositorio.GeoEventRepositorio
import com.google.android.gms.maps.model.LatLng
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class GeoEventViewModel(application: Application) : AndroidViewModel(application) {
    private val repository: GeoEventRepositorio
    val allGeoEvents: LiveData<List<GeoEvent>>

    init {
        val geoEventDAO = FishingRoomDatabase.getDatabase(application, viewModelScope).geoEventDao()

        repository = GeoEventRepositorio(geoEventDAO)
        allGeoEvents = repository.allGeoEvents
    }

    fun insert(geoEvent: GeoEvent) = viewModelScope.launch(Dispatchers.IO) {
        repository.insert(geoEvent)
    }

    fun borrarTodos() = viewModelScope.launch(Dispatchers.IO) {
        repository.borrarTodos()
    }
}