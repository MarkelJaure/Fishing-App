package com.example.fishingapp

data class HomeItem (

    var nombre: String,
    var descripcion: String,
    var  subtitulo: String,

    ){
    companion object {
        val data
            get() = listOf(
                HomeItem("FormFragment","Agregar Un Reporte","Crea un nuevo reporte de pesca"),
                //HomeItem("ReportListFragment","Ver Reportes", "Observa los ultimos reportes cargados"),
                //HomeItem("ReglamentacionListFragment","Ver Reglamentaciones", "Enterate de las ultimas normas de pesca"),
                //HomeItem("ConcursoListFragment","Ver Concursos", "Anotate en los cursos de tu zona"),
                HomeItem("FormEventFragment","Agregar Evento", "Reporta un evento raro"),
                //HomeItem("EventoListFragment","Ver eventos raros", "Mantente al tanto de los eventos raros de la zona"),
            )
    }
}