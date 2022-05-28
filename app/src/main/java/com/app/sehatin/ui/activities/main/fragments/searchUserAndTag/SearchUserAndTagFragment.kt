package com.app.sehatin.ui.activities.main.fragments.searchUserAndTag

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.app.sehatin.databinding.FragmentSearchUserAndTagBinding
import com.app.sehatin.ui.viewmodel.ViewModelFactory

class SearchUserAndTagFragment : Fragment() {
    private lateinit var binding : FragmentSearchUserAndTagBinding
    private lateinit var viewModel: SearchUserAndTagViewModel

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        binding = FragmentSearchUserAndTagBinding.inflate(inflater, container, false)
        initVariable()
        initListener()
        return binding.root
    }

    private fun initVariable() = with(binding) {
        viewModel = ViewModelProvider(this@SearchUserAndTagFragment, ViewModelFactory.getInstance())[SearchUserAndTagViewModel::class.java]
        searchBar.requestFocus()
    }

    private fun initListener() {

    }


}