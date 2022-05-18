package com.app.sehatin.ui.activities.main.fragments.content.profile

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.app.sehatin.R
import com.app.sehatin.data.model.User
import com.app.sehatin.databinding.FragmentProfileBinding
import com.app.sehatin.ui.activities.start.StartActivity
import com.bumptech.glide.Glide
import com.google.firebase.auth.FirebaseAuth

class ProfileFragment : Fragment() {
    private lateinit var binding: FragmentProfileBinding

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        binding = FragmentProfileBinding.inflate(inflater, container, false)
        initVariable()
        initListener()
        return binding.root
    }

    private fun initVariable() = with(binding) {
        val currentUser = User.currentUser
        currentUser?.let {
            Glide.with(requireContext())
                .load(it.imageUrl)
                .placeholder(R.drawable.user_default)
                .error(R.drawable.user_default)
                .into(userImageIV)
            usernameTv.text = it.username
            userEmailTv.text = it.email
        }
        diagnosisLayout.actionName.text = getString(R.string.diagnosis)
        editProfileLayout.actionName.text = getString(R.string.edit_profile)
        logoutLayout.actionName.text = getString(R.string.log_out)
        postLayout.actionName.text = getString(R.string.my_post)
        savedPostLayout.actionName.text = getString(R.string.saved_post)
    }

    private fun initListener() = with(binding) {
        logoutLayout.root.setOnClickListener {
            User.currentUser = null
            FirebaseAuth.getInstance().signOut()
            startActivity(Intent(requireActivity(), StartActivity::class.java))
            requireActivity().finish()
        }
    }

}