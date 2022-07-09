package com.example.fishingapp.models

import androidx.room.*

@Entity(tableName = "eventos_cloud")
data class EventoCloud(
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

