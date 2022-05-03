package com.example.fishingapp.repositorio

import androidx.lifecycle.LiveData
import com.example.fishingapp.dao.ReporteDAO
import com.example.fishingapp.models.Reporte

class ReporteRepositorio(private val reporteDAO: ReporteDAO) {
    val allReportes: LiveData<List<Reporte>> = reporteDAO.getAll()

    suspend fun insert(reporte: Reporte) {
        reporteDAO.insert(reporte)
    }
}