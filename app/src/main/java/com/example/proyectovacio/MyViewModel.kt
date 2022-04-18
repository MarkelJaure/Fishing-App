package com.example.proyectovacio

import android.graphics.Bitmap
import androidx.lifecycle.ViewModel

class MyViewModel : ViewModel() {

    private var nombre: String = ""
    private var seleccionUser: String = ""
    private var date: String = ""
    private var image: Bitmap? = null

    fun getNombre():String{
        return nombre
    }

    fun setNombre(newNombre:String){
        nombre = newNombre
    }


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

    fun getImage(): Bitmap? {
        return image
    }

    fun setImage(imagen:Bitmap){
        image = imagen
    }
}