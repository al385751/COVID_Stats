package com.example.covidstats.aditionalClasses

import android.app.DatePickerDialog
import android.app.Dialog
import android.os.Bundle
import androidx.fragment.app.DialogFragment
import java.time.Month
import java.util.*

class DatePickerFragment : DialogFragment() {

    private var listener: DatePickerDialog.OnDateSetListener? = null

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val c = Calendar.getInstance()
        val year = c.get(Calendar.YEAR)
        val month = c.get(Calendar.MONTH)
        val day = c.get(Calendar.DAY_OF_MONTH)

        // Selected date (initial value)
        val datePickerDialog = DatePickerDialog(context!!, listener, year - 18, month, day)

        // Min and max date
        c.set(Calendar.YEAR, year)
        c.set(Calendar.MONTH, month)
        c.set(Calendar.DAY_OF_MONTH, day)
        datePickerDialog.datePicker.maxDate = c.timeInMillis
        c.set(Calendar.YEAR, 2020)
        c.set(Calendar.MONTH, 1)
        c.set(Calendar.DAY_OF_MONTH, 23)
        datePickerDialog.datePicker.minDate = c.timeInMillis

        return datePickerDialog
    }

    companion object {
        fun newInstance(listener: DatePickerDialog.OnDateSetListener): DatePickerFragment {
            val fragment = DatePickerFragment()
            fragment.listener = listener
            return fragment
        }
    }
}