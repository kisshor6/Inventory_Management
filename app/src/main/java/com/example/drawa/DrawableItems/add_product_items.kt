package com.example.drawa.DrawableItems

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import com.example.drawa.MainActivity
import com.example.drawa.Utils.Utils.Companion.database
import com.example.drawa.Utils.Utils.Companion.storage
import com.example.drawa.Utils.Utils.Companion.userId
import com.example.drawa.databinding.FragmentAddProductItemsBinding
import com.example.drawa.modal.ProductModal
import java.util.*

class add_product_items : Fragment() {

    private var _binding : FragmentAddProductItemsBinding? = null
    private val binding get() = _binding!!
    private lateinit var selectedImage : Uri

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentAddProductItemsBinding.inflate(layoutInflater, container, false)

        binding.selectImageButton.setOnClickListener {
            someActivityResultLauncher.launch("image/*")
        }

        binding.addProductButton.setOnClickListener {
            val productName = binding.productNameEditText.text.toString()
            val productQuantity = binding.quantityEditText.text.toString()
            val productPrice = binding.priceEditText.text.toString()
            val productDesc = binding.descriptionEditText.text.toString()

            if (productName.isNotEmpty() && productQuantity.isNotEmpty() &&
                productPrice.isNotEmpty() && productDesc.isNotEmpty()){

                binding.progressBar.visibility = View.VISIBLE
                setUpProductImage()

            }else{
                Toast.makeText(context, "Input all the fields", Toast.LENGTH_SHORT).show()
            }
        }
        return binding.root
    }

    private fun setUpProductImage() {
        val reference = storage.reference.child("productImg").child(Date().time.toString())
        reference.putFile(selectedImage).addOnCompleteListener{
            if (it.isSuccessful){
                reference.downloadUrl.addOnSuccessListener { task->
                    uploadProductInfo(task.toString())
                }
            }
        }
    }

    private fun uploadProductInfo(imageUrl: String) {
        val postId = database.reference.child("product").push().key
        val productModal = ProductModal(postId.toString(), userId, binding.productNameEditText.text.toString(),
            binding.quantityEditText.text.toString(), binding.priceEditText.text.toString(),
            binding.descriptionEditText.text.toString(), imageUrl)

        database.reference.child("product")
            .child(postId.toString())
            .setValue(productModal)
            .addOnSuccessListener {
                binding.progressBar.visibility = View.GONE
                Toast.makeText(context, "Product Updated successfully", Toast.LENGTH_LONG).show()
                startActivity(Intent(context, MainActivity::class.java))

            }
    }

    private val someActivityResultLauncher = registerForActivityResult(ActivityResultContracts.GetContent()){uri:Uri?->
        if (uri != null){
            selectedImage = uri
            binding.productImageView.setImageURI(uri)
        }
    }
}