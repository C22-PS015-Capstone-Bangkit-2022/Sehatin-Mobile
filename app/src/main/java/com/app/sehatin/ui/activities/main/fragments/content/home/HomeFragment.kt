package com.app.sehatin.ui.activities.main.fragments.content.home

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.app.sehatin.databinding.*
import com.app.sehatin.ui.activities.main.fragments.content.home.viewHolder.HomeContentHolder
import com.app.sehatin.ui.activities.main.fragments.content.home.viewHolder.HomePostHolder
import com.app.sehatin.ui.activities.main.fragments.content.home.viewHolder.HomeTopHolder
import com.app.sehatin.ui.sharedAdapter.ViewHolder
import com.app.sehatin.ui.sharedAdapter.ViewsAdapter
import com.app.sehatin.ui.viewmodel.ViewModelFactory
import com.google.android.material.bottomnavigation.BottomNavigationView

class HomeFragment(private val bottomNavigationView: BottomNavigationView) : Fragment() {

    private lateinit var binding : FragmentHomeBinding
    private lateinit var homeUiAdapter: ViewsAdapter
    private var listHomeUi = mutableListOf<ViewHolder>()
    private lateinit var homeViewModel: HomeViewModel

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        binding = FragmentHomeBinding.inflate(inflater, container, false)
        initVariable()
        initListener()
        return binding.root
    }

    private fun initVariable() = with(binding) {
        homeViewModel = ViewModelProvider(this@HomeFragment, ViewModelFactory.getInstance())[HomeViewModel::class.java]
        listHomeUi = mutableListOf(
            HomeTopHolder(ItemHomeTopBinding.inflate(LayoutInflater.from(requireContext()), binding.root, false).root),
            HomeContentHolder(ItemHomeContentBinding.inflate(LayoutInflater.from(requireContext()), binding.root, false).root, bottomNavigationView),
            HomePostHolder(ItemHomePostBinding.inflate(LayoutInflater.from(requireContext()), binding.root, false), homeViewModel)
        )
        homeUiAdapter = ViewsAdapter(listHomeUi)
        rvUi.setHasFixedSize(true)
        rvUi.layoutManager = LinearLayoutManager(requireContext())
        rvUi.adapter = homeUiAdapter
    }

    private fun initListener() {

    }

}