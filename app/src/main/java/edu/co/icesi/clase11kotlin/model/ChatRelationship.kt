package co.domi.clase10.model

data class ChatRelationship(
    val chatID: String,
    //Metadata
    val contactID: String,
    val contactName: String
){
    constructor() : this("NO_ID","NO_ID","NO_CONTACT_NAME")
}