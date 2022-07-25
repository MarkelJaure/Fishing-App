package com.example.fishingapp.zonas

import android.Manifest
import android.annotation.SuppressLint
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Color
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import com.example.fishingapp.LoginActivity
import com.example.fishingapp.MainActivity
import com.example.fishingapp.R
import com.example.fishingapp.databinding.ActivityZonaDetailBinding
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.CircleOptions
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.LatLngBounds
import com.google.android.gms.maps.model.MarkerOptions


class ZonaDetailActivity : AppCompatActivity(), OnMapReadyCallback {

    private lateinit var binding: ActivityZonaDetailBinding

    private lateinit var nombre: String
    private lateinit var descripcion: String
    private  var latitud: Double = 0.0
    private  var longitud: Double = 0.0
    private  var radius: Double= 0.0
    private lateinit var mMap: GoogleMap

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityZonaDetailBinding.inflate(layoutInflater)
        val view = binding.root


        nombre = intent.getStringExtra("nombre").toString()
        descripcion = intent.getStringExtra("descripcion").toString()
        latitud = intent.getDoubleExtra("latitud",0.0)
        longitud = intent.getDoubleExtra("longitud",0.0)
        radius = intent.getDoubleExtra("radius",0.0)

        binding.zonaNombreTextView.text = nombre
        binding.zonaDescripcionTextView.text = descripcion
        binding.radiusTextView.text = "Radio: ${radius.toInt()}m"
        binding.homeBackButton.setOnClickListener {
            startActivity(Intent(this, LoginActivity::class.java))
        }

        val mapFragment = supportFragmentManager.findFragmentById(R.id.mapReportHome) as SupportMapFragment
        mapFragment.getMapAsync(this)


        setContentView(view)
    }
    private fun setPermission() : Boolean {
        return ContextCompat.checkSelfPermission(this,
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
                    latitud,
                    longitud
                ), (12 + ((500 - radius) / 100)).toFloat()
            )
        )

        mMap.addCircle(
            CircleOptions()
                .center(LatLng(latitud, longitud))
                .radius(radius)
                .strokeColor(Color.parseColor("#D8005011"))
                .fillColor(Color.parseColor("#9C85F685"))
        )
    }
}