package com.example.fishingapp.evento

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.navigation.navGraphViewModels
import com.example.fishingapp.R
import com.example.fishingapp.databinding.FragmentEventoItemBinding
import com.example.fishingapp.viewModels.MyViewModel
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.ktx.storage
import java.io.File
import java.util.*
import kotlin.math.max
import kotlin.math.min


class EventoItemFragment: Fragment(), OnMapReadyCallback {

    private lateinit var binding: FragmentEventoItemBinding
    private val model: MyViewModel by navGraphViewModels(R.id.navigation)
    private lateinit var mMap: GoogleMap
    private  var imagesBitmaps: List<Bitmap> = listOf();
    val mTimer = Timer()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_evento_item,container,false)
        binding.lifecycleOwner = this
        binding.model = model
        val view = binding.root

        val mapFragment = childFragmentManager.findFragmentById(R.id.mapReport) as SupportMapFragment
        mapFragment.getMapAsync(this)

        binding.confirmNombreTextView.text = model.getEventoDetail()?.nombre
        binding.confirmTipoEventoTextView.text = model.getEventoDetail()?.tipoEvento
        binding.confirmDateTextView.text = model.getEventoDetail()?.date
        if(!model.getEventoDetail()?.images.isNullOrEmpty()) {

            for (image in model.getEventoDetail()!!.images){
                val imageRef = Firebase.storage.getReferenceFromUrl("gs://fishingapp-44a54.appspot.com/eventos/" + image)
                val localFile = File.createTempFile("images", "jpg")
                imageRef.getFile(localFile).addOnSuccessListener {
                    var finalBitmap = BitmapFactory.decodeFile(localFile.absolutePath)

                    if (imagesBitmaps.isNullOrEmpty())
                    {binding.confirmImageView.setImageBitmap(finalBitmap)}

                    imagesBitmaps = imagesBitmaps.plus(finalBitmap)
                }.addOnFailureListener {
                    binding.confirmImageView.setBackgroundResource(R.drawable.reporte_default)
                }
            }


            var position = -1
            mTimer.schedule(object : TimerTask() {
                override fun run() {

                    activity!!.runOnUiThread {
                        Log.w("Timer","TIME")
                        if (!imagesBitmaps.isNullOrEmpty()){
                            Log.w("ImageBitmap","Ya no es null")
                            position++

                            if (position >= imagesBitmaps.size) position = 0

                           model.setVisibleFoto(position)
                        }

                    }
                }
            }, 0, 3000)


        } else {
            binding.confirmImageView.setBackgroundResource(R.drawable.reporte_default)
        }

        model.setVisibleFoto(0)
        model.visibleFoto.observe(viewLifecycleOwner) { aVisibleFoto ->
            Log.w("FOTOS", imagesBitmaps.toString())
            Log.w("Visible", model.visibleFoto.value!!.toString())
            if (!imagesBitmaps.isNullOrEmpty()){
                binding.confirmImageView.setImageBitmap(imagesBitmaps[aVisibleFoto])
            }
        }
//        binding.prevFotoButton.setOnClickListener{ model.setVisibleFoto(max(model.visibleFoto.value!! - 1, 0))}
//        binding.nextFotoButton.setOnClickListener{
//            model.setVisibleFoto(min((model.visibleFoto.value!! + 1), (imagesBitmaps.size -1)))
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

    override fun onDestroy() {
        super.onDestroy()
        if (mTimer != null) {
            mTimer.cancel()
        }
    }
}

