package com.example.myapplication

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast

class HistoryActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_history)

        val ticket = Ticket()
        ticket.getTicketList()
        Toast.makeText(this, "Alona", Toast.LENGTH_SHORT).show()
    }
}