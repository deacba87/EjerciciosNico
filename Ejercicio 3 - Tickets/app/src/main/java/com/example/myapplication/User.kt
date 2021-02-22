package com.example.myapplication

import com.google.firebase.database.FirebaseDatabase

class User() {
    var id: String = ""
        private set
    var email: String = ""
    var name: String = ""
    var password: String = ""
    var profilePicture: String = ""

    constructor(id: String, email: String, password: String ) : this()
    {
        this.id = id
        this.email = email
        this.name = email
        this.password = password
    }

    constructor(id: String, email: String, name: String, password: String, profilePicture: String  ) : this()
    {
        this.id = id
        this.email = email
        this.name = name
        this.password = password
        this.profilePicture = profilePicture
    }

    fun toHashMap(): HashMap<String, String>
    {
        val hashMap = HashMap<String, String>()
        with(hashMap)
        {
            put(BdText.COL_EMAIL, email)
            put(BdText.COL_PASSWORD, password)
            put(BdText.COL_NAME, name)
            put(BdText.COL_PPICTURE, profilePicture)
        }
        return hashMap
    }
}