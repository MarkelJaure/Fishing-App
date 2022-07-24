package com.example.fishingapp.dao

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.fishingapp.models.GeoEvent
import com.example.fishingapp.models.Reglamentacion
import com.example.fishingapp.models.Zona

@Dao
interface GeoEventDAO {

    @Query("SELECT * from geo_event")
    fun getAll() : LiveData<List<GeoEvent>>

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insert(geoEvent: GeoEvent)

    @Query("DELETE FROM geo_event")
    suspend fun borrarTodos()


    @Query("SELECT COUNT(geoEventId) from geo_event")
    fun getCount() : Int
}