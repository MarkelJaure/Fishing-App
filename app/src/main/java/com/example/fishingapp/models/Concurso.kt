package com.example.fishingapp.models

import androidx.room.*
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

data class BasesAndCondiciones(
    @TypeConverters(Converter::class)
    var basesAndCondiciones: List<BaseOrCondicion> = listOf()
)

data class Ranking(
    @TypeConverters(Converter::class)
    var ranking: List<Reporte> = listOf()
)

@Entity(tableName = "concursos")
data class Concurso (

    @PrimaryKey(autoGenerate = true)
    var concursoId: Int = 0,

    @ColumnInfo(name = "id")
    var id: String,

    @ColumnInfo(name = "nombre")
    var nombre: String,

    @Embedded var basesYCondiciones: BasesAndCondiciones,

    @ColumnInfo(name = "premio")
    var premio: String,

    @Embedded var ranking: Ranking,
    )
{
    constructor(nombre: String, basesAndCondiciones: BasesAndCondiciones, premio: String, ranking: Ranking): this (0,"0",nombre,basesAndCondiciones, premio, ranking)
    constructor(id:String,nombre: String, basesAndCondiciones: BasesAndCondiciones, premio: String, ranking: Ranking): this (0,id,nombre,basesAndCondiciones, premio, ranking)
    constructor(nombre: String, basesAndCondiciones: List<BaseOrCondicion>, premio: String, ranking: List<Reporte>): this (nombre,BasesAndCondiciones(basesAndCondiciones), premio, Ranking(ranking))
    constructor(nombre: String, premio: String, ranking: List<Reporte>): this (nombre, BasesAndCondiciones(listOf<BaseOrCondicion>()), premio, Ranking(ranking))
    constructor(nombre: String, basesAndCondiciones: List<BaseOrCondicion>, premio: String): this (nombre,BasesAndCondiciones(basesAndCondiciones), premio, Ranking(listOf<Reporte>()))
    constructor(nombre: String, premio: String): this (nombre, BasesAndCondiciones(listOf<BaseOrCondicion>()), premio, Ranking(listOf<Reporte>()))


   }

