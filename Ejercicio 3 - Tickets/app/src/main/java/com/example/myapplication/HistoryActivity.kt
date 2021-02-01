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
import kotlinx.android.synthetic.main.activity_history.*
import java.lang.Exception
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
    private val TICKET_PATH: String = "TICKET_PATH"
    //

    private var lstTicketsObject= mutableListOf<Ticket>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_history)

        //lstHistoryTickets = findViewById(R.id.lstHistoryTickets4)
        //lstHistoryTickets.setOnClickListener(View.OnClickListener { onClickListenerlstHistoryTickets() })
        lstHistoryTickets.setOnItemClickListener { arg0, arg1, position, arg3 -> onClickListenerlstHistoryTickets(position) }

        //Toast.makeText(this, "Alona", Toast.LENGTH_SHORT).show()
        loadHistoryTickets()
    }
    private fun onClickListenerlstHistoryTickets(position: Int)
    {
        var intent = Intent(this.applicationContext, RegusterTicketActivity::class.java)
        try
        {
            val obj = lstTicketsObject[position]
            if (obj != null)
            {
                intent.putExtra(TYPEDISPLAY, MODE_EDIT)
                intent.putExtra(URL_IMG, obj.url)
                intent.putExtra(TICKET_DATE, obj.date)
                intent.putExtra(TICKET_AMOUNT, obj.amount.toString())
                intent.putExtra(TICKET_CREATED, obj.created)
                intent.putExtra(TICKET_PATH, obj.path)

                startActivity(intent)
            }
        }
        catch (e: Exception)
        {

        }


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
                        /*Log.i("dea1", item.path)
                        Log.i("dea2", item.name)
                        Log.i("dea3", item.bucket)
                        Log.i("dea4", item.parent.toString())
                        Log.i("dea5", item.downloadUrl.toString())
                        Log.i("dea6", item.metadata.toString())
                        Log.i("dea7", item.root.toString())*/
                        getTicketInfo(text, item.path)
                    }
                }
    }
    private fun getTicketInfo(pUrl: String, pName: String)
    {
        val storage = FirebaseStorage.getInstance()
        val gsReference = storage.reference.child(pName)

        //Log.i("dea pNAme", pName)

        var lstTicketsText = mutableListOf<String>()

        gsReference.metadata.addOnSuccessListener { metadata ->
            val date = metadata.getCustomMetadata(Ticket.DATE)
            val amount = metadata.getCustomMetadata(Ticket.AMOUNT)
            val created = metadata.getCustomMetadata(Ticket.CREATED)

            val ticket = Ticket(date, amount?.toDouble(), created, pName, pUrl )

            val texto = ticket.getStringData()

            lstTicketsText.add(texto)
            lstTicketsObject.add(ticket)

            var adapter = ArrayAdapter(this@HistoryActivity, android.R.layout.simple_list_item_1, lstTicketsText)
            lstHistoryTickets.adapter = adapter

        }.addOnFailureListener {
            Log.e("dea", it.toString())
        }
    }
}