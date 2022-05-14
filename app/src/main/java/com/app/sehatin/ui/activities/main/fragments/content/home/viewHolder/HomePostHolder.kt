package com.app.sehatin.ui.activities.main.fragments.content.home.viewHolder

import android.content.Context
import androidx.recyclerview.widget.LinearLayoutManager
import com.app.sehatin.databinding.ItemHomePostBinding
import com.app.sehatin.ui.activities.main.fragments.content.home.HomeViewModel
import com.app.sehatin.ui.activities.main.fragments.content.home.adapter.HorizontalPostAdapter
import com.app.sehatin.ui.sharedAdapter.ViewHolder

class HomePostHolder(private val binding: ItemHomePostBinding, private val homeViewModel: HomeViewModel): ViewHolder(binding.root) {
    private lateinit var context: Context

    override fun bind(context: Context) {
        this.context = context
        initVariable()
        initListener()
    }

    private fun initVariable() = with(binding) {
        val post =  homeViewModel.getPost(5)
        val adapter = HorizontalPostAdapter(post)
        rvPost.setHasFixedSize(true)
        rvPost.layoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
        rvPost.adapter = adapter
    }

    private fun initListener() = with(binding) {

    }

}