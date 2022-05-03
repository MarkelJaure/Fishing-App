package com.example.fishingapp.models

import android.graphics.Bitmap
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.fishingapp.R

class Report{
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

companion object {
    val data
        get() = listOf(
            Reporte(
                0,
                "Nombre reporte 1",
                "Lago",
                "10/11/2011",
                R.drawable.pesca
            ),
            Reporte(
                1,
                "Nombre reporte 2",
                "Costa",
                "11/11/2011",
                R.drawable.pesca

            ),
            Reporte(
                2,
                "Nombre reporte 3",
                "Embarcacion",
                "12/11/2011",
                R.drawable.pesca

            )
        )
}}