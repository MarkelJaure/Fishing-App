package com.example.fishingapp.dao

import androidx.lifecycle.LiveData
import androidx.room.*
import com.example.fishingapp.models.Reporte

@Dao
interface ReporteDAO {
    @Query("SELECT * from reportes")
    fun getAll() : LiveData<List<Reporte>>

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insert(reporte: Reporte)

    @Update
    fun update(reporte: Reporte)

    @Query("DELETE FROM reportes")
    suspend fun borrarTodos()

    @Query("SELECT COUNT(reporteId) from reportes")
    fun getCount() : Int
}