package com.example.login

object SingletonLogin
{
    /* Atributos */


    init
    {

    }
    /* Metodos */
    fun isLogged(): Boolean
    {
        return false
    }

    private fun isLoggedGoogle(): Boolean
    {
        return false
    }

    private fun isLoggedMail(): Boolean
    {
        return false
    }

}