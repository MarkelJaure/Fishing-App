package com.example.proyectovacio

import android.app.DatePickerDialog
import android.app.DatePickerDialog.OnDateSetListener
import android.app.Dialog
import android.os.Bundle
import androidx.fragment.app.DialogFragment
import java.util.*

class DatePicker : DialogFragment() {
    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val mCalendar = Calendar.getInstance()
        val year = mCalendar[Calendar.YEAR]
        val month = mCalendar[Calendar.MONTH]
        val dayOfMonth = mCalendar[Calendar.DAY_OF_MONTH]
        //TODO: com.example.proyectovacio.MainActivity cannot be cast to android.app.DatePickerDialog$OnDateSetListener
        return DatePickerDialog(requireActivity(), activity as OnDateSetListener?, year, month, dayOfMonth)
    }
}