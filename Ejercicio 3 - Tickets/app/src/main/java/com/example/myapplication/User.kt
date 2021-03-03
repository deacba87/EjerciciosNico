package com.example.myapplication

class User() {
    var id: String = ""
        private set
    var email: String = ""
    var name: String = ""
    var password: String = ""
    var profilePicture: String = ""

    companion object
    {
        val HM_NAME = "NAME"
        val HM_PPICTURE = "PPICTURE"
        val HM_NAME_PICTURE = "NAME_PICTURE"
        val HM_FULL = "FULL"
    }

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
            put(BdText.COL_ID, id)
            put(BdText.COL_EMAIL, email)
            put(BdText.COL_PASSWORD, password)
            put(BdText.COL_NAME, name)
            put(BdText.COL_PPICTURE, profilePicture)
        }
        return hashMap
    }

    fun toHashMap2(typeMap: String ): HashMap<String, String>
    {
        val hashMap = HashMap<String, String>()
        with(hashMap)
        {

            when(typeMap)
            {
                HM_NAME -> put(BdText.COL_NAME, name)
                HM_PPICTURE -> put(BdText.COL_PPICTURE, profilePicture)
                HM_NAME_PICTURE ->{
                    put(BdText.COL_NAME, name)
                    put(BdText.COL_PPICTURE, profilePicture)
                }
                else ->
                {
                    put(BdText.COL_ID, id)
                    put(BdText.COL_EMAIL, email)
                    put(BdText.COL_PASSWORD, password)
                    put(BdText.COL_NAME, name)
                    put(BdText.COL_PPICTURE, profilePicture)
                }
            }
        }
        return hashMap
    }

}