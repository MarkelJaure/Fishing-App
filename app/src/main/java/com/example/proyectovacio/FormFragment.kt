package com.example.proyectovacio

import android.app.Activity
import android.content.Intent
import android.graphics.Bitmap
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
import java.text.SimpleDateFormat
import com.example.proyectovacio.databinding.FragmentFormBinding
import java.util.*

class FormFragment : Fragment() {

    //private var _binding: FragmentFormBinding? = null
    //private val binding get() = _binding!!
    private lateinit var binding: FragmentFormBinding

    private val model: MyViewModel by navGraphViewModels(R.id.navigation)
    private val mDatePickerDialogFragment = DatePicker()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        //_binding = FragmentFormBinding.inflate(layoutInflater)
        //val binding = DataBindingUtil.setContentView<FragmentFormBinding>(, R.layout.fragment_form)
        binding = DataBindingUtil.inflate(inflater,R.layout.fragment_form,container,false)
        binding.lifecycleOwner = this

        val view = binding.root


        val opcionesDropdown = resources.getStringArray(R.array.types)
        binding.autoCompleteTextView.setAdapter(ArrayAdapter(view.context, R.layout.dropdown_item, opcionesDropdown))

        binding.imageView.setImageBitmap(model.getImage())

        if (model.date.value == null){
            val selectedDate = SimpleDateFormat("dd/MM/yyyy", Locale.ENGLISH).format(Calendar.getInstance().time)
            //model.setDate(selectedDate.toString())
            model.setDate(selectedDate.toString())
        }
        //binding.textView3.text = model.getDate()
        //binding.textView3.text = model.fecha.value
        model.date.observe(viewLifecycleOwner) { fecha -> binding.textView3.text = fecha }

        binding.fragmentButton.setOnClickListener{
            view.findNavController().navigate(R.id.action_formFragment_to_helpFragment)
        }
        binding.IntentButton.setOnClickListener{ sendMessage(view,binding)}
        binding.dateButton.setOnClickListener{ selectDate()}
        binding.button.setOnClickListener{ dispatchTakePictureIntent()}

        return view
    }

    
    private fun selectDate() {
        mDatePickerDialogFragment.show(parentFragmentManager, "DATE PICK")
    }

    private fun sendMessage(view: View, binding:FragmentFormBinding) {
        //TODO: Sacar el binding de los parametros
        model.setNombre("${binding.TextView.text}")
        model.setTipoPesca("${binding.autoCompleteTextView.text}")
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
            //TODO: Volver a mostar la imagen cuando se setea
            // binding.imageView.setImageBitmap(model.getImage())
        }
    }
}