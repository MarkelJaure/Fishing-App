package com.example.proyectovacio

import android.app.DatePickerDialog
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.example.proyectovacio.databinding.ActivityMainBinding
import java.text.DateFormat
import java.util.*

const val EXTRA_MESSAGE = "com.example.myfirstapp.MESSAGE"
var finalDate = ""

class MainActivity : AppCompatActivity(), DatePickerDialog.OnDateSetListener  {

    private lateinit var binding: ActivityMainBinding
    private val model: MyViewModel by viewModels()
    private val mDatePickerDialogFragment = DatePicker()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        Log.i("Main Activity","OnCreate")

        binding = ActivityMainBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        val opcionesDropdown = resources.getStringArray(R.array.types)
        val arrayAdapter = ArrayAdapter(this,R.layout.dropdown_item, opcionesDropdown)
        binding.autoCompleteTextView.setAdapter(arrayAdapter)

        binding.MainText.text = model.getSeleccionUser()

        //binding.MainText.text = "Hola view binding"
        //binding.MainButton.setOnClickListener{ manejarClick()}

        binding.IntentButton.setOnClickListener{ sendMessage()}
        binding.fragmentButton.setOnClickListener{ goToFragment()}
        binding.dateButton.setOnClickListener{ selectDate()}
        val defaultCalendar: Calendar = Calendar.getInstance()

        val defaultDate: String =
            DateFormat.getDateInstance(DateFormat.FULL).format(defaultCalendar.getTime())
        finalDate = defaultDate

    }

    private fun selectDate() {
        mDatePickerDialogFragment.show(supportFragmentManager, "DATE PICK")
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

    private fun goToFragment() {
        val intent = Intent(this, FragmentActivity::class.java).apply{}
        startActivity(intent)
    }

    private fun manejarClick(){
        val msj = Toast.makeText(
            applicationContext,
            "Click on me",
            Toast.LENGTH_LONG)
        msj.show()
        val userSelection = "${binding.TextView.text} ${binding.autoCompleteTextView.text}"
        model.setSeleccionUser(userSelection)
        binding.MainText.text = model.getSeleccionUser()
    }

    fun sendMessage() {

        if (binding.TextView.text.isEmpty()){
            val msj = Toast.makeText(
                applicationContext,
                "Completar texto",
                Toast.LENGTH_LONG)
            msj.show()
            return
        }

        if (binding.autoCompleteTextView.text.isEmpty()){
            val msj = Toast.makeText(
                applicationContext,
                "Seleccionar una opcion",
                Toast.LENGTH_LONG)
            msj.show()
            return
        }
        val userSelection = "${binding.TextView.text} - ${binding.autoCompleteTextView.text}"


        val intent = Intent(this, DisplayMessageActivity::class.java).apply {
            putExtra(EXTRA_MESSAGE, userSelection)
            putExtra(finalDate, finalDate)
        }
        startActivity(intent)
    }

    override fun onStart() {
        super.onStart()
        Log.i("Main Activity","OnStart")
    }

    override fun onStop() {
        super.onStop()
        Log.i("Main Activity","OnStop")
    }

    override fun onResume() {
        super.onResume()
        val opcionesDropdown = resources.getStringArray(R.array.types)
        val arrayAdapter = ArrayAdapter(this,R.layout.dropdown_item, opcionesDropdown)
        binding.autoCompleteTextView.setAdapter(arrayAdapter)
        Log.i("Main Activity","OnResume")
    }

    override fun onDestroy() {
        super.onDestroy()
        Log.i("Main Activity","OnDestroy")
    }

    override fun onPause() {
        super.onPause()
        Log.i("Main Activity","OnPause")
    }

    override fun onRestart() {
        super.onRestart()
        Log.i("Main Activity","OnRestart")
    }



}