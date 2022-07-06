package com.example.fishingapp

import android.app.DatePickerDialog
import android.app.Dialog
import android.os.Bundle
import android.os.Parcel
import android.os.Parcelable
import android.widget.DatePicker
import androidx.fragment.app.DialogFragment
import androidx.navigation.navGraphViewModels
import com.example.fishingapp.viewModels.MyViewModel
import java.text.SimpleDateFormat
import java.util.*

class DatePicker() : DialogFragment(), DatePickerDialog.OnDateSetListener {

    private val calendar = Calendar.getInstance()
    private val model: MyViewModel by navGraphViewModels(R.id.navigation)
    private var setOption = 0;

    constructor(aSetOption: Int) : this() {
        setOption = aSetOption
    }


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

        when (setOption) {
            1 -> model.setDate(selectedDate)
            2 -> model.setDateEvento(selectedDate)
        }

    }


}