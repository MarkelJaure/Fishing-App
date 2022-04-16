package com.example.proyectovacio

import android.app.DatePickerDialog
import android.app.Dialog
import android.os.Bundle
import android.widget.DatePicker
import androidx.fragment.app.DialogFragment
import androidx.navigation.navGraphViewModels
import java.text.SimpleDateFormat
import java.util.*

class DatePicker : DialogFragment(), DatePickerDialog.OnDateSetListener {

    private val calendar = Calendar.getInstance()
    private val model: MyViewModel by navGraphViewModels(R.id.navigation)

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val year = calendar[Calendar.YEAR]
        val month = calendar[Calendar.MONTH]
        val dayOfMonth = calendar[Calendar.DAY_OF_MONTH]
        return DatePickerDialog(requireActivity(), this, year, month, dayOfMonth)
    }

    override fun onDateSet(view: DatePicker?, year: Int, month: Int, dayOfMonth: Int) {
        calendar.set(Calendar.YEAR, year)
        calendar.set(Calendar.MONTH, month)
        calendar.set(Calendar.DAY_OF_MONTH, dayOfMonth)

        val selectedDate = SimpleDateFormat("dd/MM/yyyy", Locale.ENGLISH).format(calendar.time)

        model.setDate(selectedDate.toString())
    }
}