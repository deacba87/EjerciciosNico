package com.example.login

import android.text.BoringLayout

/*Notas:
1- Si, se que pude haber definido los caracteres especiales usando "\'" "Ä" etc... pero me odio mucho aparentemente, y en un lugar lei de usar el codigo html
2- Se que no es necesario los {} cuando el if es de una linea, pero... caprichos
3- La funcion correcta es la que se va a encargar de validar si el pass y usuario son incorrectos
*/
class ValidacionLogin
{
    private var lstCaracteres: ArrayList<String> = initCaracteres()

    private fun initCaracteres(): ArrayList<String>
    {
        //Comillas simples ni dobles, coma, punto y coma, diéresis, tildes, caracteres de escapes

        val comillaSimple: String = "\u0027"
        val comillaDoble: String = "\u0022"
        val coma: String = "\u002C"
        val puntoComa: String = "\u003B"
        val dieresisMayusA: String = "\u00C4"
        val dieresisMinusA: String = "\u00E4"
        val dieresisMayusE: String = "\u00CB"
        val dieresisMinusE: String = "\u00EB"
        val dieresisMayusI: String = "\u00CF"
        val dieresisMinusI: String = "\u00EF"
        val dieresisMayusO: String = "\u00D6"
        val dieresisMinusO: String = "\u00F6"
        val dieresisMayusU: String = "\u00D6"
        val dieresisMinusU: String = "\u00F6"
        val tildeMayusA: String = "\u00C1"
        val tildeMinusA: String = "\u00E1"
        val tildeMayusE: String = "\u00C9"
        val tildeinusE: String = "\u00E9"
        val tildeMayusI: String = "\u00CD"
        val tildeMinusI: String = "\u00ED"
        val tildeMayusO: String = "\u00D3"
        val tildeMinusO: String = "\u00F3"
        val tildeMayusU: String = "\u00DA"
        val tildeMinusU: String = "\u00FA"
        val tab: String = "\t"
        val backspace: String = "\b"
        val newline: String = "\n"
        val carriage: String = "\r"

        var lstCaract: ArrayList<String> = ArrayList()

        lstCaract.add(comillaSimple)
        lstCaract.add(comillaDoble)
        lstCaract.add(coma)
        lstCaract.add(puntoComa)
        lstCaract.add(dieresisMayusA)
        lstCaract.add(dieresisMinusA)
        lstCaract.add(dieresisMayusE)
        lstCaract.add(dieresisMinusE)
        lstCaract.add(dieresisMayusI)
        lstCaract.add(dieresisMinusI)
        lstCaract.add(dieresisMayusO)
        lstCaract.add(dieresisMinusO)
        lstCaract.add(dieresisMayusU)
        lstCaract.add(dieresisMinusU)
        lstCaract.add(tildeMayusA)
        lstCaract.add(tildeMinusA)
        lstCaract.add(tildeMayusE)
        lstCaract.add(tildeinusE)
        lstCaract.add(tildeMayusI)
        lstCaract.add(tildeMinusI)
        lstCaract.add(tildeMayusO)
        lstCaract.add(tildeMinusO)
        lstCaract.add(tildeMayusU)
        lstCaract.add(tildeMinusU)
        lstCaract.add(tab)
        lstCaract.add(backspace)
        lstCaract.add(newline)
        lstCaract.add(carriage)

        return lstCaract
    }

    public fun validarIntegridad(iUser: String, iPass: String):Pair<Boolean, String>
    {
        var msgError: String = "";
        var user: String = "";
        var pass: String = "";

        // Usuario y Password no deben estar vacios
        if (isNull(iUser))
        {
            return Pair(false, "Usuario no puede ser vacio")
        }

        if (isNull(iPass))
        {
            return Pair(false, "Contraseña no puede ser vacio")
        }

        //Usuario no puede tener los siguientes caracteres : Comillas simples ni dobles, coma, punto y coma, diéresis, tildes, caracteres de escapes.
        if (haveSpecialChar(iUser))
        {
            return Pair(false, "Usuario no puede contener caracteres especiales")
        }

        //El usuario debe validar un mínimo de 8 caracteres
        if (iUser.length < 8)
        {
            return Pair(false, "Usuario debe tener más de 8(ocho) caracteres")
        }

        //El password tiene las mismas restricciones pensionadas para usuario
        if (iPass.length < 8)
        {
            return Pair(false, "Contraseña debe tener más de 8(ocho) caracteres")
        }

        if (haveSpecialChar(iUser))
        {
            return Pair(false, "Usuario no puede contener caracteres especiales")
        }

        //El password debe validar un mínimo de 8 caracteres, 1 mayúscula, 1 minúscula, 1 numero y algún carácter especial que no este contemplado en la lista de restricciones.
        if (!securityPass(iPass))
        {
            return Pair(false, "La contraseña debe contener mayuscula, minuscula, numero, caracter especial, sangre de una virgen, una cancion de los 80 o 90" )
        }


        if (correcta(iUser, iPass))
        {
            return Pair(true, "Login exitoso")
        }
        else
        {
            return Pair(false, "Usuario y pass incorrectos")
        }

    }

    private fun correcta(iUser: String, iPass: String): Boolean
    {
        return true
    }

    private fun isNull(iTexto: String):Boolean
    {
        var texto: String
        texto = iTexto.replace("\\s".toRegex(), "")
        if (texto.isEmpty()) {
            return true
        }
        return false
    }

    private fun haveSpecialChar(iTexto: String): Boolean
    {
        var ch:String = ""
        for (c in iTexto)
        {
            ch = c.toString()

            for (item in  lstCaracteres)
            {
                if (item.equals(ch))
                {
                    return true
                }
            }

        }
        return false
    }

    private fun securityPass(iTexto: String):Boolean
    {
        //El password debe validar un mínimo de 8 caracteres, 1 mayúscula, 1 minúscula, 1 numero y algún carácter especial que no este contemplado en la lista de restricciones.
        var haveMayus: Boolean = false
        var haveMinus: Boolean = false
        var haveNumber: Boolean = false
        var haveSpecial: Boolean = false

        var ch: String = ""
        for (c in iTexto)
        {
            if ( c.isLetter() &&
                ( haveMayus == false || haveMinus == false ) )
            {
                if (c.isUpperCase())
                {
                    haveMayus = true
                }
                else
                {
                    haveMinus = true
                }
            }
            else if ( c.isDigit() && haveNumber == false)
            {
                haveNumber = true
            }
            else if (haveSpecial == false)
            {
                if (!lstCaracteres.contains(c.toString()))
                {
                    haveSpecial = true
                }
            }

        }

        if (haveMayus && haveMayus && haveNumber && haveSpecial )
        {
            return true
        }
        else
        {
            return false
        }


    }
}

