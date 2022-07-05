package com.example.fishingapp.dao

import androidx.lifecycle.LiveData
import androidx.room.*
import com.example.fishingapp.models.Evento

@Dao
interface EventoDAO {
    @Query("SELECT * from eventos")
    fun getAll() : LiveData<List<Evento>>

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insert(evento: Evento)

    @Update
    fun update(evento: Evento)

    @Query("DELETE FROM eventos")
    suspend fun borrarTodos()

    @Query("SELECT COUNT(eventoId) from eventos")
    fun getCount() : Int
}