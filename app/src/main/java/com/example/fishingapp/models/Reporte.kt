package com.example.fishingapp.models

import com.example.fishingapp.R

class Report{
data class Reporte (
    var nombre: String,

    var tipoPesca: String,
    var date: String,
    var image: Int,
)

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
}}