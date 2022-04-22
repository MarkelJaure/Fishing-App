package com.example.proyectovacio

import android.graphics.Bitmap
import androidx.lifecycle.ViewModel

class MyViewModel : ViewModel() {

    private var nombre: String = ""
    private var tipoPesca: String = ""
    private var date: String = ""
    private var image: Bitmap? = null

    fun getNombre():String{
        return nombre
    }

    fun setNombre(newNombre:String){
        nombre = newNombre
    }


    fun getTipoPesca(): String {
        return tipoPesca
    }

    fun setTipoPesca(newTipoPesca:String) {
        tipoPesca = newTipoPesca
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