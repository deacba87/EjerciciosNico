package com.example.myapplication

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.auth.FirebaseAuth
import kotlinx.android.synthetic.main.activity_login.*


class LoginActivity : AppCompatActivity()
{
    /*--------------------------------------------------------------------------------------------*/
    /*private lateinit var rdbGoogle : RadioButton
    private lateinit var rdbMail : RadioButton
    private lateinit var txtUser : TextView
    private lateinit var txtPassword : TextView
    private lateinit var btnLogin : Button
    private lateinit var btnRegister : Button*/
    /*--------------------------------------------------------------------------------------------*/
    private val RC_SIGN_IN = 1
    /*--------------------------------------------------------------------------------------------*/

    override fun onCreate(savedInstanceState: Bundle?)
    {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        //init_elements()
        rdbGoogle.isChecked = true
        //init_handlers()
        initLogin()
        updateLayout(rdbGoogle)
    }

    override fun onResume()
    {
        super.onResume()
        if (SingletonLogin.isLogged())
            moveToMainMenu()
    }

    /*private fun init_elements()
    {
        rdbGoogle = findViewById(R.id.rdbGoogle)
        rdbMail = findViewById(R.id.rdbMail)
        txtUser = findViewById(R.id.txtUser)
        txtPassword = findViewById(R.id.txtPassword)
        btnLogin = findViewById(R.id.btnLogin)
        btnRegister = findViewById(R.id.btnRegister)
    }*/

    private fun initLogin()
    {
        SingletonLogin.context = this
        // Google Auth *****************************************************************************
        val gso: GoogleSignInOptions = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestEmail()
            .build()
        // Build a GoogleSignInClient with the options specified by gso.
        SingletonLogin.authGoogle = GoogleSignIn.getClient(this, gso)
        // Mail Auth *******************************************************************************
        SingletonLogin.authMail = FirebaseAuth.getInstance()
    }

    /*private fun init_handlers()
    {
        rdbGoogle.isChecked = true

        //rdbGoogle.setOnClickListener(View.OnClickListener { update_layout() })
        //rdbMail.setOnClickListener(View.OnClickListener { update_layout() })

        //btnLogin.setOnClickListener(View.OnClickListener { login() })
        //btnRegister.setOnClickListener(View.OnClickListener { register() })
    }*/

    fun updateLayout(view: View)
    {
        if ( rdbGoogle.isChecked)
        {
            txtUser.visibility = View.GONE
            txtPassword.visibility = View.GONE
            btnRegister.visibility = View.GONE
        }
        else
        {
            txtUser.visibility = View.VISIBLE
            txtPassword.visibility = View.VISIBLE
            btnRegister.visibility = View.VISIBLE
        }
    }
    fun login(view: View)
    {
        if (rdbGoogle.isChecked )
        {
            if (SingletonLogin.authGoogle != null)
            {
                val signInIntent =  SingletonLogin.authGoogle!!.signInIntent
                startActivityForResult(signInIntent, RC_SIGN_IN)
            }
        }
        else if (rdbMail.isChecked)
        {
            if (SingletonLogin.authMail != null)
            {
                SingletonLogin.authMail!!.signInWithEmailAndPassword(txtUser.text.toString(), txtPassword.text.toString() )
                    .addOnCompleteListener(this)
                    { task ->
                        if (task.isSuccessful)
                        {
                            //validateLogin()
                            if (SingletonLogin.isLogged())
                                moveToMainMenu()
                        }
                        else
                        {
                            //Toast.makeText(this,"No fue posible realizar el inicio de sesión", Toast.LENGTH_LONG).show()
                            Snackbar.make(view, "No fue posible realizar el inicio de sesión", Snackbar.LENGTH_LONG)
                        }
                    }

            }
        }
    }
    /*private fun validateLogin()
    {
        if (SingletonLogin.isLogged())
            moveToMainMenu()
    }*/
    fun register(view: View)
    {
        if (SingletonLogin.authMail == null)
        {
            Toast.makeText(this, "No es posible registrase", Toast.LENGTH_LONG).show()
        }
        else
        {
            if (SingletonLogin.authMail != null)
            {
                SingletonLogin.authMail!!.createUserWithEmailAndPassword(txtUser.text.toString(),txtPassword.text.toString())
                    .addOnCompleteListener(this)
                    { task ->
                        if (task.isSuccessful)
                        {
                            //validateLogin()
                            if (SingletonLogin.isLogged())
                                moveToMainMenu()
                        }
                        else
                        {
                            Toast.makeText(
                                this,
                                "No fue posible realizar el registro",
                                Toast.LENGTH_LONG
                            ).show()
                        }
                    }
            }
        }
    }

    private fun moveToMainMenu()
    {
        val intent = Intent(this.applicationContext, MainMenuActivity::class.java)
        startActivity(intent)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?)
    {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == RC_SIGN_IN && SingletonLogin.isLogged())
            moveToMainMenu()
    }
}