package com.example.fishingapp.models

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "reportes_cloud")
data class ReporteCloud(
    @PrimaryKey(autoGenerate = true)
    var reporteId: Int = 0,

    @ColumnInfo(name = "id")
    var id: String,
    @ColumnInfo(name = "nombre")
    var nombre: String,
    @ColumnInfo(name = "tipoPesca")
    var tipoPesca: String,
    @ColumnInfo(name = "tipoEspecie")
    var tipoEspecie: String,
    @ColumnInfo(name = "date")
    var date: String,
    @ColumnInfo(name = "image")
    var image: String,
    @ColumnInfo(name = "latitud")
    var latitud: Double,
    @ColumnInfo(name = "longitud")
    var longitud: Double,
)
