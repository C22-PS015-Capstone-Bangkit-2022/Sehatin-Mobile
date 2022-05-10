package com.app.sehatin.ui.activities.main.fragments.home

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import com.app.sehatin.databinding.FragmentHomeBinding
import com.app.sehatin.databinding.ItemHomeContentBinding
import com.app.sehatin.databinding.ItemHomeTopBinding
import com.app.sehatin.ui.activities.main.fragments.home.viewHolder.HomeContentHolder
import com.app.sehatin.ui.activities.main.fragments.home.viewHolder.HomeTopHolder
import com.app.sehatin.ui.sharedAdapter.ViewHolder
import com.app.sehatin.ui.sharedAdapter.ViewsAdapter

class HomeFragment : Fragment() {

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
        listHomeUi = mutableListOf(
            HomeTopHolder(ItemHomeTopBinding.inflate(LayoutInflater.from(requireContext()), binding.root, false).root),
            HomeContentHolder(ItemHomeContentBinding.inflate(LayoutInflater.from(requireContext()), binding.root, false).root, requireContext())
        )

        homeUiAdapter = ViewsAdapter(listHomeUi)
        rvUi.setHasFixedSize(true)
        rvUi.layoutManager = LinearLayoutManager(requireContext())
        rvUi.adapter = homeUiAdapter
    }

    private fun initListener() {

    }

}