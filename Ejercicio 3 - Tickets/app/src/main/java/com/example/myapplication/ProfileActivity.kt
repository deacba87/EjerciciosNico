package com.example.myapplication

import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.drawable.BitmapDrawable
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.view.View
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import kotlinx.android.synthetic.main.activity_profile.*
import java.io.ByteArrayOutputStream


class ProfileActivity : AppCompatActivity()
{
    /*--------------------------------------------------------------------------------------------*/
    private val TAG = "deacba"
    private val CAMERA_PERMISSION_CODE:Int = 100
    private val IMG_REQUEST: Int = 1
    /*--------------------------------------------------------------------------------------------*/
    private var mStorageRef: StorageReference? = null
    private var bitmapProfilePicture: Bitmap? ? = null
    private var user: User? = null
    /*--------------------------------------------------------------------------------------------*/
    override fun onCreate(savedInstanceState: Bundle?)
    {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_profile)
        //setElements()
        updateLayout()
        mStorageRef = FirebaseStorage.getInstance().getReference();

        if (bitmapProfilePicture != null)
            img_profile_picture.setImageBitmap(bitmapProfilePicture)

    }
    /*private fun setElements()
    {
        txtProfileName = findViewById(R.id.txtProfileName)
        imgProfilePicture = findViewById(R.id.imgProfilePicture)
        btnProfileSelectPicture = findViewById(R.id.btnProfileSelectPicture)
        btnProfileSaveChanges = findViewById(R.id.btnProfileSaveChanges)

        btnProfileSelectPicture.setOnClickListener(View.OnClickListener { onClickProfileSelectPicture() })
        btnProfileSaveChanges.setOnClickListener(View.OnClickListener { onClickProfileSaveChanges() })

    }*/
    private fun updateLayout()
    {
        if (SLogin.isEditableName())
        {
            txt_profile_name.setText(SLogin.getUserName(), TextView.BufferType.EDITABLE)
        }
        else
        {
            txt_profile_name.setText(SLogin.getUserName(), TextView.BufferType.NORMAL)
        }

        //val userId = SLogin.getUserId()
        val fbDB = FirebaseDatabase.getInstance().getReference(BdText.TB_USER)
        // Read from the database

        Log.i(TAG, "Loading")
        //val progressBar = ProgressDialog.show(this.applicationContext, "", "Loading...", true)
        val loadingDialog = LoadingDialog(this)
        loadingDialog.startLoadingAnimation()
        fbDB.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                //progressBar.dismiss()
                //var value = dataSnapshot.childrenCount
                //val userId = SLogin.getUserId()

                val userDb = dataSnapshot.child(SLogin.getUserId())
                //val userName = userDb.child(BdText.COL_NAME)

                user = User( SLogin.getUserId(),
                             userDb.child(BdText.COL_EMAIL).value.toString(),
                             userDb.child(BdText.COL_NAME).value.toString(),
                             userDb.child(BdText.COL_PASSWORD).value.toString(),
                             userDb.child(BdText.COL_PPICTURE).value.toString() )

                txt_profile_name.setText(user!!.name)

                val imgUri = user!!.profilePicture
                if (imgUri.isNotEmpty())
                {
                    val storage = FirebaseStorage.getInstance()

                    val gsReference = storage.getReferenceFromUrl(imgUri)
                    val ONE_MEGABYTE: Long = 1024 * 1024
                    gsReference.getBytes(ONE_MEGABYTE).addOnSuccessListener {
                        // Data for "images/island.jpg" is returned, use this as needed
                        val bitm = BitmapFactory.decodeByteArray(it, 0, it.size)
                        img_profile_picture.setImageBitmap(bitm)
                        loadingDialog.dismissDialog()

                    }.addOnFailureListener {
                        Log.e(TAG, it.message.toString())
                        loadingDialog.dismissDialog()
                    }
                }
                else
                {
                    loadingDialog.dismissDialog()
                }
            }

            override fun onCancelled(error: DatabaseError) {
                // Failed to read value
                loadingDialog.dismissDialog()
                Log.e(TAG, "Failed to read value.", error.toException())
            }
        })


    }
    fun onClickProfileSelectPicture(view: View)
    {
        /*if (checkSelfPermission(Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED)
        {
            requestPermissions(arrayOf(Manifest.permission.CAMERA), CAMERA_PERMISSION_CODE)
        }
        else
        {
            val cameraIntent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
            startActivityForResult(cameraIntent, CAMERA_REQUEST)
        }*/

        val intent = Intent()
        intent.type = "image/*"
        intent.action = Intent.ACTION_GET_CONTENT

        val imgChooser = Intent.createChooser(intent, "Selecciona una foto")
        startActivityForResult(imgChooser, IMG_REQUEST)

    }
    fun onClickProfileSaveChanges(view: View)
    {
        if (user!=null)
        {
            user!!.name = txt_profile_name.text.toString()
            SLogin.saveUserProfile(user!!.name, this.applicationContext)
            saveUser()
            updateProfilePicture()
        }
    }

    private fun updateProfilePicture()
    {

        // Create a reference to "mountains.jpg"
        //val perfilRef = mStorageRef!!.child("perfil3.jpg")
        val bitmap = (img_profile_picture.drawable as BitmapDrawable).bitmap
        val baos = ByteArrayOutputStream()
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos)
        val data = baos.toByteArray()

        var uploadTask = mStorageRef!!.child(SLogin.getUserProfilePhotoPath()).putBytes(data)
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
                        Log.e("dea", task.result.toString())
                        Log.e("dea", task.exception.toString())
                    }
                }
    }
    private fun saveProfilePicture()
    {
        val child = mStorageRef!!.child(SLogin.getUserProfilePhotoPath())
        val url = child.parent.toString() + "/" + child.name
        SLogin.saveUserPhoto(url)
        if (user!= null)
        {
            user!!.profilePicture = url
            saveUser()
        }
    }
    private fun saveUser()
    {
        if (user!= null)
        {
            val fbDB = FirebaseDatabase.getInstance().reference.child(BdText.TB_USER).child(user!!.id)
            fbDB.setValue(user!!.toHashMap()).addOnCompleteListener{
                if (it.isSuccessful)
                {
                    Toast.makeText(this, "Usuario actualizado", Toast.LENGTH_LONG).show()
                }
                else
                    Toast.makeText(this, "Usuario NO actualizado", Toast.LENGTH_LONG).show()

            }
        }
    }

    /*@Override
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
    }*/
    @Override
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?)
    {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == IMG_REQUEST &&
            resultCode == RESULT_OK &&
            data != null &&
            data.data != null)
        {
            /*val photo = data?.extras!!["data"] as Bitmap?
            imgProfilePicture.setImageBitmap(photo)*/
            val bitmap = try {
                MediaStore.Images.Media.getBitmap(contentResolver, data.data)
            }
            catch (e: Exception) { null }

            bitmap?.let {
                //imgProfilePicture.setImageBitmap(bitmap)
                bitmapProfilePicture = it
                img_profile_picture?.setImageBitmap(it)
            }

        }
    }

}