package com.app.sehatin.ui.activities.main.fragments.searchUserAndTag

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.app.sehatin.databinding.FragmentSearchUserAndTagBinding

class SearchUserAndTagFragment : Fragment() {
    private lateinit var binding : FragmentSearchUserAndTagBinding

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        binding = FragmentSearchUserAndTagBinding.inflate(inflater, container, false)
        return binding.root
    }

}