package com.example.login
/* Notas
1- En algun lado lei que kotlin me debería tomar los elementos del container directamente sin necesidad de usar el findView, pero mis muñones no logran hacerlo andar
2- Se que el Toast solo es mas feo que golpearse el dedo pequeño, pero me diste in dia para hacer esto y en la hora de trabajo estoy haciendo los cursos
3- Pense que la propiedad onClic de boton me iba a permitir no hacer el listener en el onCreate, pero me da error de instalacion.. Todavia no arranque el curso de kotlin en androoid (estoy por el BASICO), asi que no le prestes atencion a esta burrada mia
 */

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.TextView
import android.widget.Toast

class MainActivity : AppCompatActivity()
{
    override fun onCreate(savedInstanceState: Bundle?)
    {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        var btnLogin: Button = findViewById(R.id.btnIngresar)
        btnLogin.setOnClickListener(
            View.OnClickListener { validarLogin() }
        )
    }

    public fun login(view: View)
    {
        validarLogin()

    }

    private fun validarLogin()
    {
        var oValidacion: ValidacionLogin = ValidacionLogin()
        var usuario: TextView = findViewById(R.id.txtUsuario)
        var pass: TextView = findViewById(R.id.txtPass)
        val toast: Toast

        val (a, b) = oValidacion.validarIntegridad(usuario.text.toString() , pass.text.toString())
        Toast.makeText(this.applicationContext, b.toString(), Toast.LENGTH_LONG).show()
        /*if (a == true)
        {
            Toast.makeText(this.applicationContext, b.toString(), Toast.LENGTH_LONG)
        }
        else
        {
            Toast.makeText(this.applicationContext, b.toString(), Toast.LENGTH_LONG)
        }*/


    }
}