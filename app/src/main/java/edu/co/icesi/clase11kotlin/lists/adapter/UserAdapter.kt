package edu.co.icesi.clase11kotlin.lists.adapter

import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import co.domi.clase10.model.User
import com.bumptech.glide.Glide
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.ktx.storage
import edu.co.icesi.clase11kotlin.R
import edu.co.icesi.clase11kotlin.lists.viewmodel.UserViewModel

class UserAdapter : RecyclerView.Adapter<UserViewModel>() {


    private val users = ArrayList<User>()
    private val storage = Firebase.storage
    var listener: OnUserClickListener? = null

    fun addUser(user: User) {
        users.add(user)
        notifyItemInserted(users.size - 1)
    }

    fun clear() {
        users.clear()
        notifyItemInserted(users.size - 1)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): UserViewModel {
        val inflater = LayoutInflater.from(parent.context)
        val row = inflater.inflate(R.layout.userrow, parent, false)
        val contactview = UserViewModel(row)
        return contactview
    }

    override fun onBindViewHolder(holder: UserViewModel, position: Int) {
        val user = users[position]
        holder.nameRow.text = user.username
        holder.actionRow.setOnClickListener {
            listener?.onUserClick(user)
        }

        if (!user.photoId.isEmpty()) {
            Log.e(">>>",user.photoId)
            storage.reference.child("profiles").child(user.photoId).downloadUrl.addOnCompleteListener {
                val url = it.result.toString()
                Glide.with(holder.imageRow).load(url).into(holder.imageRow)
            }
        }
    }

    override fun getItemCount(): Int {
        return users.size
    }


    interface OnUserClickListener {
        fun onUserClick(user: User)
    }

}