 package com.example.myapplication

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.ListView
import android.widget.Toast
import com.google.firebase.storage.FirebaseStorage
import android.widget.AdapterView.OnItemClickListener
import java.text.FieldPosition


 class HistoryActivity : AppCompatActivity()
{
    //
    private val MODE_EDIT: String = "EDIT"
    private val URL_IMG: String = "URL_IMG"
    private val TYPEDISPLAY: String = "TYPE_DISPLAY"
    private val TICKET_DATE: String = "TICKET_DATE"
    private val TICKET_AMOUNT: String = "TICKET_AMOUNT"
    private val TICKET_CREATED: String = "TICKET_CREATED"
    //
    private lateinit var lstHistoryTickets: ListView
    private var lstTicketsData = mutableListOf<String>()
    private var lstTicketsText = mutableListOf<String>()
    private var lstTicketsObject = mutableListOf<Ticket>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_history)

        lstHistoryTickets = findViewById(R.id.lstHistoryTickets4)
        //lstHistoryTickets.setOnClickListener(View.OnClickListener { onClickListenerlstHistoryTickets() })
        lstHistoryTickets.setOnItemClickListener { arg0, arg1, position, arg3 -> onClickListenerlstHistoryTickets(position) }

        //Toast.makeText(this, "Alona", Toast.LENGTH_SHORT).show()
        loadHistoryTickets()
    }
    private fun onClickListenerlstHistoryTickets(position: Int)
    {
        Log.i("dea", lstTicketsData.get(position).toString())

        var intent = Intent(this.applicationContext, RegusterTicketActivity::class.java)
        intent.putExtra(TYPEDISPLAY, MODE_EDIT)
        intent.putExtra(URL_IMG, lstTicketsData.get(position))
        intent.putExtra(TICKET_DATE, lstTicketsObject.get(position).date)
        intent.putExtra(TICKET_AMOUNT, lstTicketsObject.get(position).amount.toString())
        intent.putExtra(TICKET_CREATED, lstTicketsObject.get(position).created)

        startActivity(intent)
        //startActivity(Intent(this.applicationContext, LoginActivity::class.java))

    }

    private fun loadHistoryTickets()
    {
        val ticket = Ticket()
        var childe = ticket.getTicketList()

        Toast.makeText(this, "Alona", Toast.LENGTH_SHORT).show()

        childe.listAll()
                .addOnSuccessListener { it ->

                    var text = ""

                    for (item in it.items)
                    {
                        text = item.parent.toString() +"/" + item.name
                        /*Log.i("dea", item.path)
                        Log.i("dea", item.name)
                        Log.i("dea", item.bucket)
                        Log.i("dea", item.parent.toString())
                        Log.i("dea", item.downloadUrl.toString())
                        Log.i("dea", item.metadata.toString())
                        Log.i("dea", item.root.toString())*/
                        getTicketInfo(text, item.path)
                    }
                }
    }
    private fun getTicketInfo(pUrl: String, pName: String)
    {
        val storage = FirebaseStorage.getInstance()
        val gsReference = storage.reference.child(pName)

        Log.i("dea pNAme", pName)

        gsReference.metadata.addOnSuccessListener { metadata ->
            val date = metadata.getCustomMetadata(Ticket.DATE)
            val amount = metadata.getCustomMetadata(Ticket.AMOUNT)
            val created = metadata.getCustomMetadata(Ticket.CREATED)

            val ticket = Ticket()
            ticket.date = date!!
            ticket.amount = amount!!.toDouble()
            ticket.created = created!!

            val texto = "Fecha: " + date + " - Monto; " + amount + " - Registrado el: " + created

            lstTicketsText.add(texto)
            lstTicketsData.add(pUrl)
            lstTicketsObject.add(ticket)

            var adapter = ArrayAdapter(this@HistoryActivity, android.R.layout.simple_list_item_1, lstTicketsText)
            lstHistoryTickets.adapter = adapter


        }.addOnFailureListener {
            Log.e("dea", it.toString())
        }

        /*val ONE_MEGABYTE: Long = 1024 * 1024
        gsReference.getBytes(ONE_MEGABYTE).addOnSuccessListener {
            // Data for "images/island.jpg" is returned, use this as needed

            val bitm = BitmapFactory.decodeByteArray(it, 0, it.size)
            addTicketDataText(pUrl, bitm)
            //imgProfilePicture.setImageBitmap(bitm)

        }.addOnFailureListener {
            Log.e("dea_addOnFailureListener", it.message.toString())
        }*/



    }
}