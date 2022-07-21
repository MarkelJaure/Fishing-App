package com.example.fishingapp.viewModels

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.fishingapp.database.FishingRoomDatabase
import com.example.fishingapp.models.Reglamentacion
import com.example.fishingapp.repositorio.ReglamentacionRepositorio
import com.google.android.gms.maps.model.LatLng
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class ReglamentacionViewModel(application: Application) : AndroidViewModel(application) {
    private val repository: ReglamentacionRepositorio
    val allReglamentaciones: LiveData<List<Reglamentacion>>

    //UbicationFilter
    private var _centerPoint = MutableLiveData<LatLng>()
    private var _isUbicationFilterApplied = MutableLiveData<Boolean>()

    val centerPoint: LiveData<LatLng>
        get() = _centerPoint

    fun setCenterPoint(aCenterPoint: LatLng){
        _centerPoint.value= aCenterPoint
    }

    val isUbicationFilterApplied: LiveData<Boolean>
        get() = _isUbicationFilterApplied

    fun setIsUbicationFilterApplied(aIsUbicationFilterApplied:Boolean){
        _isUbicationFilterApplied.value= aIsUbicationFilterApplied
    }

    init {
        val reglamentacionDao = FishingRoomDatabase.getDatabase(application, viewModelScope).reglamentacionDao()

        repository = ReglamentacionRepositorio(reglamentacionDao)
        allReglamentaciones = repository.allReglamentaciones
    }

    fun insert(reglamentacion: Reglamentacion) = viewModelScope.launch(Dispatchers.IO) {
        repository.insert(reglamentacion)
    }

    fun borrarTodos() = viewModelScope.launch(Dispatchers.IO) {
        repository.borrarTodos()
    }
}