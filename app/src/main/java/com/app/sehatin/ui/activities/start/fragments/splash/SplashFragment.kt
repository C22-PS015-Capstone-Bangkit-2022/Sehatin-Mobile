package com.app.sehatin.ui.activities.start.fragments.splash

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.app.sehatin.R
import com.app.sehatin.data.model.User
import com.app.sehatin.data.repository.USER_COLLECTION
import com.app.sehatin.databinding.FragmentSplashBinding
import com.app.sehatin.ui.activities.main.MainActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch


class SplashFragment : Fragment() {
    private lateinit var binding: FragmentSplashBinding

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        // Inflate the layout for this fragment
        binding = FragmentSplashBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        lifecycleScope.launch {
            delay(1500)
            val currentUser = FirebaseAuth.getInstance().currentUser
            if(currentUser == null) {
                findNavController().navigate(R.id.action_splashFragment_to_loginFragment)
            } else {
                loadUser(currentUser)
            }
        }
    }

    private fun loadUser(currentUser: FirebaseUser) {
        FirebaseFirestore.getInstance().collection(USER_COLLECTION)
            .whereEqualTo(User.ID, currentUser.uid)
            .get()
            .addOnSuccessListener { documents ->
                for(document in documents) {
                    User.currentUser = document.toObject(User::class.java)
                    Log.d(TAG, "loadUser: ${User.currentUser}")
                    startActivity(Intent(requireActivity(), MainActivity::class.java))
                    requireActivity().finish()
                    return@addOnSuccessListener
                }
            }
    }


    private companion object {
        const val TAG = "SplashFragment"
    }
}