package com.app.sehatin.ui.activities.main.fragments.content.profile

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.app.sehatin.R
import com.app.sehatin.data.Result
import com.app.sehatin.data.model.User
import com.app.sehatin.databinding.FragmentProfileBinding
import com.app.sehatin.ui.activities.main.fragments.content.ContentFragmentDirections
import com.app.sehatin.ui.activities.start.StartActivity
import com.app.sehatin.ui.viewmodel.ViewModelFactory
import com.bumptech.glide.Glide
import com.google.firebase.auth.FirebaseAuth

class ProfileFragment : Fragment() {
    private lateinit var binding: FragmentProfileBinding
    private lateinit var viewModel: ProfileViewModel

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        Log.d("ProfileFragment", "onCreateView: ")
        binding = FragmentProfileBinding.inflate(inflater, container, false)
        initVariable()
        initListener()
        return binding.root
    }

    private fun initVariable() = with(binding) {
        viewModel = ViewModelProvider(this@ProfileFragment, ViewModelFactory.getInstance())[ProfileViewModel::class.java]
        val currentUser = User.currentUser
        currentUser?.let {
            Glide.with(requireContext())
                .load(it.imageUrl)
                .placeholder(R.drawable.user_default)
                .error(R.drawable.user_default)
                .into(userImageIV)
            usernameTv.text = it.username
            userEmailTv.text = it.email
            if(it.diseases == null) {
                diagnosisLayout.warningInfo.visibility = View.VISIBLE
                diagnosisLayout.warningInfo.text = getString(R.string.please_complete_your_diagnosis)
            } else {
                diagnosisLayout.warningInfo.visibility = View.GONE
            }
        }
        diagnosisLayout.actionName.text = getString(R.string.diagnosis)
        editProfileLayout.actionName.text = getString(R.string.edit_profile)
        logoutLayout.actionName.text = getString(R.string.log_out)
        postLayout.actionName.text = getString(R.string.my_post)
        postLayout.counterText.visibility = View.VISIBLE
        savedPostLayout.actionName.text = getString(R.string.saved_post)
        savedPostLayout.counterText.visibility = View.VISIBLE
        savedPostLayout.counterText.text = "0"
        settingLayout.actionName.text = getString(R.string.setting)
        feedbackLayout.actionName.text = getString(R.string.share_feedback)

        val userId = User.currentUser?.id
        userId?.let {
            viewModel.getUserPost(it)
        }
    }

    private fun initListener() = with(binding) {
        diagnosisLayout.root.setOnClickListener {
            val diseases = User.currentUser?.diseases
            if(diseases != null && diseases.isNotEmpty()) {
                findNavController().navigate(R.id.action_contentFragment_to_userDiseasesFragment)
            } else {
                findNavController().navigate(R.id.action_contentFragment_to_diagnosisFragment)
            }
        }
        editProfileLayout.root.setOnClickListener {
            User.currentUser?.let {
                val direction = ContentFragmentDirections.actionContentFragmentToEditProfileFragment(it)
                findNavController().navigate(direction)
            }
        }
        logoutLayout.root.setOnClickListener {
            User.currentUser = null
            FirebaseAuth.getInstance().signOut()
            startActivity(Intent(requireActivity(), StartActivity::class.java))
            requireActivity().finish()
        }
        postLayout.root.setOnClickListener {
            findNavController().navigate(R.id.action_contentFragment_to_userPostFragment)
        }
        savedPostLayout.root.setOnClickListener {
            Toast.makeText(requireContext(), "Saved post coming soon", Toast.LENGTH_SHORT).show()
        }
        settingLayout.root.setOnClickListener {
            Toast.makeText(requireContext(), "Setting coming soon", Toast.LENGTH_SHORT).show()
        }
        feedbackLayout.root.setOnClickListener {
            Toast.makeText(requireContext(), "Feedback coming soon", Toast.LENGTH_SHORT).show()
        }
        viewModel.userPostState.observe(viewLifecycleOwner) {
            when(it) {
                is Result.Loading -> {
                    Log.d(TAG, "userPostState Loading")
                }
                is Result.Error -> {
                    Log.e(TAG, "userPostState error : ${it.error}")
                }
                is Result.Success -> {
                    postLayout.counterText.text = "${it.data.size}"
                }
            }
        }

    }

    private companion object {
        const val TAG = "ProfileFragment"
    }

}