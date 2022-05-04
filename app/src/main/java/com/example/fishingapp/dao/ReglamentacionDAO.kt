package com.example.fishingapp.dao

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.fishingapp.models.Reglamentacion

@Dao
interface ReglamentacionDAO {

    @Query("SELECT * from reglamentaciones")
    fun getAll() : LiveData<List<Reglamentacion>>

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insert(reglamentacion: Reglamentacion)
}