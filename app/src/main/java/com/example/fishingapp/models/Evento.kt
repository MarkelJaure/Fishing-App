package com.example.fishingapp.models

import androidx.room.*

@Entity(tableName = "eventos")
data class Evento(
    @PrimaryKey(autoGenerate = true)
    var eventoId: Int = 0,

    @ColumnInfo(name = "id")
    var id: String,

    @ColumnInfo(name = "nombre")
    var nombre: String,
    @ColumnInfo(name = "tipoPesca")
    var tipoEvento: String,
    @ColumnInfo(name = "date")
    var date: String,

    //TODO: Deben ser imagenes
    @TypeConverters(Converter::class)
    var images: List<String> = listOf(),

    @ColumnInfo(name = "latitud")
    var latitud: Double,
    @ColumnInfo(name = "longitud")
    var longitud: Double,
)
{
    constructor(nombre: String, tipoEvento: String, date: String, image: List<String>, latitud: Double, longitud: Double)
            : this(0,"0", nombre, tipoEvento, date, image, latitud, longitud)
    constructor(nombre: String, tipoEvento: String, date: String, latitud: Double, longitud: Double)
            : this(0,"0", nombre, tipoEvento, date, listOf<String>(), latitud, longitud)


}
