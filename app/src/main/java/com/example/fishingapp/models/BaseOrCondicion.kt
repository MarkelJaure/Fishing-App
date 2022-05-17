package com.example.fishingapp.models

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey


data class BaseOrCondicion (

    var articulo: String,
    var descripcion: String
    )
{
   // constructor(articulo: String,descripcion: String):this(articulo,descripcion)

    companion object {
        val data
            get() = listOf(
                BaseOrCondicion(
                    "Articulo 1",
                    " La duración de la competencia será realizada en dos (2) " +
                            "jornadas los días miércoles y jueves, cada jornada se realizará en " +
                            "cuatro (4) horas efectivas de pesca, constará de dos tiempos de dos (2) horas " +
                            "cada uno, con un intervalo de quince (15) minutos de descanso entre tiempos y " +
                            "rotación en función del sorteo."
                ),
                BaseOrCondicion(
                    "Articulo 2",
                    "La competencia será en la modalidad de Pesca de muelle. A cada pescador se le asignará un número para el sorteo."
                ),
                BaseOrCondicion(
                    "Articulo 3",
                    "Por ninguna causa se podrá abandonar la caña estando la " +
                            "línea en el agua. En caso de tener que ausentarse del lugar de pesca deberá " +
                            "levantar la línea del agua y colocarla en un lugar donde no moleste a los demás " +
                            "participantes, solicitando al mismo tiempo la autorización correspondiente."
                ),
                BaseOrCondicion(
                    "Articulo 4",
                    "Frente al empate, se desempatara por las piezas de mayor " +
                            "tamaño, al término de las dos jornadas de pesca. Las medidas se tomarán " +
                            "desde el hocico al extremo más largo de la cola. y de persistir la igualdad, se " +
                            "hará por sorteo."
                )
            )
    }
}
