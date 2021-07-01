package edu.co.icesi.clase11kotlin.activities

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.AdapterView
import android.widget.AdapterView.OnItemClickListener
import android.widget.ArrayAdapter
import android.widget.LinearLayout
import android.widget.ListView
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager

import co.domi.clase10.model.User
import com.google.android.gms.tasks.Task
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.QuerySnapshot
import com.google.firebase.ktx.Firebase
import com.google.firebase.messaging.FirebaseMessaging
import edu.co.icesi.clase11kotlin.R
import edu.co.icesi.clase11kotlin.lists.adapter.UserAdapter
import kotlinx.android.synthetic.main.activity_user_list.*
import java.util.*

class UserListActivity : AppCompatActivity(), UserAdapter.OnUserClickListener {

    private var db = FirebaseFirestore.getInstance()

    private lateinit var myUser: User

    private var isLoggingout: Boolean = false

    private lateinit var adapter: UserAdapter


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_user_list)

        //Configuracion de la lista

        adapter = UserAdapter()
        adapter.listener = this
        userList.adapter = adapter
        userList.setHasFixedSize(true)
        val manager = LinearLayoutManager(this)
        userList.layoutManager = manager

        //Recuperar el user de la actividad pasada
        myUser = intent.extras?.getSerializable("myUser") as User

        //Listar usuarios
        db = FirebaseFirestore.getInstance()
        val userReference = db.collection("users").orderBy("username").limit(10)
        userReference.get().addOnCompleteListener { task: Task<QuerySnapshot> ->
            task.result?.let {
                adapter.clear()
                for (doc in task.result!!) {
                    val user = doc.toObject(User::class.java)
                    adapter.addUser(user)
                }
            }
        }


        logoutBtn.setOnClickListener(::logout)

        profileBtn.setOnClickListener(::goToProfile)

    }

    private fun goToProfile(view: View?) {
        val intent = Intent(this, ProfileActivity::class.java)
        intent.putExtra("myUser", this.myUser)
        startActivity(intent)
    }

    private fun logout(view: View?) {
        isLoggingout = true
        finish()
    }



    override fun onResume() {
        FirebaseMessaging.getInstance().unsubscribeFromTopic(myUser.username)
        super.onResume()

    }

    override fun onPause() {
        if (isLoggingout) {
            FirebaseMessaging.getInstance().unsubscribeFromTopic(myUser.username)
        } else {
            FirebaseMessaging.getInstance().subscribeToTopic(myUser.username)
        }
        super.onPause()
    }

    override fun onUserClick(userClicked: User) {
        val intent = Intent(this, ChatActivity::class.java)
        intent.putExtra("myUser", myUser)
        intent.putExtra("userClicked", userClicked)
        startActivity(intent)
    }

}