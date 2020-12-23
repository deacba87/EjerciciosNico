package com.example.myapplication

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.drawable.BitmapDrawable
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.view.View
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import com.google.android.gms.auth.api.signin.internal.Storage
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import java.io.ByteArrayOutputStream
import java.io.InputStream
import java.net.URL


class ProfileActivity : AppCompatActivity()
{
    /*--------------------------------------------------------------------------------------------*/
    private val CAMERA_PERMISSION_CODE:Int = 100
    private val CAMERA_REQUEST: Int = 1
    /*--------------------------------------------------------------------------------------------*/
    private var mStorageRef: StorageReference? = null

    /*--------------------------------------------------------------------------------------------*/
    private lateinit var txtProfileName: EditText
    private lateinit var imgProfilePicture: ImageView
    private lateinit var btnProfileSelectPicture : Button
    private lateinit var btnProfileSaveChanges: Button
    /*--------------------------------------------------------------------------------------------*/
    override fun onCreate(savedInstanceState: Bundle?)
    {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_profile)
        setElements()
        updateLayout()
        mStorageRef = FirebaseStorage.getInstance().getReference();
    }
    private fun setElements()
    {
        txtProfileName = findViewById(R.id.txtProfileName)
        imgProfilePicture = findViewById(R.id.imgProfilePicture)
        btnProfileSelectPicture = findViewById(R.id.btnProfileSelectPicture)
        btnProfileSaveChanges = findViewById(R.id.btnProfileSaveChanges)

        btnProfileSelectPicture.setOnClickListener(View.OnClickListener { onClickProfileSelectPicture() })
        btnProfileSaveChanges.setOnClickListener(View.OnClickListener { onClickProfileSaveChanges() })

    }
    private fun updateLayout()
    {
        if (SingletonLogin.isEditableName())
        {
            txtProfileName.setText(SingletonLogin.getUserName(), TextView.BufferType.EDITABLE)
        }
        else
        {
            txtProfileName.setText(SingletonLogin.getUserName(), TextView.BufferType.NORMAL)
        }

        val imgUri: String? = SingletonLogin.getUserPhoto()
        if (imgUri != null)
        {

            val storage = FirebaseStorage.getInstance()
            val gsReference = storage.getReferenceFromUrl(imgUri)
            val ONE_MEGABYTE: Long = 1024 * 1024
            gsReference.getBytes(ONE_MEGABYTE).addOnSuccessListener {
                // Data for "images/island.jpg" is returned, use this as needed

                val bitm = BitmapFactory.decodeByteArray(it, 0, it.size)
                imgProfilePicture.setImageBitmap(bitm)

            }.addOnFailureListener {
                Log.e("dea_addOnFailureListener", it.message.toString())
            }


            /*
            //imgProfilePicture.setImageURI(imgUri)
            var mIcon11: Bitmap? = null
            try {
                val ins: InputStream = URL("gs://ejercicio4tickets.appspot.com/tempora/perfil.jpg").openStream()
                mIcon11 = BitmapFactory.decodeStream(ins)
            } catch (e: Exception) {
                Log.e("Error", e.message!!)
                e.printStackTrace()
            }
            imgProfilePicture.setImageBitmap(mIcon11)*/
        }


    }
    private fun onClickProfileSelectPicture()
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
    }
    private fun onClickProfileSaveChanges()
    {
        SingletonLogin.saveUserProfile(txtProfileName.text.toString())
        updateProfilePicture()
    }

    private fun updateProfilePicture()
    {

        // Create a reference to "mountains.jpg"
        //val perfilRef = mStorageRef!!.child("perfil3.jpg")
        val bitmap = (imgProfilePicture.drawable as BitmapDrawable).bitmap
        val baos = ByteArrayOutputStream()
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos)
        val data = baos.toByteArray()

        var uploadTask = mStorageRef!!.child(SingletonLogin.getUserProfilePhotoPath()).putBytes(data)
        uploadTask
                .addOnFailureListener{
                    Toast.makeText(this, it.message.toString(), Toast.LENGTH_LONG).show()
                    Log.e("dea", it.message.toString())
                }
                .addOnCompleteListener { task ->
                    if (task.isSuccessful)
                    {
                        saveProfilePicture()
                    }
                    else
                    {

                    }
                }
    }
    private fun saveProfilePicture()
    {
        val child = mStorageRef!!.child(SingletonLogin.getUserProfilePhotoPath())
        val url = child.parent.toString() + "/" + child.name
        SingletonLogin.saveUserPhoto(url)
    }

    @Override
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
    }
    @Override

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?)
    {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == CAMERA_REQUEST && resultCode == RESULT_OK)
        {
            val photo = data?.extras!!["data"] as Bitmap?
            imgProfilePicture.setImageBitmap(photo)
        }
    }

}