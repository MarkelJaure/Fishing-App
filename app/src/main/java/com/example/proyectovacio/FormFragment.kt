package com.example.proyectovacio

import android.app.DatePickerDialog
import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.activity.viewModels
import androidx.fragment.app.viewModels
import com.example.proyectovacio.databinding.FragmentFormBinding
import java.text.DateFormat
import java.util.*

const val EXTRA_MESSAGE = "com.example.myfirstapp.MESSAGE"
var finalDate = ""

class FormFragment : Fragment(), DatePickerDialog.OnDateSetListener {

    private var _binding: FragmentFormBinding? = null
    private val binding get() = _binding!!

    private val model: MyViewModel by viewModels()
    private val mDatePickerDialogFragment = DatePicker()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentFormBinding.inflate(layoutInflater)
        val view = binding.root

        val opcionesDropdown = resources.getStringArray(R.array.types)
        binding.autoCompleteTextView.setAdapter(ArrayAdapter(view.context, R.layout.dropdown_item, opcionesDropdown))

        binding.MainText.text = model.getSeleccionUser()

        //binding.MainText.text = "Hola view binding"
        //binding.MainButton.setOnClickListener{ manejarClick()}

        binding.IntentButton.setOnClickListener{ sendMessage()}
        binding.dateButton.setOnClickListener{ selectDate()}
        val defaultCalendar: Calendar = Calendar.getInstance()

        val defaultDate: String =
            DateFormat.getDateInstance(DateFormat.FULL).format(defaultCalendar.getTime())
        finalDate = defaultDate

        return view
    }

    private fun selectDate() {
        mDatePickerDialogFragment.show(parentFragmentManager, "DATE PICK")
    }

    override fun onDateSet(view: android.widget.DatePicker?, year: Int, month: Int, dayOfMonth: Int) {
        val mCalendar: Calendar = Calendar.getInstance()
        mCalendar.set(Calendar.YEAR, year)
        mCalendar.set(Calendar.MONTH, month)
        mCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth)
        val selectedDate: String =
            DateFormat.getDateInstance(DateFormat.FULL).format(mCalendar.getTime())
        finalDate = selectedDate
    }

    fun sendMessage() {

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
        val userSelection = "${binding.TextView.text} - ${binding.autoCompleteTextView.text}"

    }
}