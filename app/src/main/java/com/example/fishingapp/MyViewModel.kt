package com.example.fishingapp

import android.graphics.Bitmap
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class MyViewModel : ViewModel() {

    private var nombre: String = ""
    private var tipoPesca: String = ""
    private var _date = MutableLiveData<String>()
    private var _image = MutableLiveData<Bitmap?>()
    private lateinit var reportDetail: Report.Reporte
    private lateinit var concursoDetail: Concurso.Concurso

    val date: LiveData<String>
        get() = _date

    val image: LiveData<Bitmap?>
        get() = _image

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

    fun setImage(imagen:Bitmap){
        _image.value= imagen
    }

    fun getReportDetail(): Report.Reporte? {
        return reportDetail
    }

    fun setReportDetail(reporte:Report.Reporte){
        reportDetail = reporte
    }

    fun getConcursoDetail(): Concurso.Concurso? {
        return concursoDetail
    }

    fun setConcursoDetail(concurso:Concurso.Concurso){
        concursoDetail = concurso
    }
}