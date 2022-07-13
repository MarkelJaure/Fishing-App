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
import androidx.core.view.isVisible
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
import kotlin.math.max
import kotlin.math.min

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

        model.imagesEvento.observe(viewLifecycleOwner) { images ->
            checkVisibilityOfButtons()
            if (!model.imagesEvento.value.isNullOrEmpty()){
                binding.eventoImageView.setImageBitmap(images[model.visibleFoto.value!!])}

        }
        model.visibleFoto.observe(viewLifecycleOwner) { aVisibleFoto ->
            if (!model.imagesEvento.value.isNullOrEmpty()){
                binding.eventoImageView.setImageBitmap(model.imagesEvento.value!![aVisibleFoto])
            }
            checkVisibilityOfButtons()
        }
        binding.prevFotoButton.setOnClickListener{ model.setVisibleFoto(max(model.visibleFoto.value!! - 1, 0))}
        binding.nextFotoButton.setOnClickListener{
            model.setVisibleFoto(min((model.visibleFoto.value!! + 1), (model.imagesEvento.value!!.size -1)))
        }

        binding.eventoDateButton.setOnClickListener{ selectDate()}
        binding.eventoInsertButton.setOnClickListener{
            sendEvento()
            view.findNavController().navigate(R.id.action_formEventFragment_to_eventoListFragment)
        }

        binding.eventoMapButton.setOnClickListener{
            view.findNavController().navigate(R.id.action_formEventFragment_to_eventoMapsFragment)
        }
        binding.eventoFotoButton.setOnClickListener{
            if (model.imagesEvento.value !== null && model.imagesEvento.value!!.size >= 3){
                val msj = Toast.makeText(
                    activity,
                    "El maximo de fotos para un evento son 3",
                    Toast.LENGTH_LONG)
                msj.show()
            } else{
                dispatchTakePictureIntent()
            }

        }

        //TODO: eliminable
        eventoModel.allEventos.observe(viewLifecycleOwner) { eventos ->
            Log.w("eventos room", eventos.toString())
        }

        return view
    }

    private fun  checkVisibilityOfButtons(){
        if (model.imagesEvento.value == null){
            binding.nextFotoButton.isVisible = false
            binding.prevFotoButton.isVisible = false
            return
        }
        if (model.imagesEvento.value!!.size < 2){
            binding.nextFotoButton.isVisible = false
            binding.prevFotoButton.isVisible = false
            return
        }
        if (model.visibleFoto.value!! == model.imagesEvento.value!!.size -1) {
            binding.prevFotoButton.isVisible = true
            binding.nextFotoButton.isVisible = false
            return
        }
        if (model.visibleFoto.value!! == 0) {
            binding.nextFotoButton.isVisible = true
            binding.prevFotoButton.isVisible = false
            return
        }
        binding.nextFotoButton.isVisible = true
        binding.prevFotoButton.isVisible = true
        return
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

    private fun sendEvento() {
        model.setNombreEvento("${binding.eventoNombreTextView.text}")
        model.setTipoEvento("${binding.tipoEventoTextView.text}")

        val missingRequiredInput = checkRequiredInputs()
        if (missingRequiredInput.isEmpty()){
            saveEvento()
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

    fun saveEvento(){
       var pictures = listOf<String>()
        if (model.imagesEvento.value !== null && !model.imagesEvento.value.isNullOrEmpty()) {
            for (image in model.imagesEvento.value!!){
                val file =
                    imageStorage.storeImageOnLocal(image!!,requireActivity().packageName,"EV") //Se guarda en /Android/data/com.example.fishingapp/files

                //Si tarda en verse el cambio en la carpeta es por que tarda en guardar el png
                pictures = pictures.plus(file.toString())
                if (file != null) {
                    imageStorage.uploadImageToFirebase(file,"EV")
                }
            }
        }
        
        Log.w("Pictures", pictures.toString())
            val newEvento = Evento(
                model.getNombreEvento(),
                model.getTipoEvento(),
                model.dateEvento.value.toString(),
                pictures,
                model.coordenadasEvento.value!!.latitude,
                model.coordenadasEvento.value!!.longitude
            )

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
                .addOnFailureListener {
                    Log.w("evento - fallo", it.toString())
                    eventoModel.insert(newEvento)
                }

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
        model.setImagesEvento(listOf())
        val selectedDate = SimpleDateFormat("dd/MM/yyyy", Locale.ENGLISH).format(Calendar.getInstance().time)
        model.setDateEvento(selectedDate.toString())
    }

    override fun onMapReady(googleMap: GoogleMap) {
        mMap = googleMap
        mMap.clear()

        val coordenadas: LatLng
        if(model.coordenadasEvento.value != null) {
            coordenadas = model.coordenadasEvento.value!!
            mMap.addMarker(MarkerOptions().position(coordenadas))
            mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(coordenadas, 10f))
        }
    }
}