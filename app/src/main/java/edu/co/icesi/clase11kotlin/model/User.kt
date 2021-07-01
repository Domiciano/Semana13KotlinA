package co.domi.clase10.model

import java.io.Serializable

data class User(
    var id: String,
    var username: String,
    var photoId:String
) : Serializable {

    constructor() : this("NO_ID","NO_USERNAME", "NO_PHOTO")

    override fun toString(): String {
        return username
    }

}