package com.example.fishingapp.repositorio

import androidx.lifecycle.LiveData
import com.example.fishingapp.dao.ReporteDAO
import com.example.fishingapp.models.Reporte

class ReporteRepositorio(private val reporteDAO: ReporteDAO) {
    val allReportes: LiveData<List<Reporte>> = reporteDAO.getAll()

    suspend fun insert(reporte: Reporte) {
        reporteDAO.insert(reporte)
    }

    suspend fun load(reporte: Reporte) {
        reporteDAO.load(reporte)
    }

    suspend fun clearCloudReportes() {
        reporteDAO.borrarTodos()
    }

    fun update(reporte: Reporte) {
        reporteDAO.update(reporte)
    }
}