package com.example.fishingapp.dao

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.fishingapp.models.Reporte

@Dao
interface ReporteDAO {
    @Query("SELECT * from reportes")
    fun getAll() : LiveData<List<Reporte>>

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insert(reporte: Reporte)

    @Query("DELETE FROM reportes")
    suspend fun borrarTodos()
}