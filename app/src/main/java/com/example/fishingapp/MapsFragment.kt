package com.example.fishingapp

import android.Manifest
import android.content.pm.PackageManager
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.navigation.navGraphViewModels
import com.example.fishingapp.databinding.FragmentMapsBinding
import com.example.fishingapp.viewModels.MyViewModel
import com.example.fishingapp.viewModels.ReporteViewModel
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions

var REQUEST_ACCESS_LOCATION = 1

class MapsFragment : Fragment(), OnMapReadyCallback {

    private lateinit var mMap: GoogleMap
    private lateinit var binding: FragmentMapsBinding
    private val model: MyViewModel by navGraphViewModels(R.id.navigation)
    private val reporteModel: ReporteViewModel by navGraphViewModels(R.id.navigation)

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_maps,container,false)
        binding.lifecycleOwner = this
        binding.model = model
        val view = binding.root

        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        val mapFragment = childFragmentManager.findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(this)

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
        if(reporteModel.allReportes.value != null) {
            for (reporte in reporteModel.allReportes.value!!) {
                this.mMap.addMarker(MarkerOptions().position(LatLng(reporte.latitud, reporte.longitud)))
            }
        }
    }

    private fun editReport() {
        if(model.getReportDetail() != null) {
            model.setCoordenadasReporte(
                LatLng(model.getReportDetail()!!.latitud, model.getReportDetail()!!.longitud)
            )
        }

        if(model.coordenadasReporte.value != null) {
            this.mMap.addMarker(MarkerOptions().position(model.coordenadasReporte.value!!))
        }
        this.mMap.setOnMapClickListener {
            mMap.clear()
            this.mMap.addMarker(MarkerOptions().position(it))
            model.setCoordenadasReporte(it)
        }
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
    }

    override fun onStop() {
        super.onStop()
        model.setFilterReport(false)
    }
}