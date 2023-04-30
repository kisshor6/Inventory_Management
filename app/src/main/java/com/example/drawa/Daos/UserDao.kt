package com.example.drawa.Daos

import android.util.Log
import com.example.drawa.modal.UserModal
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
class UserDao {
    private val db = FirebaseDatabase.getInstance().getReference("users")
    val postCollection = FirebaseDatabase.getInstance().getReference()

    fun getUserData(userId : String, callback: (UserModal) -> Unit){
        val userRef = db.child(userId)
        userRef.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val value = snapshot.getValue(UserModal::class.java)
                if (value != null){
                    callback(value)
                }
            }
            override fun onCancelled(error: DatabaseError) {
                Log.w("TAG", "Failed to read value.", error.toException())
            }
        })
    }
}