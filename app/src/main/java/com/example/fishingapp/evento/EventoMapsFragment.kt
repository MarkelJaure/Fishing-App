package com.example.fishingapp.evento

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
import androidx.navigation.navGraphViewModels
import com.example.fishingapp.R
import com.example.fishingapp.databinding.FragmentMapsBinding
import com.example.fishingapp.viewModels.EventoViewModel
import com.example.fishingapp.viewModels.MyViewModel
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.*
import com.google.android.material.datepicker.MaterialDatePicker

var REQUEST_ACCESS_LOCATION = 1

class EventoMapsFragment : Fragment(), OnMapReadyCallback {

    private lateinit var mMap: GoogleMap
    private lateinit var binding: FragmentMapsBinding
    private val model: MyViewModel by navGraphViewModels(R.id.navigation)

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        Log.w("Crando mapa","Entraste al Map Evento")
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_maps,container,false)
        binding.lifecycleOwner = this
        binding.model = model
        val view = binding.root

        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        val mapFragment = childFragmentManager.findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(this)

        binding.ZoomInCircleButton.isVisible= false;
        binding.ZoomOutCircleButton.isVisible= false;
        binding.CancelFilterButton.isVisible= false;
        binding.ApllyUbicationFilterButton.isVisible= false;
        binding.mapToolBar.isVisible= false;

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

    private fun editEvento() {

        if(model.coordenadasEvento.value != null) {
            val marker = mMap.addMarker(MarkerOptions().position(model.coordenadasEvento.value!!))
            mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(model.coordenadasEvento.value!!, 15f))
        }

        mMap.setOnMapClickListener {
            mMap.clear()
            mMap.addMarker(MarkerOptions().position(it))
            model.setCoordenadasEvento(it)
        }
    }

    override fun onMapReady(googleMap: GoogleMap) {
        mMap = googleMap
        mMap.clear()
        val argentinaBounds = LatLngBounds(
            LatLng((-54.0), -75.0),  // SW bounds
            LatLng((-40.0), -50.0) // NE bounds
        )
        mMap.moveCamera(CameraUpdateFactory.newLatLngBounds(argentinaBounds, 0))

        editEvento()
        activateLocation()

    }
}