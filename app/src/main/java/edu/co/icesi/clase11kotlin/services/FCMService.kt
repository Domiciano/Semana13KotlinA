package edu.co.icesi.clase11kotlin.services

import android.content.Intent
import android.util.Log
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage
import com.google.gson.Gson
import edu.co.icesi.clase11kotlin.activities.MainActivity
import edu.co.icesi.clase11kotlin.model.FCMMessage
import edu.co.icesi.clase11kotlin.util.NotificationUtil
import org.json.JSONObject

//Firebase cloud messaging
class FCMService : FirebaseMessagingService() {
    override fun onMessageReceived(remoteMessage: RemoteMessage) {

        //Esto es para arreglar el formato del String
        val objecto = JSONObject(remoteMessage.data as Map<*, *>)
        val json = objecto.toString()

        val message = Gson().fromJson(json, FCMMessage::class.java)
        NotificationUtil().createNotification(
            this,
            "Nuevo mensaje",
            message.message,
            Intent(this, MainActivity::class.java)
        )
    }
}