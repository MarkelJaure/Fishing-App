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

    companion object {
        val data
            get() = listOf(
                Evento(
                    "Evento 1",
                    "Fauna",
                    "10/07/2022",
                    -42.6387597,
                    -65.0118788
                ),
                Evento(
                    "Evento 2",
                    "Flora",
                    "11/07/2022",
                    -42.4987,
                    -65.09
                ),
                Evento(
                    "Evento 3",
                    "Residuos",
                    "12/07/2022",
                    -42.57,
                    -65.3518
                ),
                Evento(
                    "Evento 4",
                    "Residuos",
                    "12/07/2022",
                    listOf("EV_08072022_13323644.png","EV_08072022_13323644.png","EV_08072022_13323644.png"),
                    -42.57,
                    -65.3518
                )
            )
    }
}
