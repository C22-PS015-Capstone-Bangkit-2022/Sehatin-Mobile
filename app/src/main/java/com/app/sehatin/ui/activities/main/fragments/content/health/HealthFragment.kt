package com.app.sehatin.ui.activities.main.fragments.content.health

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.app.sehatin.R
import com.app.sehatin.databinding.FragmentHealthBinding

class HealthFragment : Fragment() {
    private lateinit var binding: FragmentHealthBinding

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = FragmentHealthBinding.inflate(inflater, container, false)
        initVariable()
        initListener()
        return binding.root
    }

    private fun initVariable() {

    }

    private fun initListener() {

    }

}