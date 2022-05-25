package com.app.sehatin.ui.activities.main.fragments.userDiseases

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.app.sehatin.R
import com.app.sehatin.databinding.FragmentUserDiseasesBinding

class UserDiseasesFragment : Fragment() {
    private lateinit var binding : FragmentUserDiseasesBinding

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        binding = FragmentUserDiseasesBinding.inflate(inflater, container, false)
        initVariable()
        initListener()
        return binding.root
    }

    private fun initVariable() {

    }

    private fun initListener() {

    }


}