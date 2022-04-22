package com.example.proyectovacio

import android.graphics.Bitmap

class Report{
data class Reporte (
    var nombre: String,
    var seleccionUser: String,
    var date: String,
    var image: Bitmap?,
)

companion object {
    val data
        get() = listOf(
            Reporte(
                "Nombre reporte 1",
                "Lago",
                "11/11/2011",
                null
            ),
            Reporte(
                "Nombre reporte 2",
                "Costa",
                "11/11/2011",
                null
            ),
            Reporte(
                "Nombre reporte 3",
                "Embarcacion",
                "11/11/2011",
                null
            )
        )
}}