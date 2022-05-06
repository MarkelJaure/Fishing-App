package com.example.fishingapp.dao

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.fishingapp.models.Concurso

@Dao
interface ConcursoDAO {

    @Query("SELECT * from concursos")
    fun getAll() : LiveData<List<Concurso>>

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insert(concurso: Concurso)

    @Query("DELETE FROM concursos")
    suspend fun borrarTodos()
}