package com.example.fishingapp.viewModels

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.viewModelScope
import com.example.fishingapp.database.FishingRoomDatabase
import com.example.fishingapp.models.Reporte
import com.example.fishingapp.repositorio.ReporteRepositorio
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class ReporteViewModel(application: Application) : AndroidViewModel(application) {
    private val repository: ReporteRepositorio
    val allReportes: LiveData<List<Reporte>>
    init {
        val reporteDao = FishingRoomDatabase.getDatabase(application, viewModelScope).reporteDao()

        repository = ReporteRepositorio(reporteDao)
        allReportes = repository.allReportes
    }

    fun insert(reporte: Reporte) = viewModelScope.launch(Dispatchers.IO) {
        repository.insert(reporte)
    }

    fun update(reporte: Reporte) = viewModelScope.launch(Dispatchers.IO) {
        repository.update(reporte)
    }
}