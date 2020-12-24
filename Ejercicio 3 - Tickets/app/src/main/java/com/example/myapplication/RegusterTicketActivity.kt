package com.example.myapplication

import android.Manifest
import android.app.DatePickerDialog
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.view.View
import android.widget.*
import androidx.core.text.isDigitsOnly
import java.text.DateFormat
import java.text.SimpleDateFormat
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.util.*

class RegusterTicketActivity : AppCompatActivity()
{
    /*-------------------------------------------------------------------------------------------*/
    private val CAMERA_PERMISSION_CODE:Int = 100
    private val CAMERA_REQUEST: Int = 1
    /*-------------------------------------------------------------------------------------------*/
    private var ticketPhoto: Bitmap? = null
    /*-------------------------------------------------------------------------------------------*/
    private lateinit var txtRegTicketDate: TextView
    private lateinit var txtRegTicketAmount: TextView
    private lateinit var imgRegTicketPhoto: ImageView
    private lateinit var btnRegTicketPhoto: Button
    private lateinit var btnRegTicketRegister: Button

    /*-------------------------------------------------------------------------------------------*/
    override fun onCreate(savedInstanceState: Bundle?)
    {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_reguster_ticket)

        txtRegTicketDate = findViewById(R.id.txtRegTicketDate)
        txtRegTicketDate.setOnClickListener { txtRegTicketDateOnClick() }
        //txtRegTicketDate.text = LocalDateTime.now().format(DateTimeFormatter.ofPattern("dd/MM/yyyy"))

        txtRegTicketAmount = findViewById(R.id.txtRegTicketAmount)

        imgRegTicketPhoto = findViewById(R.id.imgRegTicketPhoto)
        imgRegTicketPhoto.setOnClickListener (View.OnClickListener { onClickbtnRegTicketPhoto() })

        btnRegTicketPhoto = findViewById(R.id.btnRegTicketPhoto)
        btnRegTicketPhoto.setOnClickListener(View.OnClickListener { onClickbtnRegTicketPhoto() })

        btnRegTicketRegister = findViewById(R.id.btnRegTicketRegister)
        btnRegTicketRegister.setOnClickListener(View.OnClickListener { onClickbtnRegTicketRegister() })
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
    private fun onClickbtnRegTicketPhoto()
    {
        if (checkSelfPermission(Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED)
        {
            requestPermissions(arrayOf(Manifest.permission.CAMERA), CAMERA_PERMISSION_CODE)
        }
        else
        {
            val cameraIntent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
            startActivityForResult(cameraIntent, CAMERA_REQUEST)
        }
    }
    private fun onClickbtnRegTicketRegister()
    {
        if( txtRegTicketDate.text.toString() == null || txtRegTicketDate.text.toString() == "" ||
            txtRegTicketAmount.text.toString() == null || txtRegTicketAmount.text.toString() == "" ||
            ticketPhoto == null )
        {
            Toast.makeText(this, "Complete todos los campos", Toast.LENGTH_LONG)
        }
        else
        {
            val date = txtRegTicketDate.text.toString()
            val amount = txtRegTicketAmount.text.toString().toDouble()
            val today = LocalDateTime.now().format(DateTimeFormatter.ofPattern("dd/MM/yyyy"))
            val ticket = Ticket(date, amount, ticketPhoto!!, today, applicationContext)

            if (amount > 0)
            {
                Toast.makeText(this, "Pre-registrado", Toast.LENGTH_LONG).show()
                if (ticket.registerTicket())
                {
                    Toast.makeText(this, "Ticket pre-registrado", Toast.LENGTH_LONG).show()
                }
                else
                {
                    Toast.makeText(this, "Ticket fallo en pre-registrado", Toast.LENGTH_LONG).show()
                }
            }
            else
            {
                Toast.makeText(this, "Ingresar monto mayor a cero", Toast.LENGTH_LONG).show()
            }
        }

        defaultValueFields()
    }

    private fun defaultValueFields()
    {
        //txtRegTicketDate.text = LocalDateTime.now().format(DateTimeFormatter.ofPattern("dd/MM/yyyy"))
        //txtRegTicketAmount.text = "0"
        txtRegTicketDate.text = ""
        txtRegTicketAmount.text = ""
        ticketPhoto = null
        imgRegTicketPhoto.setImageBitmap(ticketPhoto)
    }

    @Override
    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<String?>, grantResults: IntArray)
    {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == CAMERA_PERMISSION_CODE)
        {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED)
            {
                val cameraIntent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
                startActivityForResult(cameraIntent, CAMERA_REQUEST)
            }
            else
            {
                Toast.makeText(this, "Sin permisos para la camara.", Toast.LENGTH_LONG).show()
            }
        }
    }
    @Override
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?)
    {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == CAMERA_REQUEST && resultCode == RESULT_OK)
        {
            ticketPhoto = data?.extras!!["data"] as Bitmap?
            imgRegTicketPhoto.setImageBitmap(ticketPhoto)

        }
    }
}

