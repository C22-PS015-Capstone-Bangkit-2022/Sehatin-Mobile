package com.app.sehatin.ui.activities.main.fragments.userPost

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.app.sehatin.databinding.FragmentUserPostBinding

class UserPostFragment : Fragment() {
    private lateinit var binding: FragmentUserPostBinding
    private lateinit var postType: String

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        binding = FragmentUserPostBinding.inflate(inflater, container, false)
        initVariable()
        initListener()
        return binding.root
    }

    private fun initVariable() {
        postType = UserPostFragmentArgs.fromBundle(arguments as Bundle).type
    }

    private fun initListener() {

    }

    companion object {
        const val TYPE_USER_POST = "user post"
        const val TYPE_SAVED_POST = "saved post"
    }

}