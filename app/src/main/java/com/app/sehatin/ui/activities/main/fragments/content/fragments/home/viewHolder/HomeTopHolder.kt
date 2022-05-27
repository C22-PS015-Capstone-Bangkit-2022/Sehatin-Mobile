package com.app.sehatin.ui.activities.main.fragments.content.fragments.home.viewHolder

import android.content.Context
import android.util.Log
import android.view.View
import androidx.fragment.app.Fragment
import androidx.lifecycle.LifecycleOwner
import androidx.navigation.fragment.findNavController
import androidx.viewpager2.widget.ViewPager2
import com.app.sehatin.data.Result
import com.app.sehatin.data.model.Article
import com.app.sehatin.databinding.ItemHomeTopBinding
import com.app.sehatin.ui.activities.main.fragments.content.ContentFragmentDirections
import com.app.sehatin.ui.activities.main.fragments.content.ContentViewModel
import com.app.sehatin.ui.activities.main.fragments.content.fragments.home.adapter.HeroAdapter
import com.app.sehatin.ui.activities.main.fragments.content.adapter.ViewHolder

class HomeTopHolder(
    private val parent: Fragment,
    itemView: View,
    private val lifecycleOwner: LifecycleOwner
    ) : ViewHolder(itemView) {

    private val binding = ItemHomeTopBinding.bind(itemView)
    private lateinit var viewModel: ContentViewModel

    override fun bind(context: Context, viewModel: ContentViewModel) {
        this.viewModel = viewModel
        initListener()
    }

    private fun initListener() {
        if(viewModel.topArticle.isEmpty()) {
            viewModel.getArticles(0, 5).observe(lifecycleOwner) {
                when (it) {
                    is Result.Loading -> {
                        showLoading(true)
                    }
                    is Result.Error -> {
                        Log.e(TAG, "initListener: ${it.error}")
                    }
                    is Result.Success -> {
                        val article = it.data?.articles
                        if (article != null) {
                            viewModel.topArticle.addAll(article)
                            showLoading(false)
                            setViewPager(viewModel.topArticle)
                        }
                    }
                }
            }
        } else {
            showLoading(false)
            setViewPager(viewModel.topArticle)
        }
    }

    private fun setViewPager(articles: List<Article>) = with(binding) {
        viewPager.adapter = HeroAdapter(articles) {
            val direction = ContentFragmentDirections.actionContentFragmentToArticleDetailFragment(it)
            parent.findNavController().navigate(direction)
        }
        viewPager.orientation = ViewPager2.ORIENTATION_HORIZONTAL
        indicator.setViewPager(this.viewPager)
    }

    private fun showLoading(isLoading: Boolean) = with(binding) {
        if (isLoading) {
            contentGroup.visibility = View.INVISIBLE
            shimmerLoading.visibility = View.VISIBLE
        } else {
            contentGroup.visibility = View.VISIBLE
            shimmerLoading.visibility = View.GONE
        }
    }

    private companion object {
        const val TAG = "HomeTopHolder"
    }

}