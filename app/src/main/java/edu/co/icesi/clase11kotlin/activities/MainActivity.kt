package edu.co.icesi.clase11kotlin.activities

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.EditText
import androidx.appcompat.app.AppCompatActivity
import co.domi.clase10.model.User
import com.google.android.gms.tasks.Task
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.QuerySnapshot
import com.google.firebase.messaging.FirebaseMessaging
import edu.co.icesi.clase11kotlin.R
import kotlinx.android.synthetic.main.activity_main.*
import java.util.*

class MainActivity : AppCompatActivity() {

    private var db = FirebaseFirestore.getInstance()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        loginBtn.setOnClickListener(::login)
    }

    private fun login(view: View) {
        val username = usernameET.text.toString()
        val user = User(UUID.randomUUID().toString(), username, "")
        //Saber si el usuario ya estaba registrado
        val usersRef = db.collection("users")
        val query = usersRef.whereEqualTo("username", username)
        query.get().addOnCompleteListener { task ->
            //Desempaquetamos el result, de aquí en adelante la variable it representa el resultado
            task.result?.let {
                if (task.isSuccessful) {
                    if (it.size() > 0) {
                        for (document in it) {
                            val dbUser = document.toObject(User::class.java)
                            goToUserListActivity(dbUser)
                            break
                        }
                    } else {
                        db.collection("users").document(user.id).set(user)
                        goToUserListActivity(user)
                    }
                }
            }
        }
    }


    fun goToUserListActivity(user: User?) {
        val i = Intent(this, UserListActivity::class.java)
        i.putExtra("myUser", user)
        startActivity(i)
    }
}