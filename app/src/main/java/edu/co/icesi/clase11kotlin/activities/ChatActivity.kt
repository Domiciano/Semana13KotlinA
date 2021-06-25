package edu.co.icesi.clase11kotlin.activities

import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import edu.co.icesi.clase11kotlin.R
import co.domi.clase10.model.ChatRelationship
import co.domi.clase10.model.Message
import co.domi.clase10.model.User
import com.google.android.gms.tasks.Task
import com.google.firebase.firestore.*
import kotlinx.android.synthetic.main.activity_chat.*
import java.util.*

class ChatActivity : AppCompatActivity(){

    private var db = FirebaseFirestore.getInstance()

    private lateinit var listener: ListenerRegistration
    private lateinit var myUser: User
    private lateinit var userClicked: User
    private lateinit var currentChat: ChatRelationship


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_chat)

        myUser = intent.extras?.getSerializable("myUser") as User
        userClicked = intent.extras?.getSerializable("userClicked") as User

        sendBtn.setOnClickListener(::sendMessage)
        Toast.makeText(
            this,
            "Usted esta en la conversacion de ${myUser.username} con ${userClicked.username}",
            Toast.LENGTH_LONG
        ).show()
        resolveCurrentChat()
    }

    private fun resolveCurrentChat() {
        val userRef = db.collection("users").document(myUser.id).collection("chats")
        val query = userRef.whereEqualTo("contactID", userClicked.id)

        query.get().addOnCompleteListener { task ->
            if (task.isSuccessful) {
                task.result?.let { saferesult ->
                    if (saferesult.size() > 0) {
                        for (doc in saferesult) {
                            currentChat = doc.toObject(ChatRelationship::class.java)
                        }
                    } else {
                        //Primera vez
                        val chatUUID = UUID.randomUUID().toString()
                        val rel1 = ChatRelationship(chatUUID, userClicked.id, userClicked.username)
                        val rel2 = ChatRelationship(chatUUID, myUser.id, myUser.username)
                        db.collection("users").document(myUser.id).collection("chats").document(chatUUID).set(rel1)
                        db.collection("users").document(userClicked.id).collection("chats").document(chatUUID).set(rel2)
                        currentChat = rel1
                    }
                    suscribeToMessages()
                }

            }
        }
    }

    private fun suscribeToMessages() {
        val messagesRef = db.collection("chats").document(currentChat.chatID).collection("messages")
        val query = messagesRef.orderBy("timestamp", Query.Direction.ASCENDING).limit(20)
        listener = query.addSnapshotListener { data, error ->
            data?.let { datasafe ->
                messagesTV.text = ""
                for (doc in datasafe.documents) {
                    val m = doc.toObject(Message::class.java)
                    messagesTV!!.append("${m?.body}\n")
                }
            }

        }
    }

    private fun sendMessage(view: View) {
        val msg = msgET.text.toString()
        val message = Message(UUID.randomUUID().toString(), msg, myUser.id, Date().time)
        db.collection("chats").document(currentChat.chatID).collection("messages")
            .document(message.id).set(message)
    }

    override fun onDestroy() {
        listener.remove()
        super.onDestroy()
    }




}