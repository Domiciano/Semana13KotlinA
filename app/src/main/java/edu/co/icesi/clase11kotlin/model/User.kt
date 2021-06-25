package co.domi.clase10.model

import java.io.Serializable

data class User(
    val id: String,
    val username: String
) : Serializable {

    constructor() : this("NO_ID","NO_USERNAME")

    override fun toString(): String {
        return username
    }

}