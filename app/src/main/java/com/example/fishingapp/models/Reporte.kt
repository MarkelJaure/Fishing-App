package com.example.fishingapp.models

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.fishingapp.R

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
    var image: Int,
)
{
    constructor(nombre: String, tipoPesca: String, date: String, image: Int) : this(0, nombre, tipoPesca, date, image)

    companion object {
        val data
            get() = listOf(
                Reporte(
                    "Nombre reporte 1",
                    "Lago",
                    "10/11/2011",
                    R.drawable.pesca
                ),
                Reporte(
                    "Nombre reporte 2",
                    "Costa",
                    "11/11/2011",
                    R.drawable.pesca
                ),
                Reporte(
                    "Nombre reporte 3",
                    "Embarcacion",
                    "12/11/2011",
                    R.drawable.pesca
                )
            )
    }
}
