package com.example.drawa.userI

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.example.drawa.MainActivity
import com.example.drawa.databinding.ActivityLoginBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase

class Login : AppCompatActivity() {
    private lateinit var binding: ActivityLoginBinding
    private lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        auth = Firebase.auth

       binding.redirectSignUp.setOnClickListener {
           startActivity(Intent(this, SignUp::class.java))
       }

        binding.loginUser.setOnClickListener {
            val email = binding.username.text.toString().trim()
            val password = binding.password.text.toString().trim()

            if (email.isNotEmpty() && password.isNotEmpty()){
                login(email, password)
            }else{
                Toast.makeText(this, "Please Input email and password", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun login(email: String, password: String) {
        auth.signInWithEmailAndPassword(email, password)
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    // Sign in success, update UI with the signed-in user's information
                    Toast.makeText(baseContext, "Login Successful !", Toast.LENGTH_SHORT).show()
                    startActivity(Intent(this, MainActivity::class.java))
                    finish()
                } else {
                    Toast.makeText(baseContext, "Invalid Password", Toast.LENGTH_SHORT).show()
                }
            }
    }

    override fun onStart() {
        super.onStart()
        val currentUser = auth.currentUser
        if (currentUser != null){
            startActivity(Intent(this, MainActivity::class.java))
            finish()
        }
    }
}