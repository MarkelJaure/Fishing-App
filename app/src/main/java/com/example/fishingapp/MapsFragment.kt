package com.example.fishingapp

import android.Manifest
import android.content.pm.PackageManager
import android.graphics.Color
import android.location.Location
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.core.view.isVisible
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.navigation.findNavController
import androidx.navigation.navGraphViewModels
import com.example.fishingapp.databinding.FragmentMapsBinding
import com.example.fishingapp.models.Reporte
import com.example.fishingapp.viewModels.MyViewModel
import com.example.fishingapp.viewModels.ReporteViewModel
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.*
import com.google.android.material.datepicker.MaterialDatePicker
import java.text.SimpleDateFormat
import java.util.*
import kotlin.math.max
import kotlin.math.min

var REQUEST_ACCESS_LOCATION = 1

class MapsFragment : Fragment(), OnMapReadyCallback {

    private lateinit var mMap: GoogleMap
    private lateinit var binding: FragmentMapsBinding
    private val model: MyViewModel by navGraphViewModels(R.id.navigation)
    private val reporteModel: ReporteViewModel by navGraphViewModels(R.id.navigation)
    private val datePicker = MaterialDatePicker.Builder.dateRangePicker().build()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        Log.w("Crando mapa","Entraste al Map fragment")
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_maps,container,false)
        binding.lifecycleOwner = this
        binding.model = model
        val view = binding.root

        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        val mapFragment = childFragmentManager.findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(this)

        binding.mapToolBar.isVisible = model.getFilterReport()
        setVisibilityUbicationFilterButtons(false)
        //Setear acciones de los botones
        binding.mapToolBar.setOnMenuItemClickListener {
            when (it.itemId) {
                R.id.DateFilter -> {
                    datePicker.show(parentFragmentManager, "DATE PICK")
                    datePicker.addOnPositiveButtonClickListener { selection ->
                        reporteModel.setInitDate(selection.first)
                        reporteModel.setFinishDate(selection.second)
                        reporteModel.setIsDateFilterApplied(true)
                    }
                    true
                }
                R.id.QuitDateFilter -> {
                    reporteModel.setInitDate(null)
                    reporteModel.setFinishDate(null)
                    reporteModel.setIsDateFilterApplied(false)
                    true
                }
                R.id.UbicacionFilter -> {
                    mMap.setOnMapClickListener {
                        ubicacionFilter(it)
                    }
                    setVisibilityUbicationFilterButtons(true)
                    true
                }
                R.id.QuitUbicacionFilter -> {
                    CancelUbicationFilter()
                    true
                }
                else -> super.onOptionsItemSelected(it)
            }
        }

        binding.ZoomInCircleButton.setOnClickListener{ ZoomInUbicationCircle()}
        binding.ZoomOutCircleButton.setOnClickListener{ ZoomOutUbicationCircle()}
        binding.ApllyUbicationFilterButton.setOnClickListener{ ApplyUbicationFilter()}
        binding.CancelFilterButton.setOnClickListener{ CancelUbicationFilter()}

        return view
    }

    private fun setPermission() : Boolean {
        return ContextCompat.checkSelfPermission(requireActivity(),
            Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED
    }

    private fun activateLocation() {
        if(setPermission()) {
            mMap.isMyLocationEnabled = true
        }
        else {
            requestPermissions(arrayOf<String>(Manifest.permission.ACCESS_FINE_LOCATION), REQUEST_ACCESS_LOCATION)
        }
    }
    var markerToFilter: Marker? = null;
    var circleToFilter: Circle? = null;
    var circleRadio = 1000.0;
    private fun ubicacionFilter(aPosition: LatLng){
            markerToFilter?.remove();
            markerToFilter = mMap.addMarker(
                MarkerOptions()
                    .position(aPosition)
                    .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_GREEN)))
            circleToFilter?.remove();
            circleToFilter = mMap.addCircle(
                CircleOptions()
                    .center(aPosition)
                    .radius(circleRadio)
                    .strokeColor(Color.parseColor("#D8005011"))
                    .fillColor(Color.parseColor("#9C85F685"))
            )
    }

    private fun setVisibilityUbicationFilterButtons(aValue:Boolean){
        binding.ZoomInCircleButton.isVisible= aValue;
        binding.ZoomOutCircleButton.isVisible= aValue;
        binding.CancelFilterButton.isVisible= aValue;
        binding.ApllyUbicationFilterButton.isVisible= aValue;

    }

    private fun ZoomInUbicationCircle(){
        if (circleToFilter !== null){
            circleToFilter!!.radius= min(circleToFilter!!.radius + 100.0,10000.0);
            circleRadio = circleToFilter!!.radius
            Log.w("Radius", circleToFilter!!.radius.toString())
        }
    }

    private fun ZoomOutUbicationCircle(){
        if (circleToFilter !==null){
            circleToFilter!!.radius=max(circleToFilter!!.radius - 100.0,100.0);
            circleRadio = circleToFilter!!.radius
            Log.w("Radius", circleToFilter!!.radius.toString())
        }
    }

    private fun ApplyUbicationFilter(){
        if (circleToFilter !==null){
            reporteModel.setRadius(circleToFilter!!.radius)
            reporteModel.setCenterPoint(markerToFilter!!.position)
            reporteModel.setIsUbicationFilterApplied(true);
            setVisibilityUbicationFilterButtons(false);
        } else{
            Toast.makeText(context, "Seleccione un area para filtrar", Toast.LENGTH_SHORT).show()
        }

        Log.w("Apply Filter", reporteModel.isUbicationFilterApplied.value.toString())
    }

    private fun CancelUbicationFilter(){
        Log.w("Cancel filter", "Cancelando filtro")
        mMap.setOnMapClickListener (null)
        reporteModel.setIsUbicationFilterApplied(false);
        setVisibilityUbicationFilterButtons(false);
    }

    private fun isMarkerInsideCircle(
        centerLatLng: LatLng,
        draggedLatLng: LatLng,
        radius: Double
    ): Boolean {
        val distances = FloatArray(1)
        Location.distanceBetween(
            centerLatLng.latitude,
            centerLatLng.longitude,
            draggedLatLng.latitude,
            draggedLatLng.longitude, distances
        )
        Log.w("Distancia entre puntos", distances[0].toString())
        return radius >= distances[0]
    }
    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<String>,
        grantResults: IntArray
    ) {
        if(requestCode == REQUEST_ACCESS_LOCATION) {
            if(grantResults.contains(PackageManager.PERMISSION_GRANTED)) {
                activateLocation()
            }
        }
    }


    private fun filterReport() {
        reporteModel.allReportes.observe(viewLifecycleOwner) { reportes ->
            if(!reportes.isNullOrEmpty()) {
                var reportesFiltrados: List<Reporte> = checkReportFilters()
                for (reporte in reportesFiltrados) {
                    var snippet = String.format(
                        Locale.getDefault(),
                        "%1$.11s, %2$.11s, %3$.11s",
                        reporte.tipoEspecie,
                        reporte.tipoPesca,
                        reporte.date
                    )

                    mMap.addMarker(MarkerOptions()
                        .position(LatLng(reporte.latitud, reporte.longitud))
                        .title(reporte.nombre)
                        .snippet(snippet))

                    var minLat: Double? = null
                    var maxLat: Double? = null
                    var minLng: Double? = null
                    var maxLng: Double? = null

                    for (reporte in reportesFiltrados) {

                        if (minLat ==  null || reporte.latitud < minLat){
                            minLat = reporte.latitud
                        }
                        if (maxLat ==  null || reporte.latitud > maxLat){
                            maxLat = reporte.latitud
                        }
                        if (minLng ==  null || reporte.longitud < minLng){
                            minLng = reporte.longitud
                        }
                        if (maxLng ==  null || reporte.longitud > maxLng){
                            maxLng = reporte.longitud
                        }
                    }

                    if (reportesFiltrados.size == 1){
                        mMap.animateCamera(
                            CameraUpdateFactory.newLatLngZoom(
                                LatLng(
                                    minLat!!,
                                    minLng!!
                                ), 11F
                            )
                        )
                    } else{
                        val promedyBound = LatLngBounds(
                            LatLng(minLat!!, minLng!!),  // SW bounds
                            LatLng(maxLat!!, maxLng!!) // NE bounds
                        )
                        mMap.animateCamera(CameraUpdateFactory.newLatLngBounds(promedyBound, 150))
                    }
                }
            }
        }

    }

    private fun checkReportFilters(): List<Reporte> {
        var reportesFiltrados: List<Reporte> = reporteModel.allReportes.value!!

        if (reporteModel.isDateFilterApplied.value == true) {
            reportesFiltrados = filterByDate(reportesFiltrados)
        }

        if (reporteModel.isUbicationFilterApplied.value == true) {
            Log.w("Radio", reporteModel.radius.value.toString())
            reportesFiltrados = filterByUbication(reportesFiltrados)
        }

        return reportesFiltrados
    }

    private fun filterByDate(someReportes: List<Reporte>): List<Reporte> {
        return someReportes.filter { reporte ->
            val dateMilis = SimpleDateFormat("dd/MM/yyyy").parse(reporte.date).time
            dateMilis >= reporteModel.initDate.value!! && dateMilis <= reporteModel.finishDate.value!!
        }
    }

    private fun filterByUbication(someReportes: List<Reporte>): List<Reporte> {
        return someReportes.filter { reporte ->
            isMarkerInsideCircle(
                reporteModel.centerPoint.value!!,
                LatLng(reporte.latitud, reporte.longitud),
                reporteModel.radius.value!!
            )
        }
    }

    fun updateCoordenadas(): Boolean {
        if(model.coordenadasReporte.value == null || model.getReportDetail() == null || model.getReportDetail()!!.latitud == null)
            return false;

        if(model.coordenadasReporte.value!!.latitude != model.getReportDetail()!!.latitud
                && model.coordenadasReporte.value!!.longitude != model.getReportDetail()!!.longitud)
            return true;
        return false;
    }

    private fun editReport() {
        var marker: Marker? = null
        if(model.getReportDetail() != null){
            if(updateCoordenadas())
                marker = mMap.addMarker(MarkerOptions().position(model.coordenadasReporte.value!!))!!
            else
                marker = mMap.addMarker(MarkerOptions().position(LatLng(model.getReportDetail()!!.latitud, model.getReportDetail()!!.longitud)))!!
        } else {
            if(model.coordenadasReporte.value != null)
                marker = mMap.addMarker(MarkerOptions().position(model.coordenadasReporte.value!!))!!
        }

        if(marker != null)
            mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(marker!!.position, 15f))
        else{
            val argentinaBounds = LatLngBounds(
                    LatLng((-54.0), -75.0),  // SW bounds
                    LatLng((-40.0), -50.0) // NE bounds
            )
            mMap.moveCamera(CameraUpdateFactory.newLatLngBounds(argentinaBounds, 0))
        }

        mMap.setOnMapClickListener {
            mMap.clear()
            mMap.addMarker(MarkerOptions().position(it))
            model.setCoordenadasReporte(it)
        }
    }



    override fun onMapReady(googleMap: GoogleMap) {
        mMap = googleMap
        mMap.clear()

        activateLocation()

        if(model.getFilterReport()) {
            filterReport()
        }
        else {
            editReport()
        }

        //Observacion de la fecha a filtrar
        reporteModel.isDateFilterApplied.observe(viewLifecycleOwner) { value ->
            mMap.clear()
            filterReport()

            binding.mapToolBar.menu.findItem(R.id.QuitDateFilter).isVisible = value
        }

        //Observacion de la ubicacion a filtrar
        reporteModel.isUbicationFilterApplied.observe(viewLifecycleOwner) { value ->
            mMap.clear()
            filterReport()

            binding.mapToolBar.menu.findItem(R.id.QuitUbicacionFilter).isVisible = value
        }
    }


    override fun onPause() {
        super.onPause()
        mMap.clear()
    }

    override fun onResume() {
        binding.mapToolBar.isVisible = model.getFilterReport()
        super.onResume()
    }
}