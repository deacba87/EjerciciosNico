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
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.myapplication.R.string.*
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import kotlinx.android.synthetic.main.activity_history.*
import java.lang.Exception
import java.text.FieldPosition


 class HistoryActivity : AppCompatActivity()
{
    val TAG = "dea_HistoryActivity"
    /*
    private val MODE_EDIT: String = "EDIT"
    private val URL_IMG: String = "URL_IMG"
    private val TYPEDISPLAY: String = "TYPE_DISPLAY"
    private val TICKET_DATE: String = "TICKET_DATE"
    private val TICKET_AMOUNT: String = "TICKET_AMOUNT"
    private val TICKET_CREATED: String = "TICKET_CREATED"
    private val TICKET_PATH: String = "TICKET_PATH"
    */

    private var lstTicketsObject= mutableListOf<Ticket>()
    //private var lstTickets: MutableList<Ticket>? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_history)
        title = "Historial"

        //lstHistoryTickets = findViewById(R.id.lstHistoryTickets4)
        //lstHistoryTickets.setOnClickListener(View.OnClickListener { onClickListenerlstHistoryTickets() })
        //lstHistoryTickets.setOnItemClickListener { arg0, arg1, position, arg3 -> onClickListenerlstHistoryTickets(position) }

        rcv_history_tickets.setHasFixedSize(true)
        rcv_history_tickets.layoutManager = LinearLayoutManager(this)
        //rcv_history_tickets.adapter = HistoryTicketRowAdapter(lstTicketsObject)

        //Toast.makeText(this, "Alona", Toast.LENGTH_SHORT).show()
        loadHistoryTickets()
    }
    /*private fun onClickListenerlstHistoryTickets(position: Int)
    {
        var intent = Intent(this.applicationContext, RegusterTicketActivity::class.java)
        try
        {
            val obj = lstTicketsObject[position]
            if (obj != null)
            {
                with(intent)
                {
                    putExtra(TYPEDISPLAY.toString(), MODE_EDIT.toString())
                    putExtra(URL_IMG.toString(), obj.url)
                    putExtra(TICKET_DATE.toString(), obj.date)
                    putExtra(TICKET_AMOUNT.toString(), obj.amount.toString())
                    putExtra(TICKET_CREATED.toString(), obj.created)
                    putExtra(TICKET_PATH.toString(), obj.path)

                    startActivity(this)
                }
            }
        }
        catch (e: Exception)
        {

        }
    }*/

    private fun loadHistoryTickets()
    {
        /*val ticket = Ticket()
        var childe = ticket.getTicketList()

        Toast.makeText(this, "Alona", Toast.LENGTH_SHORT).show()

        childe.listAll()
                .addOnSuccessListener { it ->

                    var text = ""
                    var pos = 0
                    for (item in it.items)
                    {
                        pos ++
                        //text = item.parent.toString() +"/" + item.name
                        text = "${item.parent.toString()}/${item.name}"
                        /*Log.i("dea1", item.path)
                        Log.i("dea2", item.name)
                        Log.i("dea3", item.bucket)
                        Log.i("dea4", item.parent.toString())
                        Log.i("dea5", item.downloadUrl.toString())
                        Log.i("dea6", item.metadata.toString())
                        Log.i("dea7", item.root.toString())*/
                        Toast.makeText(this, "$pos de ${it.items.size}", Toast.LENGTH_SHORT).show()
                        getTicketInfo(text, item.path)
                    }
                    Toast.makeText(this, "Termino", Toast.LENGTH_SHORT).show()


                }
                .addOnFailureListener {
                    Toast.makeText(this, it.message, Toast.LENGTH_LONG).show()
                }*/
        getTicketsHistoryBd()
    }

    private fun getTicketsHistoryBd()
    {
        val userTicketsBd = FirebaseDatabase.getInstance().getReference(BdText.TB_TICKETS).child(SLogin.getUserId())
        userTicketsBd.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                Log.i(TAG, "getTicketsHistoryBd -> onDataChange")
                for (row in snapshot.children)
                {
                    Log.i(TAG, "onDataChange -> for (row in snapshot.children)")

                    try {

                        val a = row.child(BdText.COL_AMOUNT).value.toString()
                        Log.i(TAG, "row.child(BdText.COL_AMOUNT).toString() = $a")
                        val b = a.toDouble()
                        Log.i(TAG, "toSDouble() = $b")
                    }
                    catch (e: Exception)
                    {
                        Log.i(TAG, "Exception = ${e.message}")
                    }

                    lstTicketsObject.add(Ticket(
                            row.key.toString(),
                            row.child(BdText.COL_DATE).value.toString(),
                            row.child(BdText.COL_AMOUNT).value.toString().toDouble(),
                            row.child(BdText.COL_CREATED).value.toString(),
                            row.child(BdText.COL_PATH).value.toString() ,
                            row.child(BdText.COL_PHOTOURL).value.toString(),
                            row.child(BdText.COL_IDUSER).value.toString())
                    )
                }
                Log.i(TAG, "onDataChange -> Fin for" )
                rcv_history_tickets.adapter = HistoryTicketRowAdapter(lstTicketsObject)
            }

            override fun onCancelled(error: DatabaseError) {
                Log.i(TAG, "Indra")
            }
        })
    }

    /*private fun getTicketInfo(pUrl: String, pName: String)
    {
        val storage = FirebaseStorage.getInstance()
        val gsReference = storage.reference.child(pName)

        //Log.i("dea pNAme", pName)

        var lstTicketsText = mutableListOf<String>()

        gsReference.metadata.addOnSuccessListener { metadata ->
            val date = metadata.getCustomMetadata(Ticket.DATE).toString()
            val amount = metadata.getCustomMetadata(Ticket.AMOUNT).toString()
            val created = metadata.getCustomMetadata(Ticket.CREATED).toString()
            val ticket = Ticket(date, amount?.toDouble(), created, pName, pUrl, SLogin.getUserId() )

            /*val texto = ticket.getStringData()
            lstTicketsText.add(texto)*/
            lstTicketsObject.add(ticket)
            rcv_history_tickets.adapter = HistoryTicketRowAdapter(lstTicketsObject)

            /*var adapter = ArrayAdapter(this@HistoryActivity, android.R.layout.simple_list_item_1, lstTicketsText)
            lstHistoryTickets.adapter = adapter*/

        }.addOnFailureListener {
            Log.e("dea", it.toString())
        }
    }*/
}