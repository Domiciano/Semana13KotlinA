package co.domi.clase10.model

data class Message(
    val id: String,
    val body: String,
    val authorID: String,
    val timestamp: Long
){
    constructor():this("NO_ID","NO_BODY","NO_AUTHOR",0L)
}