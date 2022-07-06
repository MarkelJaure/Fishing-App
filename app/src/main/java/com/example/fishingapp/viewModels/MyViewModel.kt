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

    private var nombreEvento: String = ""
    private var tipoEvento: String = ""
    private var _dateEvento = MutableLiveData<String>()
    private var _coordenadasEvento = MutableLiveData<LatLng?>()

    val dateEvento: LiveData<String>
        get() = _dateEvento

    val date: LiveData<String>
        get() = _date

    val image: LiveData<Bitmap?>
        get() = _image

    val coordenadasReporte: LiveData<LatLng?>
        get() = _coordenadasReporte

    val coordenadasEvento: LiveData<LatLng?>
        get() = _coordenadasEvento
    fun setDate(aFecha:String){
        _date.value= aFecha
    }

    fun setDateEvento(aFecha:String){
        _dateEvento.value= aFecha
    }

    fun setImage(imagen:Bitmap?){
        _image.value= imagen
    }

    fun setCoordenadasReporte(ltlngReporte:LatLng?){
        _coordenadasReporte.value= ltlngReporte
    }

    fun setCoordenadasEvento(ltlngEvento:LatLng?){
        _coordenadasEvento.value= ltlngEvento
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

    fun getNombreEvento():String{
        return nombreEvento
    }

    fun setNombreEvento(newNombre:String){
        nombreEvento = newNombre
    }

    fun getTipoEvento(): String {
        return tipoEvento
    }

    fun setTipoEvento(newTipoEvento:String) {
        tipoEvento = newTipoEvento
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