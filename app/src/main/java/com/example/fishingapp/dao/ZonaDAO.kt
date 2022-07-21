package com.example.fishingapp.dao

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.fishingapp.models.Reglamentacion
import com.example.fishingapp.models.Zona

@Dao
interface ZonaDAO {

    @Query("SELECT * from zonas")
    fun getAll() : LiveData<List<Zona>>

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insert(zona: Zona)

    @Query("DELETE FROM zonas")
    suspend fun borrarTodos()


    @Query("SELECT COUNT(zonaId) from zonas")
    fun getCount() : Int
}