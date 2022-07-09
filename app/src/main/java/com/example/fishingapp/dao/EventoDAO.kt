package com.example.fishingapp.dao

import androidx.lifecycle.LiveData
import androidx.room.*
import com.example.fishingapp.models.Evento
import com.example.fishingapp.models.EventoCloud
import com.example.fishingapp.models.Reporte
import com.example.fishingapp.models.ReporteCloud

@Dao
interface EventoDAO {
    @Query("SELECT * from eventos_cloud")
    fun getAll() : LiveData<List<Evento>>

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insert(evento: Evento)

    @Insert(entity = EventoCloud::class, onConflict = OnConflictStrategy.IGNORE)
    suspend fun load(evento: Evento)

    @Update
    fun update(evento: Evento)

    @Query("DELETE FROM eventos_cloud")
    suspend fun borrarTodos()

    @Query("SELECT COUNT(eventoId) from eventos")
    fun getCount() : Int
}