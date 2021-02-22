package com.example.myapplication

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
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
        //rdbGoogle.isChecked = true
        rdbGoogle.visibility = View.GONE
        rdbMail.isChecked = true
        //init_handlers()
        initLogin()
        updateLayout(rdbGoogle)
    }

    override fun onResume()
    {
        super.onResume()
        if (SLogin.isLogged(this.applicationContext))
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
        //SingletonLogin.context = this
        // Google Auth *****************************************************************************
        val gso: GoogleSignInOptions = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestEmail()
            .build()
        // Build a GoogleSignInClient with the options specified by gso.
        SLogin.authGoogle = GoogleSignIn.getClient(this, gso)
        // Mail Auth *******************************************************************************
        SLogin.authMail = FirebaseAuth.getInstance()
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
            if (SLogin.authGoogle != null)
            {
                val signInIntent =  SLogin.authGoogle!!.signInIntent
                startActivityForResult(signInIntent, RC_SIGN_IN)
            }
        }
        else if (rdbMail.isChecked)
        {
            if (SLogin.authMail != null)
            {
                SLogin.authMail!!.signInWithEmailAndPassword(txtUser.text.toString().trim(), txtPassword.text.toString().trim() )
                    .addOnCompleteListener(this)
                    { task ->
                        if (task.isSuccessful)
                        {
                            //validateLogin()
                            if (SLogin.isLogged(this.applicationContext))
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
        if (SLogin.authMail == null)
        {
            Toast.makeText(this, "No es posible registrase", Toast.LENGTH_LONG).show()
        }
        else
        {
            if (SLogin.authMail != null)
            {
                val email =  txtUser.trimText() //txtUser.text.toString().trim()
                val password = txtPassword.trimText() //txtPassword.text.toString().trim()

                if ( email != null && email.isNotEmpty() &&
                     password != null && password.isNotEmpty())
                {
                    SLogin.authMail!!.createUserWithEmailAndPassword(email,password)
                        .addOnCompleteListener(this)
                        { task ->
                            if (task.isSuccessful)
                            {
                                if (SLogin.isLogged(this.applicationContext))
                                {
                                    val id = SLogin.getUserId()
                                    val user = User(id, email, password)
                                    val fbDB = FirebaseDatabase.getInstance().reference.child(BdText.TB_USER).child(id)
                                    fbDB.setValue(user.toHashMap()).addOnCompleteListener{
                                        if (it.isSuccessful)
                                        {
                                            Toast.makeText(this, "Usuario creado", Toast.LENGTH_LONG).show()
                                            moveToMainMenu()
                                        }
                                        else
                                            Toast.makeText(this, "Usuario NO creado", Toast.LENGTH_LONG).show()

                                    }

                                }

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
    }

    private fun moveToMainMenu()
    {
        val intent = Intent(this.applicationContext, MainMenuActivity::class.java)
        startActivity(intent)
        finish()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?)
    {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == RC_SIGN_IN && SLogin.isLogged(this.applicationContext))
            moveToMainMenu()
    }
}

fun EditText.trimText() = this.text.toString().trim()