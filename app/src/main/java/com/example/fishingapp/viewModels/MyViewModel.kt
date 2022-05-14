package com.example.fishingapp.viewModels

import android.graphics.Bitmap
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.fishingapp.models.Concurso
import com.example.fishingapp.models.Reporte

class MyViewModel : ViewModel() {

    private var nombre: String = ""
    private var tipoPesca: String = ""
    private var _date = MutableLiveData<String>()
    private var _image = MutableLiveData<Bitmap?>()
    private var reportDetail: Reporte? = null
    private lateinit var concursoDetail: Concurso
    private var user: Boolean = false
    private var editReport: Boolean = false

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

    fun setImage(imagen:Bitmap?){
        _image.value= imagen
    }

    fun getReportDetail(): Reporte? {
        return reportDetail
    }

    fun setReportDetail(reporte: Reporte){
        reportDetail = reporte
    }

    fun getConcursoDetail(): Concurso {
        return concursoDetail
    }

    fun setConcursoDetail(concurso: Concurso){
        concursoDetail = concurso
    }

    fun getUser():Boolean{
        return user
    }

    fun setUser(newUser:Boolean){
        user = newUser
    }

    fun getEditReport():Boolean{
        return editReport
    }

    fun setEditReport(newState: Boolean) {
        editReport = newState
    }
}