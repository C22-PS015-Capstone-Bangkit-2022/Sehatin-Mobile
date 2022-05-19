package com.app.sehatin.ui.activities.main

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import com.app.sehatin.data.model.User
import com.app.sehatin.databinding.ActivityMainBinding
import com.app.sehatin.utils.USER_COLLECTION
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

class MainActivity : AppCompatActivity() {
    private lateinit var binding : ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val userId = FirebaseAuth.getInstance().currentUser?.uid
        userId?.let {
            FirebaseFirestore.getInstance()
                .collection(USER_COLLECTION)
                .document(it)
                .addSnapshotListener { value, error ->
                    Log.d("MainActivity", "User: $value")
                    if(error != null) {
                        return@addSnapshotListener
                    }
                    if(value != null && value.exists()) {
                        val user = value.toObject(User::class.java)
                        User.currentUser = user
                    }
                }
        }
    }
}