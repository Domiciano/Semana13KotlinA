package edu.co.icesi.clase11kotlin.lists.viewmodel

import android.view.View
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import edu.co.icesi.clase11kotlin.R

class UserViewModel(itemView: View) : RecyclerView.ViewHolder(itemView) {

    var actionRow: Button = itemView.findViewById(R.id.actionRow)
    var imageRow: ImageView = itemView.findViewById(R.id.imgRow)
    var nameRow: TextView = itemView.findViewById(R.id.nameRow)

    init {
        //actionRow.setOnClickListener(::)
    }

}