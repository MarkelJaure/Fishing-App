package com.example.fishingapp.reportes

import android.app.Activity
import android.content.ContentValues
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Bundle
import android.os.Environment
import android.provider.MediaStore
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.activity.addCallback
import androidx.databinding.DataBindingUtil
import androidx.navigation.findNavController
import androidx.navigation.navGraphViewModels
import com.example.fishingapp.DatePicker
import com.example.fishingapp.R
import java.text.SimpleDateFormat
import com.example.fishingapp.databinding.FragmentFormBinding
import com.example.fishingapp.lib.ImageStorage
import com.example.fishingapp.models.Reporte
import com.example.fishingapp.viewModels.MyViewModel
import com.example.fishingapp.viewModels.ReporteViewModel
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.ktx.storage
import java.io.File
import java.io.FileNotFoundException
import java.io.FileOutputStream
import java.io.IOException
import java.util.*

val REQUEST_IMAGE_CAPTURE = 1

class FormFragment : Fragment(), OnMapReadyCallback {

    private lateinit var binding: FragmentFormBinding
    private lateinit var mMap: GoogleMap

    private val model: MyViewModel by navGraphViewModels(R.id.navigation)
    private val reporteModel: ReporteViewModel by navGraphViewModels(R.id.navigation)

    private lateinit var opcionesDropdown: Array<String>
    private val mDatePickerDialogFragment = DatePicker(1)

    private val imageStorage: ImageStorage = ImageStorage()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_form,container,false)
        binding.lifecycleOwner = this
        binding.model = model

        val view = binding.root

        val mapFragment = childFragmentManager.findFragmentById(R.id.mapReport) as SupportMapFragment
        mapFragment.getMapAsync(this)

        if(model.getReportDetail() != null && model.getEditReport()) { //Cargar datos en caso de querer editar un reporte
            binding.nombreTextView.setText(model.getReportDetail()!!.nombre)
            binding.tipoPescaTextView.setText(model.getReportDetail()!!.tipoPesca)
            model.setDate(model.getReportDetail()!!.date)
            var imgFile = File(model.getReportDetail()!!.image)
            model.setImage(BitmapFactory.decodeFile(imgFile.absolutePath))
            model.setCoordenadasReporte(LatLng(model.getReportDetail()!!.latitud, model.getReportDetail()!!.longitud))
        } else {    //En caso de crear un nuevo reporte
            model.setReportDetail(null)
            binding.nombreTextView.setText(model.getNombre())
            binding.tipoPescaTextView.setText(model.getTipoPesca())


            if(model.date.value == null || model.date.value == "") {
                val selectedDate = SimpleDateFormat("dd/MM/yyyy", Locale.ENGLISH).format(Calendar.getInstance().time)
                model.setDate(selectedDate.toString())
            }
        }
        model.setFilterReport(false)

        opcionesDropdown = resources.getStringArray(R.array.types)
        binding.tipoPescaTextView.setAdapter(ArrayAdapter(view.context,
            R.layout.dropdown_item, opcionesDropdown))
        model.image.observe(viewLifecycleOwner) { image -> binding.imageView.setImageBitmap(image) }

        binding.helpButton.setOnClickListener{
            view.findNavController().navigate(R.id.action_formFragment_to_helpFragment)
        }
        binding.insertButton?.setOnClickListener{ sendMessage(view)}
        binding.dateButton.setOnClickListener{ selectDate()}
        binding.fotoButton.setOnClickListener{ dispatchTakePictureIntent()}
        binding.mapButton.setOnClickListener{
            view.findNavController().navigate(R.id.action_formFragment_to_MapsFragment)
        }

        val callback = requireActivity().onBackPressedDispatcher.addCallback(this) {
        }

        return view
    }

    override fun onMapReady(googleMap: GoogleMap) {
        mMap = googleMap
        mMap.clear()

        var coordenadas: LatLng

        if(model.getReportDetail() != null && model.getEditReport()) {
            coordenadas = LatLng(model.getReportDetail()!!.latitud, model.getReportDetail()!!.longitud)
            mMap.addMarker(MarkerOptions().position(coordenadas))
            mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(coordenadas, 10f))
        }
        if(model.coordenadasReporte.value != null) {
            coordenadas = model.coordenadasReporte.value!!
            mMap.addMarker(MarkerOptions().position(coordenadas))
            mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(coordenadas, 10f))
        }
    }

    private fun selectDate() {
        mDatePickerDialogFragment.show(parentFragmentManager, "DATE PICK")
    }

    private fun sendMessage(view: View) {
        model.setNombre("${binding.nombreTextView.text}")
        model.setTipoPesca("${binding.tipoPescaTextView.text}")
        Log.i("Date", model.date.value.toString())

        if (model.getNombre().isEmpty()){
            val msj = Toast.makeText(
                activity,
                "Completar el titulo del reporte",
                Toast.LENGTH_LONG)
            msj.show()
            return
        }

        if (model.getTipoPesca().isEmpty()){
            val msj = Toast.makeText(
                activity,
                "Seleccionar un tipo de pesca",
                Toast.LENGTH_LONG)
            msj.show()
            return
        }

        if (model.coordenadasReporte.value == null){
            val msj = Toast.makeText(
                activity,
                "Seleccionar una ubicacion",
                Toast.LENGTH_LONG)
            msj.show()
            return
        }

        saveReporte(view)
    }


    fun saveReporte(view: View){

        var picture = ""

        if (model.image.value !== null) {
            var file =
                imageStorage.storeImageOnLocal(model.image.value!!, requireActivity().packageName) //Se guarda en /Android/data/com.example.fishingapp/files
            Log.w(
                "Imagen 2",
                file.toString()
            ) // Echo: /storage/emulated/0/Android/data/com.example.fishingapp/Files/MI_14052022_1844.png
            //Si tarda en verse el cambio en la carpeta es por que tarda en guardar el png
            picture = file.toString()
            if (file != null) {
                imageStorage.uploadImageToFirebase(file)
            }
        }
        if(model.getEditReport()) {
            var editedReporte = model.getReportDetail()?.let {
                Reporte(
                    it.reporteId,
                    model.getNombre(),
                    model.getTipoPesca(),
                    model.date.value.toString(),
                    picture,
                    model.coordenadasReporte.value!!.latitude,
                    model.coordenadasReporte.value!!.longitude
                )
            }
            if (editedReporte != null) {
                reporteModel.update(editedReporte)
            }
        }
        else {
            var newReporte = Reporte(
                model.getNombre(),
                model.getTipoPesca(),
                model.date.value.toString(),
                picture,
                model.coordenadasReporte.value!!.latitude,
                model.coordenadasReporte.value!!.longitude
            )
            reporteModel.insert(newReporte)

            var imagen = ""
            if(picture != "") {
                imagen = Uri.fromFile(File(picture)).lastPathSegment.toString()
            }

            val data = hashMapOf<String, Any>(
                "nombre" to model.getNombre(),
                "tipoPesca" to model.getTipoPesca(),
                "date" to  model.date.value.toString(),
                "imagen" to imagen,
                "latitud" to model.coordenadasReporte.value!!.latitude,
                "longitud" to model.coordenadasReporte.value!!.longitude,
                )

            FirebaseFirestore.getInstance().collection("reportes").add(data)
                .addOnCompleteListener { Log.w("reporte - exito", it.toString()) }
                .addOnFailureListener { Log.w("reporte - fallo", it.toString()) }
        }
        clearReportOnViewModel()
        view.findNavController().navigate(R.id.action_formFragment_to_ReportListFragment)
    }

    fun clearReportOnViewModel(){

        model.setNombre("")
        model.setTipoPesca("")
        model.setImage(null)
        val selectedDate = SimpleDateFormat("dd/MM/yyyy", Locale.ENGLISH).format(Calendar.getInstance().time)
        model.setDate(selectedDate.toString())
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
            model.setImage(imageBitmap)
        }
    }

    override fun onResume() {
        super.onResume()
        binding.tipoPescaTextView.setAdapter(ArrayAdapter(
            requireView().context,
            R.layout.dropdown_item, opcionesDropdown))
    }
}