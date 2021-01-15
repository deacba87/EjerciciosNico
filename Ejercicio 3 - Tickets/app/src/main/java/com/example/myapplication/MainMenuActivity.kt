package com.example.myapplication

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.Toast

class MainMenuActivity : AppCompatActivity()
{
    /*
    Comentarios
    - Si, el menu es feo  y podría haber usado el menu que suele ir arriba
     */
    private val MODE_REGISTER: String = "REGISTER"
    private val TYPEDISPLAY: String = "TYPE_DISPLAY"
    /*--------------------------------------------------------------------------------------------*/
    private lateinit var btnProfile : Button
    private lateinit var btnRegisterTicket : Button
    private lateinit var btnHistory : Button
    private lateinit var btnLogout : Button
    /*--------------------------------------------------------------------------------------------*/

    override fun onCreate(savedInstanceState: Bundle?)
    {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main_menu)
        setElements()
        setHandlers()
    }

    private fun setElements()
    {
        btnProfile = findViewById(R.id.btnProfile)
        btnRegisterTicket = findViewById(R.id.btnRegisterTicket)
        btnHistory  = findViewById(R.id.btnHistory)
        btnLogout = findViewById(R.id.btnLogOut)
    }
    private fun setHandlers()
    {
        btnProfile.setOnClickListener(View.OnClickListener { moveToProfile() })
        btnRegisterTicket.setOnClickListener(View.OnClickListener { moveToRegisterTicket() })
        btnHistory.setOnClickListener(View.OnClickListener { moveToHistory() })
        btnLogout.setOnClickListener(View.OnClickListener { logOut() })
    }
    private fun moveToProfile()
    {
        var intent = Intent(this.applicationContext, ProfileActivity::class.java)
        startActivity(intent)
    }
    private fun moveToRegisterTicket()
    {
        var intent = Intent(this.applicationContext, RegusterTicketActivity::class.java)
        intent.putExtra(TYPEDISPLAY, MODE_REGISTER)
        startActivity(intent)
    }
    private fun moveToHistory()
    {
        var intent = Intent(this.applicationContext, HistoryActivity::class.java)
        startActivity(intent)
    }
    private fun moveToLogin()
    {
        /*var intent = Intent(this.applicationContext, LoginActivity::class.java)
        startActivity(intent)*/
        startActivity(Intent(this.applicationContext, LoginActivity::class.java))
    }
    private fun logOut()
    {
        SingletonLogin.logOut()

        if (SingletonLogin.logOut())
        {
            moveToLogin()
        }
        else
        {
            Toast.makeText(this, "Algo anda mal", Toast.LENGTH_LONG)
        }


    }
}