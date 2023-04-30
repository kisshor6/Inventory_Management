package com.example.drawa.DrawableItems

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import com.bumptech.glide.Glide
import com.example.drawa.Daos.UserDao
import com.example.drawa.R
import com.example.drawa.Utils.Utils.Companion.userId
import com.example.drawa.databinding.ActivityEditProductBinding

class Edit_Product : AppCompatActivity() {

    private lateinit var binding: ActivityEditProductBinding
    private lateinit var userDao: UserDao
    private lateinit var documentId: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityEditProductBinding.inflate(layoutInflater)
        setContentView(binding.root)

        userDao = UserDao()

        documentId = intent.getStringExtra("productId")!!
        val productName = intent.getStringExtra("productName")
        val productDescription = intent.getStringExtra("productDescription")
        val productQuantity = intent.getStringExtra("productQuantity")
        val productPrice = intent.getStringExtra("productPrice")
        val productImageUrl = intent.getStringExtra("productImageUrl")

        binding.editProductName.setText(productName)
        binding.editDescription.setText(productDescription)
        binding.editQuantity.setText(productQuantity)
        binding.editPrice.setText(productPrice)
        Glide.with(this).load(productImageUrl).into(binding.editProductImage)

        binding.editProductButton.setOnClickListener {
            binding.progressBar.visibility = View.VISIBLE
            val uProductName = binding.editProductName.text.toString().trim()
            val uProductDescription = binding.editDescription.text.toString().trim()
            val uProductQuantity = binding.editQuantity.text.toString().trim()
            val uProductPrice = binding.editPrice.text.toString().trim()

            val updates =  mapOf(
                "productId" to documentId,
                "userId" to userId,
                "productName" to uProductName,
                "productDescription" to uProductDescription,
                "productQuantity" to uProductQuantity,
                "productPrice" to uProductPrice,
                "productImageUrl" to productImageUrl
            )
            userDao.postCollection.child("product").child(documentId)
                .setValue(updates)
                .addOnSuccessListener {
                    binding.progressBar.visibility = View.VISIBLE
                    Toast.makeText(this, "Updated Successfully", Toast.LENGTH_LONG).show()
                    finish()
                }
                .addOnFailureListener {
                    binding.progressBar.visibility = View.VISIBLE
                    Toast.makeText(this, "Failed to Update", Toast.LENGTH_LONG).show()
                    finish()
                }
        }
    }
}