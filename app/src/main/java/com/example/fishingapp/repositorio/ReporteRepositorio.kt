package com.example.fishingapp.repositorio

import androidx.lifecycle.LiveData
import com.example.fishingapp.dao.ReporteDAO
import com.example.fishingapp.models.Report

class ReporteRepositorio(private val reporteDAO: ReporteDAO) {
    val allReportes: LiveData<List<Report.Reporte>> = reporteDAO.getAll()

    suspend fun insert(reporte: Report.Reporte) {
        reporteDAO.insert(reporte)
    }
}