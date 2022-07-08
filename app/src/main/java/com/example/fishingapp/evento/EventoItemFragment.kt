package com.example.fishingapp.evento

import android.graphics.BitmapFactory
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.navigation.findNavController
import androidx.navigation.navGraphViewModels
import com.example.fishingapp.R
import com.example.fishingapp.databinding.FragmentEventoItemBinding
import com.example.fishingapp.databinding.FragmentReportItemBinding
import com.example.fishingapp.viewModels.MyViewModel
import com.example.fishingapp.viewModels.ReporteViewModel
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.ktx.storage
import java.io.File

class EventoItemFragment: Fragment(), OnMapReadyCallback {

    private lateinit var binding: FragmentEventoItemBinding
    private val model: MyViewModel by navGraphViewModels(R.id.navigation)
    private lateinit var mMap: GoogleMap

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_evento_item,container,false)
        binding.lifecycleOwner = this
        binding.model = model
        val view = binding.root

        val mapFragment = childFragmentManager.findFragmentById(R.id.eventoMapReport) as SupportMapFragment
        mapFragment.getMapAsync(this)

        binding.confirmNombreTextView.text = model.getEventoDetail()?.nombre
        binding.confirmTipoEventoTextView.text = model.getEventoDetail()?.tipoEvento
        binding.confirmDateTextView.text = model.getEventoDetail()?.date
//        if(model.getEventoDetail()?.image != "") {
//            val imageRef = Firebase.storage.getReferenceFromUrl("gs://fishingapp-44a54.appspot.com/reportes/" + model.getReportDetail()?.image)
//            val localFile = File.createTempFile("images", "jpg")
//
//            imageRef.getFile(localFile).addOnSuccessListener {
//                binding.confirmImageView?.setImageBitmap(BitmapFactory.decodeFile(localFile.absolutePath))
//            }.addOnFailureListener {
//                binding.confirmImageView?.setBackgroundResource(R.drawable.reporte_default)
//            }
//        } else {
//            binding.confirmImageView?.setBackgroundResource(R.drawable.reporte_default)
//        }

//        binding.editButton.setOnClickListener {
//            model.setEditReport(true)
//            view.findNavController().navigate(R.id.formFragment)
//        }

        return view
    }

    override fun onMapReady(googleMap: GoogleMap) {
        mMap = googleMap
        mMap.clear()

        var coordenadas: LatLng

        if(model.getEventoDetail() != null) {
            coordenadas = LatLng(model.getEventoDetail()!!.latitud, model.getEventoDetail()!!.longitud)
            mMap.addMarker(MarkerOptions().position(coordenadas))
            mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(coordenadas, 10f))
        }
    }
}

