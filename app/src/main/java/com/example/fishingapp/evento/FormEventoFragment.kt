package com.example.fishingapp.evento

import android.app.Activity
import android.content.Intent
import android.graphics.Bitmap
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.navigation.findNavController
import androidx.navigation.navGraphViewModels
import com.example.fishingapp.DatePicker
import com.example.fishingapp.R
import com.example.fishingapp.databinding.FragmentFormEventoBinding
import com.example.fishingapp.lib.ImageStorage
import com.example.fishingapp.models.Evento
import com.example.fishingapp.reportes.REQUEST_IMAGE_CAPTURE
import com.example.fishingapp.viewModels.EventoViewModel
import com.example.fishingapp.viewModels.MyViewModel
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import com.google.firebase.firestore.FirebaseFirestore
import java.io.File
import java.text.SimpleDateFormat
import java.util.*

class FormEventoFragment : Fragment(), OnMapReadyCallback {

    private lateinit var binding: FragmentFormEventoBinding

    private lateinit var opcionesDropdown: Array<String>
    private lateinit var mMap: GoogleMap

    private val model: MyViewModel by navGraphViewModels(R.id.navigation)
    private val eventoModel: EventoViewModel by navGraphViewModels(R.id.navigation)

    private val mDatePickerDialogFragment = DatePicker(2)
    private val imageStorage: ImageStorage = ImageStorage()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = DataBindingUtil.inflate(inflater,R.layout.fragment_form_evento,container,false)
        binding.lifecycleOwner = this
        binding.model = model
        val view = binding.root

        val mapFragment = childFragmentManager.findFragmentById(R.id.eventoMapReport) as SupportMapFragment
        mapFragment.getMapAsync(this)

        binding.eventoNombreTextView.setText(model.getNombreEvento())
        binding.tipoEventoTextView.setText(model.getTipoEvento())

        if(model.dateEvento.value == null || model.dateEvento.value == "") {
            val selectedDate = SimpleDateFormat("dd/MM/yyyy", Locale.ENGLISH).format(Calendar.getInstance().time)
            model.setDateEvento(selectedDate.toString())
        }

        opcionesDropdown = resources.getStringArray(R.array.typesEventos)
        binding.tipoEventoTextView.setAdapter(
            ArrayAdapter(view.context,
            R.layout.dropdown_item, opcionesDropdown)
        )

        binding.eventoDateButton.setOnClickListener{ selectDate()}
        binding.eventoInsertButton.setOnClickListener{ sendEvento(view)}
        binding.eventoMapButton.setOnClickListener{
            view.findNavController().navigate(R.id.action_formEventFragment_to_eventoMapsFragment)
        }
        binding.eventoFotoButton.setOnClickListener{ dispatchTakePictureIntent()}

        eventoModel.allEventos.observe(viewLifecycleOwner) { eventos ->
            Log.w("reportes room", eventos.toString())
        }

        return view
    }

    private fun dispatchTakePictureIntent() {
        Intent(MediaStore.ACTION_IMAGE_CAPTURE).also { takePictureIntent ->
            activity?.let {
                takePictureIntent.resolveActivity(it.packageManager).also {
                    startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE)
                }
            }
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == Activity.RESULT_OK) {
            val imageBitmap = data?.extras?.get("data") as Bitmap

            var tmpImages = model.imagesEvento.value
            if (tmpImages ==null){
                tmpImages = listOf<Bitmap>()
            }
            tmpImages = tmpImages.plus(imageBitmap)
            model.setImagesEvento(tmpImages)
            Log.w("CantidadImagenes", model.imagesEvento.value?.size.toString())
        }
    }

    private fun selectDate() {
        mDatePickerDialogFragment.show(parentFragmentManager, "DATE PICK")
    }

    private fun sendEvento(view: View) {
        model.setNombreEvento("${binding.eventoNombreTextView.text}")
        model.setTipoEvento("${binding.tipoEventoTextView.text}")
        Log.i("DateEvento", model.date.value.toString())

        var missingRequiredInput = checkRequiredInputs()
        if (missingRequiredInput.isEmpty()){
            saveEvento(view)
        }else {
            val msj = Toast.makeText(
                    activity,
                missingRequiredInput,
            Toast.LENGTH_LONG)
            msj.show()
        }
    }

    fun checkRequiredInputs(): String{
        if (model.getNombreEvento().isEmpty()){
            return "Completar la descripcion del evento"
        }

        if (model.getTipoEvento().isEmpty()){
            return "Seleccionar un tipo de Evento"
        }

        if (model.coordenadasEvento.value == null){
            return "Seleccionar una ubicacion del evento"
        }
        return ""
    }

    fun saveEvento(view: View){
       var pictures = listOf<String>()
        if (model.imagesEvento.value !== null && !model.imagesEvento.value.isNullOrEmpty()) {
            for (image in model.imagesEvento.value!!){
                var file =
                    imageStorage.storeImageOnLocal(image!!,requireActivity().packageName) //Se guarda en /Android/data/com.example.fishingapp/files
                Log.w(
                "Imagen guardada en room",
                    file.toString()
                ) // Echo: /storage/emulated/0/Android/data/com.example.fishingapp/Files/MI_14052022_1844.png
                //Si tarda en verse el cambio en la carpeta es por que tarda en guardar el png
                pictures = pictures.plus(file.toString())
                if (file != null) {
                    imageStorage.uploadImageToFirebase(file)
                }
            }
        }
        Log.w("Pictures", pictures.toString())
            var newEvento = Evento(
                model.getNombreEvento(),
                model.getTipoEvento(),
                model.dateEvento.value.toString(),
                pictures,
                model.coordenadasEvento.value!!.latitude,
                model.coordenadasEvento.value!!.longitude
            )
            eventoModel.insert(newEvento)

            var imagenes = listOf<String>()
            if(!pictures.isNullOrEmpty()) {
                for (picture in pictures){
                    imagenes = imagenes.plus(Uri.fromFile(File(picture)).lastPathSegment.toString())
                }

            }

            val data = hashMapOf<String, Any>(
                "nombre" to model.getNombreEvento(),
                "tipoEvento" to model.getTipoEvento(),
                "date" to  model.dateEvento.value.toString(),
                "imagenes" to imagenes,
                "latitud" to  model.coordenadasEvento.value!!.latitude,
                "longitud" to model.coordenadasEvento.value!!.longitude,
            )

            FirebaseFirestore.getInstance().collection("eventos").add(data)
                .addOnCompleteListener { Log.w("evento - exito", it.toString()) }
                .addOnFailureListener { Log.w("evento - fallo", it.toString()) }

        clearEventOnViewModel()
    }

    override fun onResume() {
        super.onResume()
        binding.tipoEventoTextView.setAdapter(ArrayAdapter(
            requireView().context,
            R.layout.dropdown_item, opcionesDropdown))
    }

    fun clearEventOnViewModel(){
        model.setNombreEvento("")
        model.setTipoEvento("")
        //model.setImageEvento(null)
        val selectedDate = SimpleDateFormat("dd/MM/yyyy", Locale.ENGLISH).format(Calendar.getInstance().time)
        model.setDateEvento(selectedDate.toString())
    }

    override fun onMapReady(googleMap: GoogleMap) {
        mMap = googleMap
        mMap.clear()

        var coordenadas: LatLng
        if(model.coordenadasEvento.value != null) {
            coordenadas = model.coordenadasEvento.value!!
            mMap.addMarker(MarkerOptions().position(coordenadas))
            mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(coordenadas, 10f))
        }
    }
}