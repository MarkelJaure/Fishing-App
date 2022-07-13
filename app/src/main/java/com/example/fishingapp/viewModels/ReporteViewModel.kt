package com.example.fishingapp.viewModels

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.fishingapp.database.FishingRoomDatabase
import com.example.fishingapp.models.Reporte
import com.example.fishingapp.repositorio.ReporteRepositorio
import com.google.android.gms.maps.model.LatLng
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class ReporteViewModel(application: Application) : AndroidViewModel(application) {
    private val repository: ReporteRepositorio
    val allReportes: LiveData<List<Reporte>>

    //Filtros:

    //DateFilter
    private var _initDate = MutableLiveData<Long?>()
    private var _finishDate = MutableLiveData<Long?>()
    private var _isDateFilterApplied = MutableLiveData<Boolean>()

    //UbicationFilter
    private var _centerPoint = MutableLiveData<LatLng>()
    private var _radius = MutableLiveData<Double>()
    private var _isUbicationFilterApplied = MutableLiveData<Boolean>()


    val initDate: MutableLiveData<Long?>
        get() = _initDate

    fun setInitDate(aFecha: Long?){
        _initDate.value= aFecha
    }
    val finishDate: MutableLiveData<Long?>
        get() = _finishDate

    fun setFinishDate(aFecha: Long?){
        _finishDate.value= aFecha
    }

    val centerPoint: LiveData<LatLng>
        get() = _centerPoint

    fun setCenterPoint(aCenterPoint:LatLng){
        _centerPoint.value= aCenterPoint
    }

    val radius: LiveData<Double>
        get() = _radius

    fun setRadius(aRadius:Double){
        _radius.value= aRadius
    }

    val isUbicationFilterApplied: LiveData<Boolean>
        get() = _isUbicationFilterApplied

    fun setIsUbicationFilterApplied(aIsUbicationFilterApplied:Boolean){
        _isUbicationFilterApplied.value= aIsUbicationFilterApplied
    }

    val isDateFilterApplied: LiveData<Boolean>
        get() = _isDateFilterApplied

    fun setIsDateFilterApplied(aIsDateFilterApplied:Boolean){
        _isDateFilterApplied.value= aIsDateFilterApplied
    }

    init {
        val reporteDao = FishingRoomDatabase.getDatabase(application, viewModelScope).reporteDao()

        repository = ReporteRepositorio(reporteDao)
        allReportes = repository.allReportes
    }

    fun insert(reporte: Reporte) = viewModelScope.launch(Dispatchers.IO) {
        repository.insert(reporte)
    }

    fun load(reporte: Reporte) = viewModelScope.launch(Dispatchers.IO) {
        repository.insert(reporte)
    }

    fun borrarTodos() = viewModelScope.launch(Dispatchers.IO) {
        repository.borrarTodos()
    }

    fun update(reporte: Reporte) = viewModelScope.launch(Dispatchers.IO) {
        repository.update(reporte)
    }
}