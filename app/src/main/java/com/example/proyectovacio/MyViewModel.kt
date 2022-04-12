package com.example.proyectovacio

import androidx.lifecycle.ViewModel

class MyViewModel : ViewModel() {

    private var seleccionUser: String = ""

    fun getSeleccionUser(): String {
        return seleccionUser
    }

    fun setSeleccionUser(nuevaSeleccion:String) {
        seleccionUser = nuevaSeleccion
    }
}