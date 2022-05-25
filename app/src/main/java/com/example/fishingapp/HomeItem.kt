package com.example.fishingapp

data class HomeItem (

    var nombre: String,
    var descripcion: String,

    ){
    companion object {
        val data
            get() = listOf(
                HomeItem("FormFragment","Agregar Un Reporte"),
                HomeItem("ReportListFragment","Ver Reportes"),
                HomeItem("ReglamentacionListFragment","Ver Reglamentaciones"),
                HomeItem("ConcursoListFragment","Ver Concursos"),
                HomeItem("AboutUsFragment","Info Sobre nosotros"),
            )
    }
}