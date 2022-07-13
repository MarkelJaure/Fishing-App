package com.example.fishingapp.models

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "reglamentaciones")
data class Reglamentacion(

    @PrimaryKey(autoGenerate = true)
    var reglamentacionId: Int = 0,

    @ColumnInfo(name = "id")
    var id: String,

    @ColumnInfo(name = "nombre")
    var nombre: String,
    @ColumnInfo(name = "descripcion")
    var descripcion: String,


    @ColumnInfo(name = "latitud")
    var latitud: Double,
    @ColumnInfo(name = "longitud")
    var longitud: Double,
    @ColumnInfo(name = "radius")
    var radius: Double,
    @ColumnInfo(name = "ubicacion")
    var ubicacion: String,
)
{

    constructor(nombre: String,descripcion: String,latitud: Double,longitud: Double,radius: Double,ubicacion: String):this(0,"0", nombre, descripcion, latitud,longitud,radius,ubicacion)
    constructor(id:String,nombre: String,descripcion: String,latitud: Double,longitud: Double,radius: Double,ubicacion: String):this(0,id, nombre, descripcion, latitud,longitud,radius,ubicacion)

    companion object {
            val data
                get() = listOf(
                    Reglamentacion(
                        "Señuelos autorizados",
                        "La pesca debe practicarse con un señuelo artificial con un único anzuelo (simple, doble o triple). Cuando se quiera utilizar un señuelo que tenga más de un anzuelo, deben quitarse los restantes o inutilizarlos de forma tal que no puedan clavarse en los peces. En los ambientes de devolución obligatoria solo se permite la utilización de un anzuelo simple, sin rebaba o con rebaba aplastada. Se prohíbe el uso de señuelos que contengan pilas o baterías.",
                        -42.7692,
                        -65.03851,
                        10000.0,
                        "Puerto Madryn"
                    ),
                    Reglamentacion(
                        "Embarcación",
                        "Todas las embarcaciones usadas con fines de pesca deben cumplir los requisitos exigidos por Prefectura Naval Argentina.",
                        -43.24895,
                        -65.30505,
                        10000.0,
                        "Trelew",
                    ),
                    Reglamentacion(
                        "Número máximo de piezas por pescador",
                        "Para el caso de ambientes que no figuren en el listado alfabético con reglamentación específica correspondiente, se permitirá el sacrificio de un (1) salmónido, dos (2) Percas y cuatro (4) ejemplares de Pejerrey por día y por pescador, con la excepción de los ríos y arroyos donde Chubut adhiere al Reglamento General donde se establece la Devolución Obligatoria. En las áreas de devolución obligatoria se prohíbe la tenencia de ejemplares de peces de cualquier origen.",
                        -43.30016,
                        -65.10228,
                        10000.0,
                        "Playa union",
                    )
                )
        }
}