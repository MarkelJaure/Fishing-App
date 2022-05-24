package com.example.fishingapp.reportes

import android.app.Activity
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.os.Bundle
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
import com.example.fishingapp.viewModels.ConcursoViewModel
import com.example.fishingapp.viewModels.MyViewModel
import com.example.fishingapp.viewModels.ReglamentacionViewModel
import com.example.fishingapp.viewModels.ReporteViewModel
import java.io.File
import java.util.*

class FormFragment : Fragment() {

    private lateinit var binding: FragmentFormBinding

    private val model: MyViewModel by navGraphViewModels(R.id.navigation)

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

        if(model.getReportDetail() != null && model.getEditReport()) { //Cargar datos en caso de querer editar un reporte
            binding.nombreTextView.setText(model.getReportDetail()!!.nombre)
            binding.tipoPescaTextView.setText(model.getReportDetail()!!.tipoPesca)
            model.setDate(model.getReportDetail()!!.date)
            var imgFile = File(model.getReportDetail()!!.image)
            model.setImage(BitmapFactory.decodeFile(imgFile.getAbsolutePath()))
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
        binding.confirmarButton.setOnClickListener{ sendMessage(view)}
        binding.dateButton.setOnClickListener{ selectDate()}
        binding.fotoButton.setOnClickListener{ dispatchTakePictureIntent()}
        binding.mapButton.setOnClickListener{
            view.findNavController().navigate(R.id.action_formFragment_to_MapsFragment)
        }

        val callback = requireActivity().onBackPressedDispatcher.addCallback(this) {
        }

        return view
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

        view.findNavController().navigate(R.id.action_formFragment_to_confirmFragment)
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