package edu.co.icesi.clase11kotlin.activities

import android.Manifest
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.activity.result.ActivityResult
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts.StartActivityForResult
import co.domi.clase10.model.User
import com.bumptech.glide.Glide
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.ktx.storage
import edu.co.icesi.clase11kotlin.R
import edu.co.icesi.semana4kotlina.UtilDomi
import kotlinx.android.synthetic.main.activity_profile.*
import java.io.File
import java.io.FileInputStream
import java.util.*

class ProfileActivity : AppCompatActivity() {

    private lateinit var galleryLauncher: ActivityResultLauncher<Intent>
    private lateinit var myUser: User

    private var path: String? = null

    //Esta es otra forma de llamar a los componentes de Firebase
    private val storage = Firebase.storage
    private val db = Firebase.firestore

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_profile)

        galleryLauncher = registerForActivityResult(StartActivityForResult(), ::onGalleryResult)

        requestPermissions(
            arrayOf(
                Manifest.permission.WRITE_EXTERNAL_STORAGE,
                Manifest.permission.READ_EXTERNAL_STORAGE
            ), 1
        )

        myUser = intent.extras?.getSerializable("myUser") as User

        editProfileBtn.setOnClickListener(::editProfile)
        saveProfileBtn.setOnClickListener(::saveProfile)

        loadPhoto()
    }

    private fun loadPhoto() {
        db.collection("users").document(myUser.id).get().addOnCompleteListener {
            val user = it.result?.toObject(User::class.java)
            user?.let {
                myUser = it
                if(!myUser.photoId.isEmpty()){
                    storage.reference.child("profiles").child(myUser.photoId).downloadUrl.addOnCompleteListener {
                        val url = it.result.toString()
                        Glide.with(imageProfile).load(url).into(imageProfile)
                    }
                }
            }
        }
    }

    private fun editProfile(view: View?) {
        val i = Intent(Intent.ACTION_PICK)
        i.setType("image/*")
        galleryLauncher.launch(i)
    }

    private fun saveProfile(view: View?) {
        path?.let {
            val name = UUID.randomUUID().toString()
            val fis = FileInputStream(File(it))
            storage.reference.child("profiles").child(name).putStream(fis).addOnCompleteListener {
                if (it.isSuccessful){
                    myUser.photoId = name
                    db.collection("users").document(myUser.id).set(myUser)
                }
            }
        }
    }


    private fun onGalleryResult(activityResult: ActivityResult?) {
        if (activityResult?.resultCode == RESULT_OK) {
            val photoUri = activityResult.data?.data
            photoUri?.let {
                path = UtilDomi.getPath(this, it)
                val bitmap = BitmapFactory.decodeFile(path)
                imageProfile.setImageBitmap(bitmap)
            }
        }
    }

}