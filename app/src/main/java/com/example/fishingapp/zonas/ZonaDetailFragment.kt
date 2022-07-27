package com.example.fishingapp.zonas

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Color
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.databinding.DataBindingUtil
import androidx.navigation.findNavController
import androidx.navigation.navGraphViewModels
import com.example.fishingapp.LoginActivity
import com.example.fishingapp.R
import com.example.fishingapp.databinding.ActivityZonaDetailBinding
import com.example.fishingapp.databinding.FragmentZonaDetailBinding
import com.example.fishingapp.viewModels.MyViewModel
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.CircleOptions
import com.google.android.gms.maps.model.LatLng


class ZonaDetailFragment : Fragment(), OnMapReadyCallback {

    private lateinit var binding: FragmentZonaDetailBinding
    private val model: MyViewModel by navGraphViewModels(R.id.navigation)

    private lateinit var mMap: GoogleMap


        override fun onCreateView(
                inflater: LayoutInflater, container: ViewGroup?,
                savedInstanceState: Bundle?
        ): View {
            binding = DataBindingUtil.inflate(inflater, R.layout.fragment_zona_detail,container,false)
            binding.lifecycleOwner = this
            binding.model = model

            val view = binding.root

            binding.zonaNombreTextView.text = model.getGeoEventDetail().nombre
            binding.zonaDescripcionTextView.text = model.getGeoEventDetail().descripcion
            binding.radiusTextView.text = "Radio: ${model.getGeoEventDetail().radius}m"
            binding.homeBackButton.setOnClickListener {
                view.findNavController().navigate(R.id.action_ZonaDetailFragment_to_homeFragment)
            }

            val mapFragment = childFragmentManager.findFragmentById(R.id.mapReportHome) as SupportMapFragment
            mapFragment.getMapAsync(this)

            return view
        }


    private fun setPermission() : Boolean {
        return ContextCompat.checkSelfPermission(requireContext(),
                Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED
    }


    override fun onMapReady(googleMap: GoogleMap) {
        mMap = googleMap
        mMap.clear()
        if(setPermission()) {
            mMap.isMyLocationEnabled = true
        }

        mMap.animateCamera(
                CameraUpdateFactory.newLatLngZoom(
                        LatLng(
                                model.getGeoEventDetail().latitud,
                                model.getGeoEventDetail().longitud
                        ), (12 + ((500 - model.getGeoEventDetail().radius) / 100)).toFloat()
                )
        )

        mMap.addCircle(
                CircleOptions()
                        .center(LatLng(model.getGeoEventDetail().latitud, model.getGeoEventDetail().longitud))
                        .radius(model.getGeoEventDetail().radius)
                        .strokeColor(Color.parseColor("#D8005011"))
                        .fillColor(Color.parseColor("#9C85F685"))
        )
    }
}