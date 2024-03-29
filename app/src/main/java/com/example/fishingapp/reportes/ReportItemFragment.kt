package com.example.fishingapp.reportes

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
import com.example.fishingapp.databinding.FragmentReportItemBinding
import com.example.fishingapp.viewModels.MyViewModel
import com.example.fishingapp.viewModels.ReporteViewModel
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.ktx.storage
import java.io.File

class ReportItemFragment: Fragment(), OnMapReadyCallback {

    private lateinit var binding: FragmentReportItemBinding
    private val model: MyViewModel by navGraphViewModels(R.id.navigation)
    private lateinit var mMap: GoogleMap

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_report_item,container,false)
        binding.lifecycleOwner = this
        binding.model = model
        val view = binding.root

        val mapFragment = childFragmentManager.findFragmentById(R.id.mapReport) as SupportMapFragment
        mapFragment.getMapAsync(this)

        binding.confirmNombreTextView?.text = model.getReportDetail()?.nombre
        binding.confirmTipoPescaTextView?.text = model.getReportDetail()?.tipoPesca
        binding.confirmTipoEspecieTextView?.text = model.getReportDetail()?.tipoEspecie
        binding.confirmDateTextView?.text = model.getReportDetail()?.date

        if(model.getReportDetail()?.image != "") {
            val imageRef = Firebase.storage.getReferenceFromUrl("gs://fishingapp-44a54.appspot.com/reportes/" + model.getReportDetail()?.image)
            val localFile = File.createTempFile("RE_", "_item")

            imageRef.getFile(localFile).addOnSuccessListener {
                binding.confirmImageView?.setImageBitmap(BitmapFactory.decodeFile(localFile.absolutePath))
            }
        }

        if(Firebase.auth.currentUser?.uid!! == model.getReportDetail()?.userID)
            binding.editButton.setVisibility(View.VISIBLE);
        else
            binding.editButton.setVisibility(View.INVISIBLE);

        binding.editButton.setOnClickListener {
            model.setEditReport(true)
            view.findNavController().navigate(R.id.formFragment)
        }

        return view
    }

    override fun onMapReady(googleMap: GoogleMap) {
        mMap = googleMap
        mMap.clear()

        var coordenadas: LatLng

        if(model.getReportDetail() != null) {
            coordenadas = LatLng(model.getReportDetail()!!.latitud, model.getReportDetail()!!.longitud)
            mMap.addMarker(MarkerOptions().position(coordenadas))
            mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(coordenadas, 10f))
        }
    }
}

