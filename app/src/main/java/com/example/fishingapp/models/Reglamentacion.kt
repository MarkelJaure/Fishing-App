package com.example.fishingapp.models

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "reglamentaciones")
data class Reglamentacion(

    @PrimaryKey(autoGenerate = true)
    var reglamentacionId: Int = 0,

    @ColumnInfo(name = "id")
    var id: String,

    @ColumnInfo(name = "nombre")
    var nombre: String,
    @ColumnInfo(name = "descripcion")
    var descripcion: String,


    @ColumnInfo(name = "latitud")
    var latitud: Double,
    @ColumnInfo(name = "longitud")
    var longitud: Double,
    @ColumnInfo(name = "radius")
    var radius: Double,
    @ColumnInfo(name = "ubicacion")
    var ubicacion: String,
)
{

    constructor(nombre: String,descripcion: String,latitud: Double,longitud: Double,radius: Double,ubicacion: String):this(0,"0", nombre, descripcion, latitud,longitud,radius,ubicacion)
    constructor(id:String,nombre: String,descripcion: String,latitud: Double,longitud: Double,radius: Double,ubicacion: String):this(0,id, nombre, descripcion, latitud,longitud,radius,ubicacion)


}