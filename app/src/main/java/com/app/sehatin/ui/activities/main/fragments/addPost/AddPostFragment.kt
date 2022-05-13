package com.app.sehatin.ui.activities.main.fragments.addPost

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.app.sehatin.databinding.FragmentAddPostBinding

class AddPostFragment : Fragment() {
    private lateinit var binding: FragmentAddPostBinding

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        binding = FragmentAddPostBinding.inflate(inflater, container, false)
        return binding.root
    }

}