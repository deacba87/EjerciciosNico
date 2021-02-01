package com.example.myapplication

import android.content.Intent
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.snackbar.Snackbar
import kotlinx.android.synthetic.main.history_ticket_row.view.*
import java.lang.Exception

class HistoryTicketRowAdapter(val list: List<Ticket>): RecyclerView.Adapter<HistoryTicketRowHolder>()
{
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HistoryTicketRowHolder
    {
        val view = LayoutInflater
                    .from(parent.context)
                    .inflate(R.layout.history_ticket_row, parent, false)

        return HistoryTicketRowHolder(view)
    }

    override fun onBindViewHolder(holder: HistoryTicketRowHolder, position: Int)
    {
        val ticket = list[position]
        holder.setData(ticket)
        /*with(holder.ticketView)
        {
            txt_htr_date?.text = "${R.string.fecha}: ${ticket.date}"
            txt_htr_amount?.text = "${R.string.monto}: ${ticket.amount}"
        }*/
    }

    /*override fun getItemCount(): Int
    {
        TODO("Not yet implemented")
    }*/
    override fun getItemCount() = list.size

}

class HistoryTicketRowHolder(val tView: View) : RecyclerView.ViewHolder(tView), View.OnClickListener
{
    private var varView: View = tView
    private var ticketData: Ticket? = null
    init {
        tView.setOnClickListener(this)
    }

    fun setData(ticket: Ticket)
    {
        ticketData = ticket

        tView.txt_htr_date?.text = "${tView.getString(R.string.fecha)}: ${ticket.date}"

        tView.txt_htr_amount?.text = "${tView.getString(R.string.monto)}: $${ticket.amount}"
    }

    override fun onClick(v: View?)
    {
        Log.i("dea", "onClick" )

        var intent = Intent(tView.context, RegusterTicketActivity::class.java)
        try
        {
            if (tView != null)
            {
                with(intent)
                {
                    putExtra(R.string.TYPEDISPLAY.toString(), R.string.MODE_EDIT.toString())
                    putExtra(R.string.URL_IMG.toString(), ticketData!!.url)
                    putExtra(R.string.TICKET_DATE.toString(), ticketData!!.date)
                    putExtra(R.string.TICKET_AMOUNT.toString(), ticketData!!.amount.toString())
                    putExtra(R.string.TICKET_CREATED.toString(), ticketData!!.created)
                    putExtra(R.string.TICKET_PATH.toString(), ticketData!!.path)

                    tView.context.startActivity(this)
                    //startActivity(this)
                }
            }
            else
            {
                Log.e("dea", "tView == null" )
            }

        }
        catch (e: Exception)
        {
            Log.e("dea", e.message.toString() )
        }
    }

}

private fun View.getString(id: Int) = resources.getString(id)