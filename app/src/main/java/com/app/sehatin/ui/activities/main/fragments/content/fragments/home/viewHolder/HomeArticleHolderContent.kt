package com.app.sehatin.ui.activities.main.fragments.content.fragments.home.viewHolder

import android.content.Context
import android.util.Log
import android.view.View
import androidx.core.view.children
import androidx.fragment.app.Fragment
import androidx.lifecycle.LifecycleOwner
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.app.sehatin.R
import com.app.sehatin.data.Result
import com.app.sehatin.databinding.ItemHomeArticleBinding
import com.app.sehatin.ui.activities.main.fragments.content.ContentFragmentDirections
import com.app.sehatin.ui.activities.main.fragments.content.ContentViewModel
import com.app.sehatin.ui.sharedAdapter.ArticleMinAdapter
import com.app.sehatin.ui.activities.main.fragments.content.adapter.ContentViewHolder

class HomeArticleHolderContent(
    private val parent: Fragment,
    private val binding: ItemHomeArticleBinding,
    private val lifecycleOwner: LifecycleOwner
) : ContentViewHolder(binding.root) {

    private lateinit var context: Context
    private lateinit var articleMinAdapter: ArticleMinAdapter
    private lateinit var viewModel: ContentViewModel

    override fun bind(context: Context, viewModel: ContentViewModel) {
        this.context = context
        this.viewModel = viewModel
        initVariable()
        initListener()
    }

    private fun initVariable() = with(binding) {
        articleMinAdapter = ArticleMinAdapter {
            val direction = ContentFragmentDirections.actionContentFragmentToArticleDetailFragment(it)
            parent.findNavController().navigate(direction)
        }
        rvArticle.setHasFixedSize(true)
        rvArticle.layoutManager = LinearLayoutManager(context)
        rvArticle.adapter = articleMinAdapter
    }

    private fun initListener() = with(binding) {
        otherBtn.setOnClickListener {
            parent.findNavController().navigate(R.id.action_contentFragment_to_articleFragment)
        }
        if(viewModel.moreArticle.isEmpty()) {
            viewModel.getArticles(1, 5).observe(lifecycleOwner) {
                when(it) {
                    is Result.Loading -> {
                        showLoading(true)
                    }
                    is Result.Error -> {
                        onErrorHandle()
                    }
                    is Result.Success -> {
                        showLoading(false)
                        Log.d(TAG, "getArticles success : ${it.data?.articles?.size}")
                        val article = it.data?.articles
                        if(article != null) {
                            viewModel.moreArticle.addAll(article)
                            articleMinAdapter.submitList(viewModel.moreArticle)
                        } else {
                            onErrorHandle()
                        }
                    }
                }
            }
        } else {
            showLoading(false)
            articleMinAdapter.submitList(viewModel.moreArticle)
        }
    }

    private fun showLoading(isLoading: Boolean) = with(binding) {
        if(isLoading) {
            shimmerLoading.visibility = View.VISIBLE
            rvArticle.visibility = View.GONE
        } else {
            shimmerLoading.visibility = View.GONE
            rvArticle.visibility = View.VISIBLE
        }
    }

    private fun onErrorHandle() = with(binding) {
       this.root.children.forEach {
           it.visibility = View.GONE
       }
    }

    companion object {
        private const val TAG = "HomeArticleHolder"
    }

}