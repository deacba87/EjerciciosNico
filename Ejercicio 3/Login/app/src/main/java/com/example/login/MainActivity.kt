
package com.example.login
/* Notas
1- En algun lado lei que kotlin me debería tomar los elementos del container directamente sin necesidad de usar el findView, pero mis muñones no logran hacerlo andar
2- Se que el Toast solo es mas feo que golpearse el dedo pequeño, pero me diste in dia para hacer esto y en la hora de trabajo estoy haciendo los cursos
3- Pense que la propiedad onClic de boton me iba a permitir no hacer el listener en el onCreate, pero me da error de instalacion.. Todavia no arranque el curso de kotlin en androoid (estoy por el BASICO), asi que no le prestes atencion a esta burrada mia
 */

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.constraintlayout.widget.ConstraintLayout
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.tasks.OnCompleteListener
import com.google.firebase.auth.ActionCodeSettings
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.ktx.Firebase

class MainActivity : AppCompatActivity()
{
    private val RC_SIGN_IN = 1

    private var logVis = View.VISIBLE
    /**/
    private lateinit var btnLogin: Button
    private lateinit var btnLogout: Button
    private lateinit var btnRegistrarse: Button
    private lateinit var rdbGoogle: RadioButton
    private lateinit var rdbCorreo: RadioButton
    private lateinit var lytLog: LinearLayout
    private lateinit var lytOpciones: LinearLayout
    private lateinit var txtUsuario: TextView
    private lateinit var txtPass: TextView
    private lateinit var txtSaludo: TextView
    /**/
    private lateinit var auth: FirebaseAuth
    private lateinit var mGoogleSignInClient: GoogleSignInClient
    /**/
    private lateinit var mailLog: String
    /**/

    override fun onCreate(savedInstanceState: Bundle?)
    {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        /*----------------------------------------------------------------------------------------*/
        btnLogin = findViewById(R.id.btnIngresar)
        btnLogout = findViewById(R.id.btnLogOut)
        btnRegistrarse = findViewById(R.id.btnRegistrarse)
        rdbGoogle = findViewById(R.id.rdbGoogle)
        rdbCorreo = findViewById(R.id.rdbCorreo)
        lytLog = findViewById(R.id.lytLog)
        lytOpciones = findViewById(R.id.lytOpciones)
        txtUsuario = findViewById(R.id.txtUsuario)
        txtPass = findViewById(R.id.txtPass)
        txtSaludo  = findViewById(R.id.txtSaludo)
        /*----------------------------------------------------------------------------------------*/
        btnLogin.setOnClickListener(
                View.OnClickListener { validarLogin() }
        )

        btnLogout.setOnClickListener(
                View.OnClickListener { realizarLogout() }
        )

        btnRegistrarse.setOnClickListener( View.OnClickListener { registrarse() } )
        /*----------------------------------------------------------------------------------------*/
        rdbGoogle.setOnClickListener(View.OnClickListener { rdbClic() })
        rdbCorreo.setOnClickListener(View.OnClickListener { rdbClic() })
        rdbGoogle.isChecked = true
        rdbClic()
        //------------------------------------------------------------------------------------------
        // Configure sign-in to request the user's ID, email address, and basic
        // profile. ID and basic profile are included in DEFAULT_SIGN_IN.
        val gso: GoogleSignInOptions = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .build()
        // Build a GoogleSignInClient with the options specified by gso.
        mGoogleSignInClient = GoogleSignIn.getClient(this, gso)
        // Initialize Firebase Auth
        auth = FirebaseAuth.getInstance()
    }

    private fun registrarse()
    {
        /*auth.createUserWithEmailAndPassword(txtUsuario.text.toString(), txtPass.text.toString())
                .addOnCompleteListener(this)
                {
                    task ->
                    if (task.isSuccessful)
                    {
                        mostrarToast("Registrado exitosamente por correo")
                        updateInterface()
                        System.out.println("dea " + "Registrado exitosamente por correo")
                    }
                    else
                    {
                        mostrarToast("No se pudo Registrar  por correo")
                        System.out.println("dea " + "No se pudo Registrar  por correo")
                    }
                }*/

        var actionCodeSettings = ActionCodeSettings.newBuilder()
                .setUrl("https://loginexampledea.com/")
                .setAndroidPackageName("com.example.login", true, "11")
                .setHandleCodeInApp(true)
                .setDynamicLinkDomain("loginexampledea.page.link")
                .build()
        mailLog = txtUsuario.text.toString()
        auth.sendSignInLinkToEmail(mailLog, actionCodeSettings)
                .addOnCompleteListener { task ->
                    if (task.isSuccessful)
                    {
                        System.out.println("deacba87 task.isSuccessful")
                    }
                    else
                    {
                        System.out.println("deacba87 task NO Successful")
                        System.out.println("deacba87 " + task.exception.toString())
                        System.out.println("deacba87 " + task.result.toString())
                    }
                }
    }

    override fun onStart()
    {
        super.onStart()
        updateInterface()
        revisarLink()
    }
    private fun revisarLink()
    {
        val auth = this.auth
        val intent = intent
        val emailLink = intent.data.toString()

        // Confirm the link is a sign-in with email link.
        if (auth.isSignInWithEmailLink(emailLink))
        {
            // Retrieve this from wherever you stored it
            val email = mailLog

            // The client SDK will parse the code from the link for you.
            auth.signInWithEmailLink(email, emailLink)
                    .addOnCompleteListener { task ->
                        if (task.isSuccessful) {
                            Log.d("deacba87", "Successfully signed in with email link!")
                            System.out.println("deacba87 - Successfully signed in with email link!")
                            val result = task.result
                            System.out.println("deacba87 - resulte: " + result.toString() )
                            System.out.println("deacba87 - resulte.user: " + result?.user?.toString() )
                            Log.d("deacba87", "resulte: " + result.toString())
                            Log.d("deacba87", "user: " + result?.user?.toString())
                            // You can access the new user via result.getUser()
                            // Additional user info profile *not* available via:
                            // result.getAdditionalUserInfo().getProfile() == null
                            // You can check if the user is new or existing:
                            // result.getAdditionalUserInfo().isNewUser()
                        }
                        else
                        {
                            Log.e("deacba87",  "Error signing in with email link " + task.exception.toString()  , task.exception)
                            System.out.println("deacba87 " + "Error signing in with email link " + task.exception.toString())
                        }
                    }
        }
    }

    private fun isLogged(): Pair<Boolean, String>
    {
        val account = GoogleSignIn.getLastSignedInAccount(this)
        val user = auth.currentUser
        var datos: String = ""
        val logueado: Boolean = account != null || user != null
        if (logueado)
        {
            if (account != null)
            {
                datos = account.displayName.toString() + " - by Google"
            }
            else if (user != null)
            {
                datos = user.email.toString() + " - by Correo"
            }
        }

        return Pair(logueado, datos)
    }

    private fun updateInterface()
    {
        val (logueado, datos) = isLogged()

        if ( !logueado )
        {
            btnLogout.visibility = View.GONE
            btnLogin.visibility = View.VISIBLE
            txtSaludo.text = "Usted no se encuentra logueado"
            lytOpciones.visibility = View.VISIBLE
        }
        else
        {
            btnLogout.visibility = View.VISIBLE
            btnLogin.visibility = View.GONE
            txtSaludo.text = "Bienvenido " + datos
            lytOpciones.visibility = View.GONE
            lytLog.visibility = View.GONE
            btnRegistrarse.visibility = View.GONE
        }
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
        else if(rdbCorreo.isChecked) {
            /*auth.signInWithEmailAndPassword(txtUsuario.text.toString(), txtPass.text.toString())
                    .addOnCompleteListener(this)
                    { task ->
                        if (task.isSuccessful)
                        {
                            updateInterface()
                            mostrarToast("Logueado exitosamente mediante correo")
                            txtPass.text = ""
                            txtUsuario.text = ""
                        }
                        else
                        {
                            mostrarToast("No se pudo loguear mediante correo")

                        }
                    }*/

        }
    }

    private fun mostrarToast(mensaje: String)
    {
        Toast.makeText(this, mensaje, Toast.LENGTH_LONG).show()
    }

    private fun rdbClic()
    {
        if (rdbGoogle.isChecked)
        {
            lytLog.visibility = View.GONE
            btnRegistrarse.visibility = View.GONE
        }
        else if(rdbCorreo.isChecked)
        {
            lytLog.visibility = View.VISIBLE
            btnRegistrarse.visibility = View.VISIBLE
        }
    }

    private fun realizarLogout()
    {
        val account = GoogleSignIn.getLastSignedInAccount(this)
        val user = auth.currentUser
        if (account != null)
        {
            mGoogleSignInClient.signOut()
            mostrarToast("Deslogueado de la cuenta de Google")
        }
        else if(user != null)
        {
            auth.signOut()
            mostrarToast("Deslogueado de la cuenta de Correo")
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

        revisarLink()
    }

    override fun onResume() {
        super.onResume()
        revisarLink()
    }
}


