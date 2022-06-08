package com.app.sehatin.ui.activities.main.fragments.content.fragments.health.fragments.exercise

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.app.sehatin.databinding.FragmentExerciseBinding
import com.app.sehatin.ui.activities.main.fragments.content.ContentFragment
import com.google.firebase.auth.FirebaseAuth

class ExerciseFragment : Fragment() {
    private lateinit var binding: FragmentExerciseBinding
    private val viewModel = ContentFragment.viewModel
    private lateinit var token: String

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        binding = FragmentExerciseBinding.inflate(inflater, container, false)
        iniListener()
        return binding.root
    }

    private fun iniListener() {
        val mUser = FirebaseAuth.getInstance().currentUser
        mUser?.getIdToken(true)?.addOnCompleteListener { task ->
            if (task.isSuccessful) {
                val idToken = task.result.token
                idToken?.let {
                    token = it
                    getGoodExercises(it)
                }
            } else {
                Log.e(TAG, "getIdToken: ${task.exception}")
            }
        }
    }
    
    private fun getGoodExercises(token: String) {
        // TODO: GET DATA
        // TODO: CREATE LAYOUT FOR EXERCISE
        // TODO: CREATE ADAPTER
    }
    
    private companion object {
        const val TAG = "ExerciseFragment"
    }

}