package com.example.proyectovacio

import android.app.Activity
import android.content.Intent
import android.graphics.Bitmap
import android.os.Bundle
import android.provider.MediaStore
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.navigation.findNavController
import androidx.navigation.navGraphViewModels
import com.example.proyectovacio.databinding.FragmentFormBinding
import java.text.SimpleDateFormat
import java.util.*

class FormFragment : Fragment() {

    private var _binding: FragmentFormBinding? = null
    private val binding get() = _binding!!

    private val model: MyViewModel by navGraphViewModels(R.id.navigation)
    private val mDatePickerDialogFragment = DatePicker()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentFormBinding.inflate(layoutInflater)
        val view = binding.root

        val opcionesDropdown = resources.getStringArray(R.array.types)
        binding.autoCompleteTextView.setAdapter(ArrayAdapter(view.context, R.layout.dropdown_item, opcionesDropdown))

        binding.imageView.setImageBitmap(model.getImage())

        if (model.getDate().isEmpty()){
            val selectedDate = SimpleDateFormat("dd/MM/yyyy", Locale.ENGLISH).format(Calendar.getInstance().time)
            model.setDate(selectedDate.toString())
        }

        binding.fragmentButton.setOnClickListener{
            view.findNavController().navigate(R.id.action_formFragment_to_helpFragment)
        }
        binding.IntentButton.setOnClickListener{ sendMessage(view)}
        binding.dateButton.setOnClickListener{ selectDate()}
        binding.button.setOnClickListener{ dispatchTakePictureIntent()}

        return view
    }

    
    private fun selectDate() {
        mDatePickerDialogFragment.show(parentFragmentManager, "DATE PICK")
    }

    private fun sendMessage(view: View) {

        if (binding.TextView.text.isEmpty()){
            val msj = Toast.makeText(
                activity,
                "Completar texto",
                Toast.LENGTH_LONG)
            msj.show()
            return
        }

        if (binding.autoCompleteTextView.text.isEmpty()){
            val msj = Toast.makeText(
                activity,
                "Seleccionar una opcion",
                Toast.LENGTH_LONG)
            msj.show()
            return
        }

        if (binding.textView3.text.isEmpty()){
            val msj = Toast.makeText(
                activity,
                "Seleccionar una fecha",
                Toast.LENGTH_LONG)
            msj.show()
            return
        }

        model.setNombre("${binding.TextView.text}")
        model.setSeleccionUser("${binding.autoCompleteTextView.text}")

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
            binding.imageView.setImageBitmap(model.getImage())
        }
    }
}