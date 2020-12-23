package com.example.myapplication

import android.R
import android.content.Context
import android.net.Uri
import android.util.Log
import android.widget.Toast
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.ktx.userProfileChangeRequest

object SingletonLogin
{
    /* Atributos */
    var authMail: FirebaseAuth?
    var authGoogle: GoogleSignInClient?
    var context: Context?

    private var userGoogle: GoogleSignInAccount? = null
    private var userMail: FirebaseUser? = null
    private var loginType: LoginType? = null

    init
    {
        authMail = null
        authGoogle = null
        context = null
    }
    /* Metodos */
    fun isLogged(): Boolean
    {
        var logged: Boolean = isLoggedGoogle()
        if (!logged)
        {
            logged = isLoggedMail()
        }
        return logged
    }

    private fun isLoggedGoogle(): Boolean
    {

        if (context == null)
        {
            return false
        }
        val userGoogleAux = GoogleSignIn.getLastSignedInAccount(context)
        if (userGoogle != null)
        {
            loginType = LoginType.GOOGLE
            userGoogle = userGoogleAux!!
            return true
        }
        else
        {
            return false
        }
    }

    private fun isLoggedMail(): Boolean
    {
        val userMailAux = authMail?.currentUser
        if (userMailAux != null)
        {
            loginType = LoginType.MAIL
            userMail = userMailAux!!
            return true
        }
        else
        {
            return false
        }
    }

    fun logOut(): Boolean
    {
        var result: Boolean = false
        when(loginType)
        {
            LoginType.GOOGLE -> result = logOutGoogle()
            LoginType.MAIL -> result = logOutMail()
        }
        return result
    }
    private fun logOutGoogle(): Boolean
    {
        if (authGoogle == null)
        {
            return false
        }
        else
        {
            authGoogle!!.signOut()
            return true
        }

    }
    private fun logOutMail(): Boolean
    {
        if (authMail == null)
        {
            return false
        }
        else
        {
            authMail!!.signOut()
            return true
        }
    }

    fun isEditableName(): Boolean
    {
        var result: Boolean = true
        if (loginType == LoginType.GOOGLE)
        {
            result = false
        }
        return result
    }

    fun getUserProfilePhotoPath():String
    {
        var result: String = ""
        when(loginType)
        {
            LoginType.GOOGLE -> result = getUserProfilePhotoPathMail()
            LoginType.MAIL -> result = getUserProfilePhotoPathMail()
        }
        return result
    }
    private fun getUserProfilePhotoPathGoogle()
    {

    }
    private fun getUserProfilePhotoPathMail():String
    {
        val path = userMail?.uid.toString() + "/Profile/profilePicture.jpg"
        return path
    }
    //
    fun getUserTicketPath():String
    {
        var result: String = ""
        when(loginType)
        {
            LoginType.GOOGLE -> result = getUserTicketPathMail()
            LoginType.MAIL -> result = getUserTicketPathMail()
        }
        return result
    }
    private fun getUserTicketPathGoogle()
    {

    }
    private fun getUserTicketPathMail():String
    {
        val path = userMail?.uid.toString() + "/Tickets/"
        return path
    }
    //
    fun getUserName(): String
    {
        var result: String = ""
        when(loginType)
        {
            LoginType.GOOGLE -> result = getUserNameGoogle()
            LoginType.MAIL -> result = getUserNameMail()
        }
        return result
    }

    fun getUserNameGoogle(): String
    {
        var name: String = userGoogle?.displayName.toString()
        if (name == null)
        {
            name = "nullo"
        }
        else if( name == "")
        {
            name = "vacio"
        }
        return name
    }

    fun getUserNameMail(): String
    {

        Log.i("dea_uid", userMail?.uid.toString())

        var name: String = userMail?.displayName.toString()
        if (name == null)
        {
            name = "nullo"
        }
        else if( name == "")
        {
            name = "vacio"
        }
        return name
    }

    fun getUserPhoto():String?
    {
        var result: String? = null
        when(loginType)
        {
            LoginType.GOOGLE -> result = getUserPhotoMail()
            LoginType.MAIL -> result = getUserPhotoMail()
        }
        return result
    }
    fun getUserPhotoGoogle()
    {

    }
    fun getUserPhotoMail():String?
    {
         return userMail?.photoUrl.toString()
    }

    fun saveUserProfile(pName: String)
    {
        when(loginType)
        {
            LoginType.GOOGLE -> saveUserProfileGoogle()
            LoginType.MAIL -> saveUserProfileMail(pName)
        }
    }
    private fun saveUserProfileGoogle()
    {

    }
    private fun saveUserProfileMail(pName: String)
    {
        //val user = Firebase.auth.currentUser
        val profileUpdates = userProfileChangeRequest {
            displayName = pName
            //photoUri = Uri.parse("https://example.com/jane-q-user/profile.jpg")
        }

        userMail!!.updateProfile(profileUpdates)
            .addOnCompleteListener { task ->
                if (task.isSuccessful)
                {
                    Toast.makeText(context, "Datos actualizados", Toast.LENGTH_LONG).show()
                }
                else
                {
                    Log.d("dea", "User profile not updated.")
                }
            }

    }

    fun saveUserPhoto(pUrl: String)
    {
        when(loginType)
        {
            LoginType.GOOGLE -> saveUserPhotoGoogle(pUrl)
            LoginType.MAIL -> saveUserPhotoMail(pUrl)
        }
    }
    private fun saveUserPhotoGoogle(pUrl: String)
    {

    }
    private fun saveUserPhotoMail(pUrl: String)
    {
        //val user = Firebase.auth.currentUser
        val profileUpdates = userProfileChangeRequest {
            //displayName = pName
            photoUri = Uri.parse(pUrl)
        }

        userMail!!.updateProfile(profileUpdates)
                .addOnCompleteListener { task ->
                    if (task.isSuccessful)
                    {
                        //Toast.makeText(context, "Datos actualizados", Toast.LENGTH_LONG).show()
                        Log.i("deafoto", task.result.toString())
                    }
                    else
                    {
                        Log.e("deafoto", "User profile not updated.")
                        Log.i("deafoto", task.exception.toString())
                    }
                }
    }
}