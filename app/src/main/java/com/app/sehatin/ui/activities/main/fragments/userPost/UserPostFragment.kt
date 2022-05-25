package com.app.sehatin.ui.activities.main.fragments.userPost

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.app.sehatin.databinding.FragmentUserPostBinding

class UserPostFragment : Fragment() {
    private lateinit var binding: FragmentUserPostBinding

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = FragmentUserPostBinding.inflate(inflater, container, false)
        return binding.root
    }

}