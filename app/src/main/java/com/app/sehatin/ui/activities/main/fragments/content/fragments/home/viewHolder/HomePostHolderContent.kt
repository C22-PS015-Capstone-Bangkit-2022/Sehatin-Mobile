package com.app.sehatin.ui.activities.main.fragments.content.fragments.home.viewHolder

import android.content.Context
import android.view.View
import androidx.core.view.children
import androidx.fragment.app.Fragment
import androidx.lifecycle.LifecycleOwner
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.app.sehatin.data.Result
import com.app.sehatin.data.model.Posting
import com.app.sehatin.databinding.ItemHomePostBinding
import com.app.sehatin.ui.activities.main.fragments.content.ContentFragmentDirections
import com.app.sehatin.ui.activities.main.fragments.content.ContentViewModel
import com.app.sehatin.ui.activities.main.fragments.content.fragments.home.adapter.HorizontalPostAdapter
import com.app.sehatin.ui.activities.main.fragments.content.adapter.ContentViewHolder

class HomePostHolderContent(
    private val parent: Fragment,
    private val binding: ItemHomePostBinding,
    private val owner: LifecycleOwner
    ): ContentViewHolder(binding.root) {

    private lateinit var context: Context
    private lateinit var viewModel: ContentViewModel
    private val trendingPostSize = 3L

    override fun bind(context: Context, viewModel: ContentViewModel) {
        this.context = context
        this.viewModel = viewModel
        viewModel.getTrendingPost(trendingPostSize)
        initListener()
    }

    private fun initListener() {
        if(viewModel.trendingPost.isEmpty()) {
            viewModel.trendingPostState.observe(owner) {
                when(it) {
                    is Result.Loading -> {
                        showLoading(true)
                    }
                    is Result.Error -> {
                        onErrorHandle()
                    }
                    is Result.Success -> {
                        val data = it.data
                        viewModel.trendingPost.addAll(data)
                        showLoading(false)
                        setRvPost(viewModel.trendingPost)
                    }
                }
            }
        } else {
            showLoading(false)
            setRvPost(viewModel.trendingPost)
        }
    }

    private fun setRvPost(posts: List<Posting>) = with(binding) {
        val adapter = HorizontalPostAdapter(posts) {
            val direction = ContentFragmentDirections.actionContentFragmentToPostDetailFragment(it)
            parent.findNavController().navigate(direction)
        }
        rvPost.setHasFixedSize(true)
        rvPost.layoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
        rvPost.adapter = adapter
    }

    private fun showLoading(isLoading: Boolean) = with(binding) {
        if(isLoading) {
            shimmerLoading.visibility = View.VISIBLE
            rvPost.visibility = View.GONE
        } else {
            shimmerLoading.visibility = View.GONE
            rvPost.visibility = View.VISIBLE
        }
    }

    private fun onErrorHandle() = with(binding) {
        this.rvPost.children.forEach {
            it.visibility = View.GONE
        }
    }

}