package com.app.sehatin.ui.activities.main.fragments.userPage

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.app.sehatin.R
import com.app.sehatin.data.model.User
import com.app.sehatin.databinding.FragmentUserPageBinding
import com.bumptech.glide.Glide

class UserPageFragment : Fragment() {
    private lateinit var binding: FragmentUserPageBinding
    private lateinit var user: User

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        binding = FragmentUserPageBinding.inflate(inflater, container, false)
        initVariable()
        initListener()
        return binding.root
    }

    private fun initVariable() = with(binding) {
        user = UserPageFragmentArgs.fromBundle(arguments as Bundle).user
        Glide.with(requireContext())
            .load(user.imageUrl)
            .placeholder(R.drawable.user_default)
            .error(R.drawable.user_default)
            .into(userImageIV)
        usernameTv.text = user.username
    }

    private fun initListener() = with(binding) {
        toolbar.setNavigationOnClickListener {
            requireActivity().onBackPressed()
        }
    }

}