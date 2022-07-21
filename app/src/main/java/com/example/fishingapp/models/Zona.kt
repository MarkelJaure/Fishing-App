package com.example.fishingapp.models

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "zonas")
data class Zona(

    @PrimaryKey(autoGenerate = true)
    var zonaId: Int = 0,

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
    var radius: Double
)
{

    constructor(nombre: String,descripcion: String,latitud: Double,longitud: Double,radius: Double):this(0,"0", nombre, descripcion, latitud,longitud,radius)
    constructor(id:String,nombre: String,descripcion: String,latitud: Double,longitud: Double,radius: Double):this(0,id, nombre, descripcion, latitud,longitud,radius)


}