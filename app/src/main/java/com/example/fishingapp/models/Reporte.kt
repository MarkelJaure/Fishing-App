package com.example.fishingapp.models

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "reportes")
data class Reporte(
    @PrimaryKey(autoGenerate = true)
    var reporteId: Int = 0,

    @ColumnInfo(name = "nombre")
    var nombre: String,
    @ColumnInfo(name = "tipoPesca")
    var tipoPesca: String,
    @ColumnInfo(name = "date")
    var date: String,
    @ColumnInfo(name = "image")
    var image: String,
    @ColumnInfo(name = "latitud")
    var latitud: Double,
    @ColumnInfo(name = "longitud")
    var longitud: Double,
)
{
    constructor(nombre: String, tipoPesca: String, date: String, image: String, latitud: Double, longitud: Double)
            : this(0, nombre, tipoPesca, date, image, latitud, longitud)

    companion object {
        val data
            get() = listOf(
                Reporte(
                    "Nombre reporte 1",
                    "Lago",
                    "10/11/2022",
                    "/storage/emulated/0/Android/data/com.example.fishingapp/Files/MI_15052022_2026.png",
                    -42.7787597,
                    -65.0518788
                ),
                Reporte(
                    "Nombre reporte 2",
                    "Costa",
                    "11/11/2022",
                    "",
                    -42.7787,
                    -65.05
                ),
                Reporte(
                    "Nombre reporte 3",
                    "Embarcacion",
                    "12/11/2022",
                    "",
                    -42.77,
                    -65.0518
                )
            )
    }
}
