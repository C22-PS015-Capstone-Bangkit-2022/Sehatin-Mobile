package com.app.sehatin.ui.activities.main.fragments.editProfile

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import com.app.sehatin.R
import com.app.sehatin.data.model.User
import com.app.sehatin.databinding.FragmentEditProfileBinding
import com.app.sehatin.utils.MAN
import com.bumptech.glide.Glide

class EditProfileFragment : Fragment() {
    private var _binding: FragmentEditProfileBinding? = null
    private lateinit var user: User
    private val binding get() = _binding!!

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        _binding = FragmentEditProfileBinding.inflate(inflater, container, false)
        initVariable()
        initListener()
        return binding.root
    }

    private fun initVariable() = with(binding) {
        user = EditProfileFragmentArgs.fromBundle(arguments as Bundle).user
        Glide.with(requireContext())
            .load(user.imageUrl)
            .placeholder(R.drawable.user_default)
            .error(R.drawable.user_default)
            .into(userImage)
        usernameInput.setText(user.username)
        dateOfBirthInput.setText(user.dateOfBirth)
        val genderList = resources.getStringArray(R.array.gender_list)
        val genderAdapter = ArrayAdapter(requireContext(), com.google.android.material.R.layout.support_simple_spinner_dropdown_item,genderList)
        genderInput.setAdapter(genderAdapter)
        user.gender?.let {
            if(it == MAN) {
                genderInput.setText(getString(R.string.man), false)
            } else {
                genderInput.setText(getString(R.string.woman), false)
            }
        }
    }

    private fun initListener() {

    }

}