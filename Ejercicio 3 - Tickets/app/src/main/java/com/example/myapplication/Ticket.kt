package com.example.myapplication

import android.content.Context
import android.graphics.Bitmap
import android.widget.Toast
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageMetadata
import com.google.firebase.storage.StorageReference
import java.io.ByteArrayOutputStream
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

class Ticket()
{
    companion object {
        val DATE = "date"
        val AMOUNT = "amount"
        val CREATED = "created"
        val CONTENT_TYPE = "image/jpg"
    }

    private val fileExtention = ".jpg"

    var date: String = ""
    var amount: Double = 0.0
    var photo: Bitmap? = null
    var created: String = ""
    var context: Context? = null
    var path: String = ""

    constructor(date: String, amount: Double, photo: Bitmap, created: String, context: Context): this()
    {
        this.date = date
        this.amount = amount
        this.photo = photo
        this.created = created
        this.context = context
    }

    fun registerTicket(): Boolean
    {
        val filePath = getFilePath() + getFileName()
        var storageRef = FirebaseStorage.getInstance().getReference();

        /*Metodo aparte para obtener el Byte array*/
        if (photo != null)
        {
            val baos= ByteArrayOutputStream()
            photo!!.compress(Bitmap.CompressFormat.JPEG, 100, baos)
            val data = baos.toByteArray()
            // Create file metadata including the content type
            /*val metadata = storageMetadata {
                setCustomMetadata("id", "filmId")
            }*/
            val metaData = StorageMetadata.Builder()
                    .setCustomMetadata(Ticket.DATE, date)
                    .setCustomMetadata(Ticket.AMOUNT, amount.toString())
                    .setCustomMetadata(Ticket.CREATED, created)
                    .build()

            var uploadTask = storageRef!!.child(filePath).putBytes(data, metaData)
            uploadTask
                    .addOnFailureListener{
                        Toast.makeText(context, it.message.toString(), Toast.LENGTH_LONG).show()
                    }
                    .addOnCompleteListener { task ->
                        if (task.isSuccessful)
                        {
                            Toast.makeText(context, "Ticket registrado", Toast.LENGTH_LONG).show()
                        }
                        else
                        {
                            Toast.makeText(context, "Ticket no se pudo registrar", Toast.LENGTH_LONG).show()
                        }
                    }

            return true
        }
        else
        {
            return false
        }
    }
    private  fun getFilePath(): String
    {
        var ret = SingletonLogin.getUserId() + "/Tickets/"
        return ret
    }
    private fun getFileName(): String
    {
        return LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMddHHmmss")) + fileExtention
    }
    private fun getByteArray()
    {
    }

    fun getTicketList(): StorageReference
    {
        val filePath = getFilePath()
        var storageRef = FirebaseStorage.getInstance().getReference();
        var childe = storageRef.child(filePath)
        return childe

        // Find all the prefixes and items.
        /*
        childe.listAll()
                .addOnSuccessListener { it ->

                    for (item in it.items)
                    {
                        Log.i("dea_items_parent", item.parent.toString() )
                        Log.i("dea_items_name", item.name )
                    }


                }
         */


    }


}


