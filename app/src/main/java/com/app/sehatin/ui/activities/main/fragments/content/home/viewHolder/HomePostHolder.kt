package com.app.sehatin.ui.activities.main.fragments.content.home.viewHolder

import android.content.Context
import androidx.lifecycle.LifecycleOwner
import androidx.recyclerview.widget.LinearLayoutManager
import com.app.sehatin.data.Result
import com.app.sehatin.data.model.Posting
import com.app.sehatin.databinding.ItemHomePostBinding
import com.app.sehatin.ui.activities.main.fragments.content.home.HomeViewModel
import com.app.sehatin.ui.activities.main.fragments.content.home.adapter.HorizontalPostAdapter
import com.app.sehatin.ui.sharedAdapter.ViewHolder

class HomePostHolder(private val binding: ItemHomePostBinding, private val homeViewModel: HomeViewModel, private val owner: LifecycleOwner): ViewHolder(binding.root) {
    private lateinit var context: Context
    private val trendingPostSize = 3L

    override fun bind(context: Context) {
        this.context = context
        initVariable()
        initListener()
    }

    private fun initVariable() {
        homeViewModel.getTrendingPost(trendingPostSize)
    }

    private fun initListener() {
        homeViewModel.trendingPostState.observe(owner) {
            when(it) {
                is Result.Loading -> {

                }
                is Result.Error -> {

                }
                is Result.Success -> {
                    setRvPost(it.data)
                }
            }
        }
    }

    private fun setRvPost(posts: List<Posting>) = with(binding) {
        val adapter = HorizontalPostAdapter(posts)
        rvPost.setHasFixedSize(true)
        rvPost.layoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
        rvPost.adapter = adapter
    }

}