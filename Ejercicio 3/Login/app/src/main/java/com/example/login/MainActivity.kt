
package com.example.login
/* Notas
1- En algun lado lei que kotlin me debería tomar los elementos del container directamente sin necesidad de usar el findView, pero mis muñones no logran hacerlo andar
2- Se que el Toast solo es mas feo que golpearse el dedo pequeño, pero me diste in dia para hacer esto y en la hora de trabajo estoy haciendo los cursos
3- Pense que la propiedad onClic de boton me iba a permitir no hacer el listener en el onCreate, pero me da error de instalacion.. Todavia no arranque el curso de kotlin en androoid (estoy por el BASICO), asi que no le prestes atencion a esta burrada mia
 */

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.constraintlayout.widget.ConstraintLayout
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.tasks.OnCompleteListener
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.ktx.Firebase

class MainActivity : AppCompatActivity()
{
    private val RC_SIGN_IN = 1

    private var logVis = View.VISIBLE
    /**/
    private lateinit var btnLogin: Button
    private lateinit var btnLogout: Button
    private lateinit var rdbGoogle: RadioButton
    private lateinit var rdbCorreo: RadioButton
    private lateinit var lytLog: LinearLayout
    private lateinit var txtUsuario: TextView
    private lateinit var txtPass: TextView
    /**/
    //private lateinit var auth: FirebaseAuth
    private lateinit var mGoogleSignInClient: GoogleSignInClient
    /**/

    override fun onCreate(savedInstanceState: Bundle?)
    {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        //------------------------------------------------------------------------------------------
        btnLogin = findViewById(R.id.btnIngresar)
        btnLogout = findViewById(R.id.btnLogOut)
        rdbGoogle = findViewById(R.id.rdbGoogle)
        rdbCorreo = findViewById(R.id.rdbCorreo)
        lytLog = findViewById(R.id.lytLog)
        txtUsuario = findViewById(R.id.txtUsuario)
        txtPass = findViewById(R.id.txtPass)

        //------------------------------------------------------------------------------------------
        btnLogin.setOnClickListener(
                View.OnClickListener { validarLogin() }
        )

        btnLogout.setOnClickListener(
                View.OnClickListener { realizarLogout() }
        )
        //------------------------------------------------------------------------------------------
        rdbGoogle.isChecked = true
        //------------------------------------------------------------------------------------------
        // Configure sign-in to request the user's ID, email address, and basic
        // profile. ID and basic profile are included in DEFAULT_SIGN_IN.
        val gso: GoogleSignInOptions = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .build()
        // Build a GoogleSignInClient with the options specified by gso.
        mGoogleSignInClient = GoogleSignIn.getClient(this, gso)

        // Initialize Firebase Auth
        /*auth = FirebaseAuth.getInstance()*/

    }

    override fun onStart()
    {
        super.onStart()

        val txtSaludo: TextView = findViewById(R.id.txtSaludo)

        // Check for existing Google Sign In account, if the user is already signed in
        // the GoogleSignInAccount will be non-null.
        val account = GoogleSignIn.getLastSignedInAccount(this)
        //val currentUser = auth.currentUser
        var text: String
        if (account == null /*&& currentUser == null*/)
        {
            text = "Usted no se encuentra logueado"
        }
        else
        {
            if (account != null)
            {
                text =  "Bienvenido " + account.displayName.toString()
            }
            else
            {
                text = "Bienvenido " //+ currentUser.toString()
            }

        }

        txtSaludo.text = text
        updateInterface()
    }

    private fun updateInterface()
    {
        val account = GoogleSignIn.getLastSignedInAccount(this)
        //val currentUser = auth.currentUser
        /*if ( account == null &&
             currentUser == null)
        {
            layout.visibility = View.VISIBLE
            btnLogout.visibility = View.INVISIBLE
        }
        else
        {
            val currentUser = auth.currentUser
            if (currentUser == null)
            {

            }
            else
            {
                layout.visibility = View.INVISIBLE
                btnLogout.visibility = View.VISIBLE
            }
        }*/
    }

    fun login(view: View)
    {
        validarLogin()
    }

    private fun validarLogin()
    {
        var oValidacion: ValidacionLogin = ValidacionLogin()
        var usuario: TextView = findViewById(R.id.txtUsuario)
        var pass: TextView = findViewById(R.id.txtPass)

        val (a, b) = oValidacion.validarIntegridad(usuario.text.toString(), pass.text.toString())

        if (a == true)
        {
            multLogin()
        }
        else
        {
            Toast.makeText(this.applicationContext, b.toString(), Toast.LENGTH_LONG)
        }

    }

    private fun multLogin()
    {
        if (rdbGoogle.isChecked)
        {
            val signInIntent = mGoogleSignInClient.signInIntent
            startActivityForResult(signInIntent, RC_SIGN_IN)
        }
        else if(rdbCorreo.isChecked)
        {
            /*auth.signInWithEmailAndPassword(txtUsuario.text.toString(), txtPass.text.toString())
                    .addOnCompleteListener(this)
                    { task ->
                        if (task.isSuccessful)
                        {
                            Toast.makeText(this.applicationContext, "Ok", Toast.LENGTH_LONG)
                        }
                        else
                        {
                            Toast.makeText(this.applicationContext, "Error", Toast.LENGTH_LONG)
                        }
                    }*/
        }
    }

    private fun rdbClic()
    {
        if (rdbGoogle.isChecked)
        {
            lytLog.visibility = View.INVISIBLE
        }
        else if(rdbCorreo.isChecked)
        {
            lytLog.visibility = View.VISIBLE
        }
    }

    private fun realizarLogout()
    {

        val account = GoogleSignIn.getLastSignedInAccount(this)
        if (account != null)
        {
            mGoogleSignInClient.signOut()

        }
        else
        {

        }


        updateInterface()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?)
    {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == RC_SIGN_IN)
        {
            Toast.makeText(this.applicationContext, "Login correcto", Toast.LENGTH_LONG)
            updateInterface()
        }
    }
}