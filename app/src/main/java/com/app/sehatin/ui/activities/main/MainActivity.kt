package com.app.sehatin.ui.activities.main

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.lifecycle.lifecycleScope
import androidx.navigation.findNavController
import com.app.sehatin.R
import com.app.sehatin.data.model.User
import com.app.sehatin.databinding.ActivityMainBinding
import com.app.sehatin.ui.dialog.DiseaseAlertDialog
import com.app.sehatin.utils.USER_COLLECTION
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class MainActivity : AppCompatActivity(), DiseaseAlertDialog.OnDiseaseAlertDialogListener{
    private lateinit var binding : ActivityMainBinding
    private var isHadShowDiseasesDialog = false
    private val diseaseDialog = DiseaseAlertDialog(this@MainActivity, this@MainActivity)

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
                        user?.let { data ->
                            showDiseasesDialogOnce(data)
                        }
                    }
                }
        }
    }

    private fun showDiseasesDialogOnce(user: User) = lifecycleScope.launch {
        if(!isHadShowDiseasesDialog) {
            if(user.diseases == null) {
                delay(2000L)
                diseaseDialog.showDialog()
            }
            isHadShowDiseasesDialog = true
        }
    }

    override fun onPositiveClick() {
        diseaseDialog.dismissDialog()
        findNavController(R.id.main_nav).navigate(R.id.action_contentFragment_to_diagnosisFragment)
    }

    override fun onNegativeClick() {
        diseaseDialog.dismissDialog()
    }

}