package com.example.fishingapp.models

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "geo_event")
data class GeoEvent(

    @PrimaryKey(autoGenerate = true)
    var geoEventId: Int = 0,

    @ColumnInfo(name = "id")
    var id: String,

    @ColumnInfo(name = "userId")
    var userId: String,

    @ColumnInfo(name = "timestamp")
    var timestamp: String,

    @ColumnInfo(name = "nombre")
    var nombre: String,
    @ColumnInfo(name = "descripcion")
    var descripcion: String,


    @ColumnInfo(name = "latitud")
    var latitud: Double,
    @ColumnInfo(name = "longitud")
    var longitud: Double,
    @ColumnInfo(name = "radius")
    var radius: Double
)
{
    constructor(id: String, userId: String, timestamp: String, nombre: String,descripcion: String,latitud: Double,longitud: Double,radius: Double):this(0, id, userId, timestamp, nombre, descripcion, latitud,longitud,radius)
}