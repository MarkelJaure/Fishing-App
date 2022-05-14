package com.example.fishingapp.reportes

import android.content.ContentValues.TAG
import android.content.Context
import android.content.ContextWrapper
import android.graphics.Bitmap
import android.os.Bundle
import android.os.Environment
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.navigation.findNavController
import androidx.navigation.navGraphViewModels
import com.example.fishingapp.R
import com.example.fishingapp.databinding.FragmentConfirmBinding
import com.example.fishingapp.models.Reporte
import com.example.fishingapp.viewModels.MyViewModel
import com.example.fishingapp.viewModels.ReporteViewModel
import java.io.File
import java.io.FileNotFoundException
import java.io.FileOutputStream
import java.io.IOException
import java.text.SimpleDateFormat
import java.util.*


var REQUEST_IMAGE_CAPTURE = 1


class ConfirmFragment : Fragment() {

    private lateinit var binding: FragmentConfirmBinding

    private val model: MyViewModel by navGraphViewModels(R.id.navigation)
    private val reporteModel: ReporteViewModel by navGraphViewModels(R.id.navigation)

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        //_binding = FragmentConfirmBinding.inflate(layoutInflater)
        binding = DataBindingUtil.inflate(inflater,R.layout.fragment_confirm,container,false)
        binding.lifecycleOwner = this
        val view = binding.root

        binding.textView2.text = "${model.getNombre()} - ${model.getTipoPesca()}"
        binding.textView4.text = model.date.value
        binding.imageView2.setImageBitmap(model.image.value)

        binding.insertButton.setOnClickListener{ saveReporte(view)}

        if(model.getEditReport()){
            binding.insertButton.text = getString(R.string.editReportButton)
        }
        else {
            binding.insertButton.text = getString(R.string.newReportButton)
        }
        return view
    }

    fun saveReporte(view: View){

        if(model.getEditReport()) {
            var editedReporte = model.getReportDetail()?.let {
                Reporte(
                    it.reporteId,
                    model.getNombre(),
                    model.getTipoPesca(),
                    model.date.value.toString(),
                    R.drawable.pesca
                )
            }
            if (editedReporte != null) {
                reporteModel.update(editedReporte)
            }
        }
        else {
            if (model.image.value !== null){

                var file = storeImage(model.image.value!!) //Se guarda en /Android/data/com.example.fishingapp/files
                Log.w("Imagen 2", file.toString()) // Echo: /storage/emulated/0/Android/data/com.example.fishingapp/Files/MI_14052022_1844.png
                //Si tarda en verse el cambio en la carpeta es por que tarda en guardar el png

            }

            }

            var newReporte = Reporte(model.getNombre(),model.getTipoPesca(),model.date.value.toString(),R.drawable.pesca)
            reporteModel.insert(newReporte)

        clearReportOnViewModel()
        view.findNavController().navigate(R.id.action_formConfirmFragment_to_reportListFragment)
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
            Log.d(TAG, "File not found: ")
        } catch (e: IOException) {
            Log.d(TAG, "Error accessing file: " + e.message)
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
}