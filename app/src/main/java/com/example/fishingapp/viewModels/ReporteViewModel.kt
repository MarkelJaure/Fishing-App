package com.example.fishingapp.viewModels

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.viewModelScope
import com.example.fishingapp.database.FishingRoomDatabase
import com.example.fishingapp.models.Report
import com.example.fishingapp.repositorio.ReporteRepositorio
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class ReporteViewModel(application: Application) : AndroidViewModel(application) {
    private val repository: ReporteRepositorio
    val allReportes: LiveData<List<Report.Reporte>>
    init {
        val partidosDao = FishingRoomDatabase.getDatabase(application, viewModelScope).reporteDao()

        repository = ReporteRepositorio(partidosDao)
        allReportes = repository.allReportes
    }

    fun insert(reporte: Report.Reporte) = viewModelScope.launch(Dispatchers.IO) {
        repository.insert(reporte)
    }
}