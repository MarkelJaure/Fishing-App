package com.example.fishingapp

import android.Manifest
import android.content.pm.PackageManager
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.core.view.isVisible
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.navigation.findNavController
import androidx.navigation.navGraphViewModels
import com.example.fishingapp.databinding.FragmentMapsBinding
import com.example.fishingapp.models.Reporte
import com.example.fishingapp.reportes.FilterDatePicker
import com.example.fishingapp.viewModels.MyViewModel
import com.example.fishingapp.viewModels.ReporteViewModel
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.Marker
import com.google.android.gms.maps.model.MarkerOptions
import java.util.*

var REQUEST_ACCESS_LOCATION = 1

class MapsFragment : Fragment(), OnMapReadyCallback {

    private lateinit var mMap: GoogleMap
    private lateinit var binding: FragmentMapsBinding
    private val model: MyViewModel by navGraphViewModels(R.id.navigation)
    private val reporteModel: ReporteViewModel by navGraphViewModels(R.id.navigation)
    private val dateToFilter = FilterDatePicker()

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

        binding.listViewButton.setOnClickListener{ seeOnList(view)}


        binding.mapToolBar.isVisible = model.getFilterReport()
        //Setear acciones de los botones
        binding.mapToolBar.setOnMenuItemClickListener {
            when (it.itemId) {
                R.id.DateFilter -> {
                    dateToFilter.show(parentFragmentManager, "DATE PICK")
                    true
                }
                R.id.QuitDateFilter -> {
                    reporteModel.setDate("")
                    true
                }
                else -> super.onOptionsItemSelected(it)
            }
        }


        return view
    }

    private fun seeOnList(view:View){
        view.findNavController().navigate(R.id.action_MapsFragment_to_ReportListFragment)
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
        var reportesFiltrados: List<Reporte>
        if(reporteModel.allReportes.value != null) {
            if (reporteModel.date.value !== "" && reporteModel.date.value != null) {
                reportesFiltrados = reporteModel.allReportes.value!!.filter { reporte ->
                    reporte.date == reporteModel.date.value!!.toString()
                }
            }
            else {
                reportesFiltrados = reporteModel.allReportes.value!!
            }
            for (reporte in reportesFiltrados) {
                var snippet = String.format(
                    Locale.getDefault(),
                    "Tipo: %1$.11s, Date %2$.11s",
                    reporte.tipoPesca,
                    reporte.date
                )

                val marker = mMap.addMarker(MarkerOptions()
                    .position(LatLng(reporte.latitud, reporte.longitud))
                    .title(reporte.nombre)
                    .snippet(snippet))
            }
        }
    }

    private fun editReport() {
        if(model.getReportDetail() != null) {
            model.setCoordenadasReporte(
                LatLng(model.getReportDetail()!!.latitud, model.getReportDetail()!!.longitud)
            )
            if(model.coordenadasReporte.value != null) {
                val marker = mMap.addMarker(MarkerOptions().position(model.coordenadasReporte.value!!))
                mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(model.coordenadasReporte.value!!, 15f))
            }
        }
        else {
            model.setCoordenadasReporte(null)
        }

        mMap.setOnMapClickListener {
            mMap.clear()
            mMap.addMarker(MarkerOptions().position(it))
            model.setCoordenadasReporte(it)
        }
    }

    fun onMarkerClick(marker: Marker): Boolean {
        marker.showInfoWindow()
        return false
    }

    override fun onMapReady(googleMap: GoogleMap) {
        mMap = googleMap
        mMap.clear()

        if(model.getFilterReport()) {
            filterReport()
        }
        else {
            editReport()
        }

        activateLocation()

        //Observacion de la fecha a filtrar
        reporteModel.date.observe(viewLifecycleOwner) { date ->
            mMap.clear()
            filterReport()
            binding.mapToolBar.menu.findItem(R.id.QuitDateFilter).isVisible = (reporteModel.date.value !== "" && reporteModel.date.value != null)
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