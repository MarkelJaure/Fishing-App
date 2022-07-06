package com.example.fishingapp.models

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "eventos")
data class Evento(
    @PrimaryKey(autoGenerate = true)
    var eventoId: Int = 0,

    @ColumnInfo(name = "nombre")
    var nombre: String,
    @ColumnInfo(name = "tipoPesca")
    var tipoEvento: String,
    @ColumnInfo(name = "date")
    var date: String,

    //TODO: Deben ser imagenens
    @ColumnInfo(name = "image")
    var image: String,

    @ColumnInfo(name = "latitud")
    var latitud: Double,
    @ColumnInfo(name = "longitud")
    var longitud: Double,
)
{
    constructor(nombre: String, tipoEvento: String, date: String, image: String, latitud: Double, longitud: Double)
            : this(0, nombre, tipoEvento, date, image, latitud, longitud)

    companion object {
        val data
            get() = listOf(
                Evento(
                    "Nombre Evento 1",
                    "Fauna",
                    "10/07/2022",
                    "",
                    -42.6387597,
                    -65.0118788
                ),
                Evento(
                    "Nombre Evento 2",
                    "Flora",
                    "11/07/2022",
                    "",
                    -42.4987,
                    -65.09
                ),
                Evento(
                    "Nombre Evento 3",
                    "Residuos",
                    "12/07/2022",
                    "",
                    -42.57,
                    -65.3518
                )
            )
    }
}
