package com.example.fishingapp.viewModels

import android.graphics.Bitmap
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.fishingapp.models.Concurso
import com.example.fishingapp.models.Reporte
import com.google.android.gms.maps.model.LatLng

class MyViewModel : ViewModel() {

    private var nombre: String = ""
    private var tipoPesca: String = ""
    private var tipoEspecie: String = ""
    private var _date = MutableLiveData<String>()
    private var _image = MutableLiveData<Bitmap?>()
    private var _coordenadasReporte = MutableLiveData<LatLng?>()
    private var reportDetail: Reporte? = null
    private lateinit var concursoDetail: Concurso
    private var editReport: Boolean = false
    private var filterReport: Boolean = false

    val date: LiveData<String>
        get() = _date

    val image: LiveData<Bitmap?>
        get() = _image

    val coordenadasReporte: LiveData<LatLng?>
        get() = _coordenadasReporte

    fun setDate(aFecha:String){
        _date.value= aFecha
    }

    fun setImage(imagen:Bitmap?){
        _image.value= imagen
    }

    fun setCoordenadasReporte(ltlngReporte:LatLng?){
        _coordenadasReporte.value= ltlngReporte
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

    fun getTipoEspecie(): String {
        return tipoEspecie
    }

    fun setTipoEspecie(newTipoEspecie:String) {
        tipoEspecie = newTipoEspecie
    }

    fun getReportDetail(): Reporte? {
        return reportDetail
    }

    fun setReportDetail(reporte: Reporte?){
        reportDetail = reporte
    }

    fun getConcursoDetail(): Concurso {
        return concursoDetail
    }

    fun setConcursoDetail(concurso: Concurso){
        concursoDetail = concurso
    }

    fun getEditReport():Boolean{
        return editReport
    }

    fun setEditReport(newState: Boolean) {
        editReport = newState
    }

    fun getFilterReport():Boolean{
        return filterReport
    }

    fun setFilterReport(newState: Boolean) {
        filterReport = newState
    }
}