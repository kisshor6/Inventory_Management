package com.example.drawa.userI

import android.content.Intent
import android.graphics.drawable.GradientDrawable
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.ImageView
import android.widget.Toast
import androidx.core.content.ContextCompat
import com.example.drawa.Daos.UserDao
import com.example.drawa.MainActivity
import com.example.drawa.R
import com.example.drawa.Utils.Utils.Companion.database
import com.example.drawa.Utils.Utils.Companion.storage
import com.example.drawa.databinding.ActivitySetProfileBinding
import com.example.drawa.modal.UserModal
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.storage.FirebaseStorage
import java.util.*

class SetProfile : AppCompatActivity() {

    private lateinit var auth : FirebaseAuth
    private lateinit var selectedImage : Uri
    lateinit var category : String

    private lateinit var binding : ActivitySetProfileBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySetProfileBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setRoundedImage(binding.ProfileImage)

        auth = FirebaseAuth.getInstance()


        binding.ProfileImage.setOnClickListener {
            val intent = Intent()
            intent.action = Intent.ACTION_GET_CONTENT
            intent.type = "image/*"
            startActivityForResult(intent, 1)
        }

        val adapter = ArrayAdapter.createFromResource(this, R.array.category, android.R.layout.simple_spinner_item)
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        binding.spinner.adapter = adapter

        binding.spinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener{
            override fun onItemSelected(p0: AdapterView<*>?, p1: View?, p2: Int, p3: Long) {
                category = p0?.getItemAtPosition(p2) as String
            }

            override fun onNothingSelected(p0: AdapterView<*>?) {
                Toast.makeText(applicationContext, "nothing",  Toast.LENGTH_SHORT).show()
            }
        }

        binding.setupProfile.setOnClickListener {
            if (category == "" && selectedImage == null){
                Toast.makeText(this, "Please Enter Name and select image", Toast.LENGTH_LONG).show()
            }else{
                binding.loading.visibility = View.VISIBLE
                setUpProfile()
            }
        }
    }
    private fun setUpProfile() {
        val reference = storage.reference.child("profile").child(Date().time.toString())
        reference.putFile(selectedImage).addOnCompleteListener{
            if (it.isSuccessful){
                reference.downloadUrl.addOnSuccessListener {task ->
                    uploadInfo(task.toString())
                }
            }
        }
    }

    private fun uploadInfo(imageUrl: String) {
        val user = UserModal(
            auth.uid.toString(),
            binding.userName.text.toString().trim(),
            auth.currentUser!!.email,
            category, imageUrl)

        database.reference.child("users")
            .child(auth.uid.toString())
            .setValue(user)
            .addOnSuccessListener {
                binding.loading.visibility = View.GONE
                Toast.makeText(this, "Profile Updated successfully", Toast.LENGTH_LONG).show()
                startActivity(Intent(this, MainActivity::class.java))
                finish()
            }
    }

    private fun setRoundedImage(profileImage: ImageView) {
        val shape = GradientDrawable()
        shape.shape = GradientDrawable.OVAL
        shape.setColor(ContextCompat.getColor(profileImage.context, R.color.transparent))
        profileImage.background = shape
        profileImage.clipToOutline = true
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (data != null){
            if (data.data != null){
                selectedImage = data.data!!
                binding.ProfileImage.setImageURI(selectedImage)
            }
        }
    }
}