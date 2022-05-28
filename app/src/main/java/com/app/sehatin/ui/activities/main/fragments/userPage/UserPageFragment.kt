package com.app.sehatin.ui.activities.main.fragments.userPage

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.app.sehatin.databinding.FragmentUserPageBinding

class UserPageFragment : Fragment() {
    private lateinit var binding: FragmentUserPageBinding

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        binding = FragmentUserPageBinding.inflate(inflater, container, false)
        initVariable()
        initListener()
        return binding.root
    }

    private fun initVariable() {

    }

    private fun initListener() {

    }

}