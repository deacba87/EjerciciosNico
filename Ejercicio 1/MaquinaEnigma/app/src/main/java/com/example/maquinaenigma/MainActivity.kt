package com.example.maquinaenigma

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import org.w3c.dom.Text

class MainActivity : AppCompatActivity()
{
    private lateinit var map: Map<Char, Char>

    override fun onCreate(savedInstanceState: Bundle?)
    {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        map = init_map()

        val btnConverir = this.findViewById(R.id.btnConvertir) as Button
        btnConverir.setOnClickListener{
            val txtEntrada = this.findViewById(R.id.txtEntrada) as EditText
            val txtSalida = this.findViewById(R.id.txtSalida) as TextView
            //val texto: String = convert(txtEntrada.text.toString())
            txtSalida.text = convert(txtEntrada.text.toString())

        }
    }

    fun convert(pEntrada: String): String
    {
        var salida: String = ""
        val map = init_map()

        val iterator = pEntrada.iterator()
        var key: Char
        var new: Char

        while (iterator.hasNext())
        {
            //key = iterator.next()
            new = convertChar(iterator.next(), map)
            salida = salida + new
        }

        return salida
    }

    fun convertChar(pKey: Char, pMap: Map<Char, Char> ): Char
    {
        var new: Char
        var aux = pKey.toLowerCase()
        try
        {
            new = pMap[aux]!!
        }
        catch (np: KotlinNullPointerException)
        {
            new = pKey
        }

        if (pKey.isUpperCase())
        {
            new = new.toUpperCase()
        }
        return new

    }

    fun init_map(): Map<Char, Char>
    {
        val map = mapOf( 'a' to 'v',
                'b' to 'b',
                'c' to 'k',
                'd' to 'g',
                'e' to 'p',
                'f' to 'j',
                'g' to 'o',
                'h' to 'e',
                'i' to 's',
                'j' to 'r',
                'k' to 'c',
                'l' to 'n',
                'm' to 'z',
                'n' to 'h',
                'o' to 'l',
                'p' to 'i',
                'q' to 't',
                'r' to 'u',
                's' to 'w',
                't' to 'd',
                'u' to 'q',
                'v' to 'f',
                'w' to 'a',
                'x' to 'm',
                'y' to 'y',
                'z' to 'x' )

        return map
    }
}