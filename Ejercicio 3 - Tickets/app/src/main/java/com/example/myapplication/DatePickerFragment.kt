package com.example.myapplication

import android.app.Activity
import android.app.DatePickerDialog
import android.app.Dialog
import android.content.Context
import android.os.Bundle
import android.widget.DatePicker
import androidx.fragment.app.DialogFragment
import java.util.*

class DatePickerFragment: DialogFragment()
{
    private lateinit var listener: DatePickerDialog.OnDateSetListener

    companion object
    {
        fun newListenerInstance(pListener: DatePickerDialog.OnDateSetListener): DatePickerFragment
        {
            val fragment = DatePickerFragment()
            fragment.listener = pListener
            return fragment
        }


    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        // Use the current date as the default date in the picker
        val c = Calendar.getInstance()
        val year = c.get(Calendar.YEAR)
        val month = c.get(Calendar.MONTH)
        val day = c.get(Calendar.DAY_OF_MONTH)

        // Create a new instance of DatePickerDialog and return it
        return DatePickerDialog( this.context!! , listener, year, month, day)
    }

}