package com.example.fishingapp.reportes

import android.app.Activity
import android.content.ContentValues
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
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
import com.example.fishingapp.models.Reporte
import com.example.fishingapp.viewModels.ConcursoViewModel
import com.example.fishingapp.viewModels.MyViewModel
import com.example.fishingapp.viewModels.ReglamentacionViewModel
import com.example.fishingapp.viewModels.ReporteViewModel
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
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
    private val mDatePickerDialogFragment = DatePicker()

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
            model.setImage(BitmapFactory.decodeFile(imgFile.getAbsolutePath()))
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
                "Completar texto",
                Toast.LENGTH_LONG)
            msj.show()
            return
        }

        if (model.getTipoPesca().isEmpty()){
            val msj = Toast.makeText(
                activity,
                "Seleccionar una opcion",
                Toast.LENGTH_LONG)
            msj.show()
            return
        }

        if (model.date.toString().isEmpty()){
            val msj = Toast.makeText(
                activity,
                "Seleccionar una fecha",
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
                storeImage(model.image.value!!) //Se guarda en /Android/data/com.example.fishingapp/files
            Log.w(
                "Imagen 2",
                file.toString()
            ) // Echo: /storage/emulated/0/Android/data/com.example.fishingapp/Files/MI_14052022_1844.png
            //Si tarda en verse el cambio en la carpeta es por que tarda en guardar el png
            picture = file.toString()
        }
        if(model.getEditReport()) {//TODO: que la ubicacion sea opcional (value!!)
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


    private fun storeImage(image: Bitmap): File? {
        val pictureFile: File = getOutputMediaFile()!!
        if (pictureFile == null) {
            Log.d(
                "Imagen",
                "Error creating media file, check storage permissions: "
            ) // e.getMessage());
            return null
        }
        try {
            val fos = FileOutputStream(pictureFile)
            image.compress(Bitmap.CompressFormat.PNG, 90, fos)
            fos.close()

        } catch (e: FileNotFoundException) {
            Log.d(ContentValues.TAG, "File not found: ")
        } catch (e: IOException) {
            Log.d(ContentValues.TAG, "Error accessing file: " + e.message)
        }
        return pictureFile
    }

    fun getOutputMediaFile(): File? {
        // To be safe, you should check that the SDCard is mounted
        // using Environment.getExternalStorageState() before doing this.
        val mediaStorageDir: File = File(
            Environment.getExternalStorageDirectory()
                .toString() + "/Android/data/"
                    + requireActivity().getPackageName()
                    + "/Files"
        )

        Log.w("Imagen path", Environment.getExternalStorageDirectory()
            .toString() + "/Android/data/")

        // This location works best if you want the created images to be shared
        // between applications and persist after your app has been uninstalled.

        // Create the storage directory if it does not exist
        if (!mediaStorageDir.exists()) {
            if (!mediaStorageDir.mkdirs()) {
                return null
            }
        }
        // Create a media file name
        val timeStamp = SimpleDateFormat("ddMMyyyy_HHmm").format(Date())
        val mediaFile: File
        val mImageName = "MI_$timeStamp.png"
        mediaFile = File(mediaStorageDir.path + File.separator + mImageName)

        Log.w("Imagen path completo", mediaStorageDir.path + File.separator + mImageName)
        return mediaFile
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