
package com.example.login
/* Notas
1- En algun lado lei que kotlin me debería tomar los elementos del container directamente sin necesidad de usar el findView, pero mis muñones no logran hacerlo andar
2- Se que el Toast solo es mas feo que golpearse el dedo pequeño, pero me diste in dia para hacer esto y en la hora de trabajo estoy haciendo los cursos
3- Pense que la propiedad onClic de boton me iba a permitir no hacer el listener en el onCreate, pero me da error de instalacion.. Todavia no arranque el curso de kotlin en androoid (estoy por el BASICO), asi que no le prestes atencion a esta burrada mia
 */

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions


class MainActivity : AppCompatActivity()
{
    private val RC_SIGN_IN = 1
    private lateinit var mGoogleSignInClient: GoogleSignInClient
    override fun onCreate(savedInstanceState: Bundle?)
    {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val btnLogin: Button = findViewById(R.id.btnIngresar)
        btnLogin.setOnClickListener(
                View.OnClickListener { validarLogin() }
        )


        // Configure sign-in to request the user's ID, email address, and basic
        // profile. ID and basic profile are included in DEFAULT_SIGN_IN.
        val gso: GoogleSignInOptions = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .build()

        // Build a GoogleSignInClient with the options specified by gso.
        mGoogleSignInClient = GoogleSignIn.getClient(this, gso)

    }

    override fun onStart()
    {
        super.onStart()

        // Check for existing Google Sign In account, if the user is already signed in
        // the GoogleSignInAccount will be non-null.
        val account = GoogleSignIn.getLastSignedInAccount(this)
        var text: String
        if (account == null)
        {
            text = "No logueado"
        }
        else
        {
            text = "Logueado"
        }
        Toast.makeText(this.applicationContext, text, Toast.LENGTH_LONG).show()
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
        val toast: Toast

        val (a, b) = oValidacion.validarIntegridad(usuario.text.toString(), pass.text.toString())

        if (a == true)
        {
            val signInIntent = mGoogleSignInClient.signInIntent
            startActivityForResult(signInIntent, RC_SIGN_IN)
        }
        else
        {
            Toast.makeText(this.applicationContext, b.toString(), Toast.LENGTH_LONG)
        }


    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?)
    {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == RC_SIGN_IN)
        {
            Toast.makeText(this.applicationContext, "Login correcto", Toast.LENGTH_LONG)
        }
    }
}