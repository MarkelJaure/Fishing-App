package com.example.fishingapp.models


data class Concurso (
        var nombre: String,
        var basesAndCondiciones: List<BaseOrCondicion>,
        var premio: String,
        var ranking: List<Reporte>,
    )
{

    companion object {
        val data
            get() = listOf(
                Concurso(
                    "Nombre concurso 1",
                    BaseOrCondicion.data,
                    "nada de nada",
                    Reporte.data
                ),
                Concurso(
                    "Nombre concurso 2",
                    BaseOrCondicion.data,
                    "Un trofeito",
                    listOf<Reporte>()

                ),
                Concurso(
                    "Nombre concurso 3",
                    listOf<BaseOrCondicion>(),
                    "Una medallita",
                    Reporte.data
                ), Concurso(
                    "Nombre concurso 4",
                    listOf<BaseOrCondicion>(),
                    "nada de nada",
                    listOf<Reporte>(),
                )
            )
    }}