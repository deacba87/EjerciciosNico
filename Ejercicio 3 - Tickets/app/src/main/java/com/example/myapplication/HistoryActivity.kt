 package com.example.myapplication

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




class HistoryActivity : AppCompatActivity()
{
    private lateinit var lstHistoryTickets: ListView
    private var lstTicketsData = mutableListOf<String>()
    private var lstTicketsText = mutableListOf<String>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_history)

        lstHistoryTickets = findViewById(R.id.lstHistoryTickets4)
        //lstHistoryTickets.setOnClickListener(View.OnClickListener { onClickListenerlstHistoryTickets() })
        lstHistoryTickets.setOnItemClickListener { arg0, arg1, position, arg3 -> Toast.makeText(this, "" + position, Toast.LENGTH_SHORT).show() }

        //Toast.makeText(this, "Alona", Toast.LENGTH_SHORT).show()
        loadHistoryTickets()
    }
    private fun onClickListenerlstHistoryTickets()
    {
        Log.i("dea_selectedItem", lstHistoryTickets.selectedItem.toString())
        Log.i("dea_selectedItemId", lstHistoryTickets.selectedItemId.toString())
        Log.i("dea_selectedItemPosition", lstHistoryTickets.selectedItemPosition.toString())
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

            val texto = "Fecha: " + date + " - Monto; " + amount + " - Registrado el: " + created

            lstTicketsText.add(texto)
            lstTicketsData.add(pUrl)

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