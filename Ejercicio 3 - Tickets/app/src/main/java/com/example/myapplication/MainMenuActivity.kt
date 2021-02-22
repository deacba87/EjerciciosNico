package com.example.myapplication

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import com.example.myapplication.R.string.MODE_REGISTER
import com.example.myapplication.R.string.TYPEDISPLAY
import com.google.android.material.snackbar.Snackbar

class MainMenuActivity : AppCompatActivity()
{
    override fun onCreate(savedInstanceState: Bundle?)
    {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main_menu)
    }

    fun moveToProfile(view: View)
    {
        var intent = Intent(this.applicationContext, ProfileActivity::class.java)
        startActivity(intent)
    }
    fun moveToRegisterTicket(view: View)
    {
        val intent = Intent(this.applicationContext, RegusterTicketActivity::class.java)
        intent.putExtra(TYPEDISPLAY.toString(), MODE_REGISTER.toString())
        startActivity(intent)
    }
    fun moveToHistory(view: View)
    {
        val intent = Intent(this.applicationContext, HistoryActivity::class.java)
        startActivity(intent)
    }
    private fun moveToLogin()
    {
        /*val intent = Intent(this.applicationContext, LoginActivity::class.java)
        startActivity(intent)*/
        startActivity(Intent(this.applicationContext, LoginActivity::class.java))
    }
    fun logOut(view: View)
    {
        //SingletonLogin.logOut()
        if (SLogin.logOut())
            moveToLogin()
        else
            Snackbar.make(view, "No se pudo desloguear", Snackbar.LENGTH_LONG)

    }
}