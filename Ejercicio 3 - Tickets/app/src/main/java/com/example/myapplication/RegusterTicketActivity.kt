package com.example.myapplication

import android.Manifest
import android.app.DatePickerDialog
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.view.View
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import com.example.myapplication.R.string.*
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageMetadata
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.activity_profile.*
import kotlinx.android.synthetic.main.activity_reguster_ticket.*
import java.lang.Exception
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.util.*

class RegusterTicketActivity : AppCompatActivity()
{
    private val TAG = "dea_RegusterTicketActivity"
    /*-------------------------------------------------------------------------------------------*/
    private val CAMERA_PERMISSION_CODE:Int = 100
    private val IMG_REQUEST: Int = 1
    /*private val TYPEDISPLAY: String = "TYPE_DISPLAY"
    private val MODE_REGISTER: String = "REGISTER"
    private val MODE_DISPLAY: String = "DISPLAY"
    private val MODE_EDIT: String = "EDIT"
    private val URL_IMG: String = "URL_IMG"
    private val TICKET_DATE: String = "TICKET_DATE"
    private val TICKET_AMOUNT: String = "TICKET_AMOUNT"
    private val TICKET_CREATED: String = "TICKET_CREATED"
    private val TICKET_PATH: String = "TICKET_PATH"*/
    /*-------------------------------------------------------------------------------------------*/
    private var ticketPhoto: Bitmap? = null
    private var typeDisplay: String? = ""
    private var storageRefChild: String = ""
    private var ticketDisplay: Ticket = Ticket()
    /*-------------------------------------------------------------------------------------------*/
    override fun onCreate(savedInstanceState: Bundle?)
    {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_reguster_ticket)

        //val myIntent = intent
        typeDisplay = intent.getStringExtra(TYPEDISPLAY.toString())
        Toast.makeText(this, "typeDisplay", Toast.LENGTH_SHORT)
        if (typeDisplay == MODE_REGISTER.toString())
        {
            ticketDisplay.resetData()
            setModeRegister()
            Toast.makeText(this, "typeDisplay MODE_REGISTER", Toast.LENGTH_SHORT)
        }
        else if (typeDisplay == MODE_EDIT.toString())
        {
            Toast.makeText(this, "typeDisplay MODE_EDIT", Toast.LENGTH_SHORT)
            setModeEdit()
            ticketDisplay.id = intent.getStringExtra(ID_TICKET.toString()).toString()
            ticketDisplay.url = intent.getStringExtra(URL_IMG.toString()).toString()
            //storageRefChild = url
            //val date = intent.getStringExtra(TICKET_DATE).toString()
            ticketDisplay.date = intent.getStringExtra(TICKET_DATE.toString()).toString()
            ticketDisplay.amount = intent.getStringExtra(TICKET_AMOUNT.toString()).toString().toDouble()

            Log.i(TAG, "Ticket ID = ${ticketDisplay.id}")

            storageRefChild = intent.getStringExtra(TICKET_PATH.toString()).toString()
            loadTicketInfo(ticketDisplay.url, ticketDisplay.date, ticketDisplay.amount.toString())
        }
        else(typeDisplay == null)
        {
            Toast.makeText(this, "typeDisplay null", Toast.LENGTH_SHORT)
        }
    }

    private fun loadTicketInfo(imgUri: String, date: String, amount: String)
    {
        //val dateChar: CharSequence = date
        txtRegTicketDate.setText(date)
        txtRegTicketAmount.setText(amount)

        Toast.makeText(this, "Cargando info", Toast.LENGTH_SHORT)
        if (imgUri != null)
        {
            val storage = FirebaseStorage.getInstance()
            val gsReference = storage.getReferenceFromUrl(imgUri)
            val ONE_MEGABYTE: Long = 1024 * 1024
            gsReference.getBytes(ONE_MEGABYTE).addOnSuccessListener {
                // Data for "images/island.jpg" is returned, use this as needed

                val bitm = BitmapFactory.decodeByteArray(it, 0, it.size)
                ticketPhoto = bitm
                imgRegTicketPhoto.setImageBitmap(bitm)

            }.addOnFailureListener {
                Log.e(TAG, it.message.toString())
            }
            /*if (imgRegTicketPhoto != null)
            {
                Log.e("daacba imgUri", imgUri)
                Toast.makeText(this, imgUri , Toast.LENGTH_SHORT)
            }
            else
            {
                Log.e("daacba", "imgRegTicketPhoto == null")
                Toast.makeText(this, "imgRegTicketPhoto == null" , Toast.LENGTH_SHORT)
            }*/
        }
        else
        {
            Log.e(TAG, "imgUri == null")
            Toast.makeText(this, "imgUri == null" , Toast.LENGTH_SHORT)
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
        btnRegTicketRegister.visibility = View.VISIBLE
    }
    private fun setModeDisplay()
    {
        btnRegTicketPhoto.visibility = View.GONE
        btnRegTicketRegister.visibility = View.GONE
    }

    fun txtRegTicketDateOnClick(view: View)
    {
        if(typeDisplay == MODE_REGISTER.toString())
        {
            val newFragment = DatePickerFragment.newListenerInstance(DatePickerDialog.OnDateSetListener
                { view: DatePicker?, year: Int, month: Int, dayOfMonth: Int ->
                    //val selectedDate = dayOfMonth.toString() + "/" + (month + 1) + "/" + year
                    /*val calendar = Calendar.getInstance()
                    calendar.set(Calendar.DAY_OF_MONTH, dayOfMonth)
                    calendar.set(Calendar.MONTH, month)
                    calendar.set(Calendar.YEAR, year)*/
                    val dt = LocalDate.of(year, month, dayOfMonth)
                    val selectedDate = dt.format(DateTimeFormatter.ofPattern("dd/MM/yyyy"))


                    //LocalDateTime.now().format(DateTimeFormatter.ofPattern("dd/MM/yyyy"))
                    //val selectedDate = "${dayOfMonth}/${(month + 1)}/$year"


                    txtRegTicketDate.setText(selectedDate)
                }
            )
            newFragment.show(supportFragmentManager, "datePicker")
        }
    }
    fun onClickbtnRegTicketPhoto(view: View)
    {
        /*if (typeDisplay == MODE_REGISTER.toString())
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
        }*/

        if(typeDisplay == MODE_REGISTER.toString()) {

            val intent = Intent()
            intent.type = "image/*"
            intent.action = Intent.ACTION_GET_CONTENT

            val imgChooser = Intent.createChooser(intent, "Selecciona un ticket")
            startActivityForResult(imgChooser, IMG_REQUEST)
        }

    }
    fun onClickbtnRegTicketRegister(view: View)
    {
        if (typeDisplay == MODE_REGISTER.toString())
        {

            registerTicket()
            defaultValueFields()
        }
        else if(typeDisplay == MODE_EDIT.toString())
        {
            updateTicket()
        }
    }

    private fun validateTicketCreation(ticket: Ticket)
    {
        val loadingDialog = LoadingDialog(this)
        loadingDialog.startLoadingAnimation()

        var crear = true
        var date_created = ""
        val userTicketsBd = FirebaseDatabase.getInstance().getReference(BdText.TB_TICKETS).child(SLogin.getUserId())
        userTicketsBd.addValueEventListener(object : ValueEventListener{
            override fun onDataChange(snapshot: DataSnapshot) {

                if (crear)
                {
                    crear = false

                    for (row in snapshot.children)
                    {
                        date_created = row.child(BdText.COL_CREATED).value.toString()
                    }

                    /***/
                    if (ticket.registerTicket())
                    {
                        registerTicketDb(ticket, loadingDialog)
                    }
                    else
                    {
                        loadingDialog.dismissDialog()
                        Log.i(TAG, "Ticket NO validado.")
                    }
                    /***/
                }


            }

            override fun onCancelled(error: DatabaseError) {
                TODO("Not yet implemented")
            }
        })
    }

    private fun registerTicket()
    {
        if( txtRegTicketDate.text.toString() == null || txtRegTicketDate.text.toString() == "" ||
            txtRegTicketAmount.text.toString() == null || txtRegTicketAmount.text.toString() == "" ||
            ticketPhoto == null )
        {
            Toast.makeText(this, "Complete todos los campos", Toast.LENGTH_LONG)
            Log.i(TAG, "Complete todos los campos")
        }
        else
        {
            /*val date = txtRegTicketDate.text.toString()
            val amount = txtRegTicketAmount.text.toString().toDouble()
            val today = LocalDateTime.now().format(DateTimeFormatter.ofPattern("dd/MM/yyyy"))
            val ticket = Ticket(date, amount, ticketPhoto!!, today, applicationContext, SLogin.getUserId())*/
            val ticket = Ticket(
                    txtRegTicketDate.text.toString(),
                    txtRegTicketAmount.text.toString().toDouble(),
                    ticketPhoto!!,
                    LocalDateTime.now().format(DateTimeFormatter.ofPattern("dd/MM/yyyy")),
                    applicationContext,
                    SLogin.getUserId())


            if (ticket.amount > 0)
            {
                validateTicketCreation(ticket)
            }
            else
            {
                Log.i(TAG, "Ingresar monto mayor a cero")
                Toast.makeText(this, "Ingresar monto mayor a cero", Toast.LENGTH_LONG).show()
            }
        }
    }

    private fun registerTicketDb(ticket: Ticket, loadingDialog: LoadingDialog)
    {
        /*Log.i(TAG, "UserId ${ticket.userid}")
        Log.i(TAG, "Path ${ticket.path}")
        Log.i(TAG, "Created ${ticket.created}")
        Log.i(TAG, "Amount ${ticket.amount}")
        Log.i(TAG, "Date ${ticket.date}")
        Log.i(TAG, "Url ${ticket.url}")*/

        val fbDB = FirebaseDatabase.getInstance().reference.child(BdText.TB_TICKETS).child(ticket.userid).push()
        fbDB.setValue(ticket.toHashMap("")).addOnCompleteListener{
            if (it.isSuccessful) {
                loadingDialog.dismissDialog() }
            else
            {
                Log.i(TAG, "Ticket No creado")
                loadingDialog.dismissDialog()
            }
        }
    }

    private fun updateTicket()
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
            //val ticket = Ticket(date, amount, ticketPhoto!!, today, applicationContext)

            if (amount > 0)
            {
                /*
                val storage = FirebaseStorage.getInstance()
                // Create a storage reference from our app
                val storageRef = storage.reference
                // Get reference to the file
                val forestRef = storageRef.child(storageRefChild)

                // Create file metadata including the content type
                /*val metadata = storageMetadata {
                    contentType = "image/jpg"
                    setCustomMetadata("myCustomProperty", "myValue")
                }*/

                val metaData = StorageMetadata.Builder()
                    //.setCustomMetadata(Ticket.DATE, date)
                    .setContentType(Ticket.CONTENT_TYPE)
                    .setCustomMetadata(Ticket.AMOUNT, amount.toString())
                    //.setCustomMetadata(Ticket.CREATED, created)
                    .build()
                // Update metadata properties
                forestRef.updateMetadata(metaData).addOnSuccessListener { updatedMetadata ->
                    Log.i(TAG, updatedMetadata.toString())
                }.addOnFailureListener {
                    Log.e(TAG, it.toString())
                }
                 */
                updateTicketBd(amount)
            }
            else
            {
                Toast.makeText(this, "Ingresar monto mayor a cero", Toast.LENGTH_LONG).show()
            }
        }
    }

    private fun updateTicketBd(amount: Double)
    {

        val ticket = Ticket()
        ticket.amount = amount

        val hashMap = HashMap<String, String>().put(BdText.COL_AMOUNT, amount.toString())
        val fbDB = FirebaseDatabase.getInstance().reference.child(BdText.TB_TICKETS).child(SLogin.getUserId()).child(ticketDisplay.id)

        fbDB.updateChildren(ticket.toHashMap(Ticket.HM_AMOUNT) as Map<String, Any>).addOnCompleteListener{
            if (it.isSuccessful)
                Toast.makeText(this, "Ticket actualizado", Toast.LENGTH_LONG).show()
            else
                Toast.makeText(this, "Ticket NO actualizado", Toast.LENGTH_LONG).show()
        }
    }

    private fun defaultValueFields()
    {
        //txtRegTicketDate.text = LocalDateTime.now().format(DateTimeFormatter.ofPattern("dd/MM/yyyy"))
        //txtRegTicketAmount.text = "0"

        txtRegTicketDate.text.clear()
        txtRegTicketAmount.text.clear()
        ticketPhoto = null
        imgRegTicketPhoto.setImageBitmap(ticketPhoto)
    }

    /*@Override
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
    }*/
    @Override
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?)
    {
        super.onActivityResult(requestCode, resultCode, data)
        if ( requestCode == IMG_REQUEST &&
             resultCode == RESULT_OK &&
             data != null &&
             data.data != null)
        {
            /*ticketPhoto = data?.extras!!["data"] as Bitmap?
            imgRegTicketPhoto.setImageBitmap(ticketPhoto)*/
            val bitmap =
                    try{ MediaStore.Images.Media.getBitmap(contentResolver, data.data) }
                    catch (e: Exception) { null }

            bitmap?.let {
                ticketPhoto = it
                imgRegTicketPhoto?.setImageBitmap(it)
            }
        }
    }
}

