package com.example.myapplication

import android.app.Activity
import android.app.DatePickerDialog
import android.app.Dialog
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.DatePicker
import android.widget.TextView
import androidx.fragment.app.DialogFragment
import java.util.*

class RegusterTicketActivity : AppCompatActivity()
{
    /*-------------------------------------------------------------------------------------------*/
    lateinit var txtRegTicketDate: TextView
    /*-------------------------------------------------------------------------------------------*/
    override fun onCreate(savedInstanceState: Bundle?)
    {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_reguster_ticket)

        txtRegTicketDate = findViewById(R.id.txtRegTicketDate)
        txtRegTicketDate.setOnClickListener { txtRegTicketDateOnClick() }
    }
    private fun txtRegTicketDateOnClick()
    {
        val newFragment = DatePickerFragment.newListenerInstance(DatePickerDialog.OnDateSetListener{view: DatePicker?, year: Int, month: Int, dayOfMonth: Int ->
            val selectedDate = dayOfMonth.toString() + "/" + (month + 1) + "/" + year
            txtRegTicketDate.text = selectedDate
        }
        )
        newFragment.show(supportFragmentManager, "datePicker")
    }
}

