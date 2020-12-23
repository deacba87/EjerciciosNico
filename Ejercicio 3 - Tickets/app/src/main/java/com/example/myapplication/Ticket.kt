package com.example.myapplication

import android.graphics.Bitmap
import java.time.temporal.TemporalAmount
import java.util.*

class Ticket()
{
    private var date: String = ""
    private var amount: Double = 0.0
    private var photo: Bitmap? = null
    private var created: String = ""
    constructor(date: String, amount: Double, photo: Bitmap, created: String): this()
    {
        this.date = date
        this.amount = amount
        this.photo = photo
        this.created = created

    }


}