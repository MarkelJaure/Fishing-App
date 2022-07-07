package com.example.fishingapp.dao

import androidx.lifecycle.LiveData
import androidx.room.*
import com.example.fishingapp.models.Reporte
import com.example.fishingapp.models.ReporteCloud

@Dao
interface ReporteDAO {
    @Query("SELECT * from reportes_cloud")
    fun getAll() : LiveData<List<Reporte>>

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insert(reporte: Reporte)

    @Insert(entity = ReporteCloud::class, onConflict = OnConflictStrategy.IGNORE)
    suspend fun load(reporte: Reporte)

    @Update
    fun update(reporte: Reporte)

    @Query("DELETE FROM reportes_cloud")
    suspend fun borrarTodos()

    @Query("SELECT COUNT(reporteId) from reportes")
    fun getCount() : Int
}