package com.example.myapplication

import android.Manifest
import android.app.DatePickerDialog
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.view.View
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.storage.FirebaseStorage
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

class RegusterTicketActivity : AppCompatActivity()
{
    /*-------------------------------------------------------------------------------------------*/
    private val CAMERA_PERMISSION_CODE:Int = 100
    private val CAMERA_REQUEST: Int = 1
    private val TYPEDISPLAY: String = "TYPE_DISPLAY"
    private val MODE_REGISTER: String = "REGISTER"
    private val MODE_DISPLAY: String = "DISPLAY"
    private val MODE_EDIT: String = "EDIT"
    private val URL_IMG: String = "URL_IMG"
    private val TICKET_DATE: String = "TICKET_DATE"
    private val TICKET_AMOUNT: String = "TICKET_AMOUNT"
    private val TICKET_CREATED: String = "TICKET_CREATED"
    /*-------------------------------------------------------------------------------------------*/
    private var ticketPhoto: Bitmap? = null
    private var typeDisplay: String = ""
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
        imgRegTicketPhoto.setOnClickListener(View.OnClickListener { onClickbtnRegTicketPhoto() })

        btnRegTicketPhoto = findViewById(R.id.btnRegTicketPhoto)
        btnRegTicketPhoto.setOnClickListener(View.OnClickListener { onClickbtnRegTicketPhoto() })

        btnRegTicketRegister = findViewById(R.id.btnRegTicketRegister)
        btnRegTicketRegister.setOnClickListener(View.OnClickListener { onClickbtnRegTicketRegister() })

        val myIntent = intent
        typeDisplay = intent.getStringExtra(TYPEDISPLAY).toString()
        if (typeDisplay == MODE_REGISTER)
        {
            setModeRegister()
        }
        else if (typeDisplay == MODE_EDIT)
        {
            setModeEdit()
            val url = intent.getStringExtra(URL_IMG).toString()
            val date = intent.getStringExtra(TICKET_DATE).toString()
            val amount = intent.getStringExtra(TICKET_AMOUNT).toString()
            loadTicketInfo(url, date, amount)
        }


    }

    private fun loadTicketInfo(imgUri: String, date: String, amount: String)
    {
        txtRegTicketDate.text = date
        txtRegTicketAmount.text = amount

        if (imgUri != null)
        {
            val storage = FirebaseStorage.getInstance()
            val gsReference = storage.getReferenceFromUrl(imgUri)
            val ONE_MEGABYTE: Long = 1024 * 1024
            gsReference.getBytes(ONE_MEGABYTE).addOnSuccessListener {
                // Data for "images/island.jpg" is returned, use this as needed

                val bitm = BitmapFactory.decodeByteArray(it, 0, it.size)
                imgRegTicketPhoto.setImageBitmap(bitm)

            }.addOnFailureListener {
                Log.e("dea_addOnFailureListener", it.message.toString())
            }
        }
    }

    private fun setModeRegister()
    {
        btnRegTicketPhoto.visibility = View.VISIBLE
        btnRegTicketRegister.visibility = View.VISIBLE
    }
    private fun setModeEdit()
    {
        btnRegTicketPhoto.visibility = View.GONE
        btnRegTicketRegister.visibility = View.GONE
    }
    private fun setModeDisplay()
    {
        btnRegTicketPhoto.visibility = View.GONE
        btnRegTicketRegister.visibility = View.GONE
    }

    private fun txtRegTicketDateOnClick()
    {
        if(typeDisplay == MODE_REGISTER)
        {
            val newFragment = DatePickerFragment.newListenerInstance(DatePickerDialog.OnDateSetListener { view: DatePicker?, year: Int, month: Int, dayOfMonth: Int ->
                val selectedDate = dayOfMonth.toString() + "/" + (month + 1) + "/" + year
                txtRegTicketDate.text = selectedDate
            }
            )
            newFragment.show(supportFragmentManager, "datePicker")
        }
    }
    private fun onClickbtnRegTicketPhoto()
    {
        if (typeDisplay == MODE_REGISTER)
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

