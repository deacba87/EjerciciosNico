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
        val HM_AMOUNT = "AMOUNT"
    }

    private val fileExtention = ".jpg"

    var id: String = ""
    var userid: String = ""
    var date: String = ""
    var amount: Double = 0.0
    var photo: Bitmap? = null
    var created: String = ""
    var context: Context? = null
    var path: String = ""
    var url: String = ""

    constructor(date: String, amount: Double, photo: Bitmap, created: String, context: Context, userid: String): this()
    {
        this.date = date
        this.amount = amount
        this.photo = photo
        this.created = created
        this.context = context
        this.userid = userid
    }
    constructor(id: String, date: String, amount: Double, created: String, path: String, url: String, userid: String): this()
    {
        this.id = id
        this.date = date
        this.amount = amount
        this.created = created
        this.path = path
        this.url = url
        this.userid = userid
    }
    /*constructor(date: String, amount: Double, created: String, path: String, url: String, userid: String): this()
    {
        this.date = date
        this.amount = amount
        this.created = created
        this.path = path
        this.url = url
        this.userid = userid
    }*/

    fun resetData()
    {
        id = ""
        userid = ""
        date = ""
        amount = 0.0
        photo = null
        created = ""
        context = null
        path = ""
        url = ""
    }

    fun registerTicket(): Boolean
    {
        val urlTemp = getFilePath() + getFileName()
        var storageRef = FirebaseStorage.getInstance().getReference();

        /*Metodo aparte para obtener el Byte array*/
        if (photo != null)
        {
            val baos= ByteArrayOutputStream()
            photo!!.compress(Bitmap.CompressFormat.JPEG, 100, baos)
            val data = baos.toByteArray()
            // Create file metadata including the content type
            val metaData = StorageMetadata.Builder()
                    .setCustomMetadata(DATE, date)
                    .setCustomMetadata(AMOUNT, amount.toString())
                    .setCustomMetadata(CREATED, created)
                    .build()

            var uploadTask = storageRef!!.child(urlTemp).putBytes(data, metaData)
            url = storageRef.child(urlTemp).toString()
            path = storageRef.child(urlTemp).toString()
            uploadTask
                    .addOnFailureListener{
                        Toast.makeText(context, it.message.toString(), Toast.LENGTH_LONG).show()
                    }
                    .addOnCompleteListener { task ->
                        if (task.isSuccessful)
                        {
                            val storageRef = FirebaseStorage.getInstance().reference;

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

    fun getStringData() = "Fecha: " + date + " - Monto; " + amount + " - Registrado el: " + created

    private  fun getFilePath(): String
    {
        /*var ret = SingletonLogin.getUserId() + "/Tickets/"
        return ret*/
        return  SLogin.getUserId() + "/Tickets/"
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
        /*val filePath = getFilePath()
        val storageRef = FirebaseStorage.getInstance().getReference();
        val childe = storageRef.child(filePath)
        return childe*/
        val storageRef = FirebaseStorage.getInstance().reference;
        return storageRef.child(getFilePath())
    }

    fun toHashMap(typeMap: String): HashMap<String, String>
    {

        val hashMap = HashMap<String, String>()
        with(hashMap)
        {
            when(typeMap)
            {
                HM_AMOUNT -> {
                    put(BdText.COL_AMOUNT, amount.toString())
                }
                else ->
                {
                    put(BdText.COL_DATE, date)
                    put(BdText.COL_AMOUNT, amount.toString())
                    put(BdText.COL_IDUSER, userid)
                    put(BdText.COL_CREATED, created)
                    put(BdText.COL_PATH, path)
                    put(BdText.COL_PHOTOURL, url)


                }
            }
        }
        return hashMap
    }

}


