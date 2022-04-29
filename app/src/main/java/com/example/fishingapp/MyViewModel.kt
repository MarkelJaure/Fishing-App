package com.example.fishingapp

import android.graphics.Bitmap
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class MyViewModel : ViewModel() {

    private var nombre: String = ""
    private var tipoPesca: String = ""
    private var image: Bitmap? = null
    private var _date = MutableLiveData<String>()
    private lateinit var reportDetail: Report.Reporte

    val date: LiveData<String>
        get() = _date

    fun setDate(aFecha:String){
        _date.value= aFecha
    }

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

    fun getImage(): Bitmap? {
        return image
    }

    fun setImage(imagen:Bitmap){
        image = imagen
    }

    fun getReportDetail(): Report.Reporte? {
        return reportDetail
    }

    fun setReportDetail(reporte:Report.Reporte){
        reportDetail = reporte
    }
}