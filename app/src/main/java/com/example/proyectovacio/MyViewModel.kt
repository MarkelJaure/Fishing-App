package com.example.proyectovacio

import androidx.lifecycle.ViewModel

class MyViewModel : ViewModel() {

    private var seleccionUser: String = ""
    private var date: String = ""

    fun getSeleccionUser(): String {
        return seleccionUser
    }

    fun setSeleccionUser(nuevaSeleccion:String) {
        seleccionUser = nuevaSeleccion
    }

    fun getDate(): String {
        return date
    }

    fun setDate(newDate:String) {
        date = newDate
    }
}