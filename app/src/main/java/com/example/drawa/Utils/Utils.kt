package com.example.drawa.Utils

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.storage.FirebaseStorage

class Utils {
    companion object{
        val userId = FirebaseAuth.getInstance().currentUser?.uid.toString()
        var storage = FirebaseStorage.getInstance()
        var database = FirebaseDatabase.getInstance()
    }
}