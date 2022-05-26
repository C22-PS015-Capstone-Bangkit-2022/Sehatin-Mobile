package com.app.sehatin.ui.activities.main.fragments.content.fragments.home.viewHolder

import android.content.Context
import android.util.Log
import android.view.View
import androidx.core.view.children
import androidx.lifecycle.LifecycleOwner
import androidx.recyclerview.widget.LinearLayoutManager
import com.app.sehatin.data.Result
import com.app.sehatin.data.model.Article
import com.app.sehatin.databinding.ItemHomeArticleBinding
import com.app.sehatin.ui.activities.main.fragments.content.ContentViewModel
import com.app.sehatin.ui.sharedAdapter.ArticleAdapter
import com.app.sehatin.ui.activities.main.fragments.content.adapter.ViewHolder

class HomeArticleHolder(
    private val binding: ItemHomeArticleBinding,
    private val lifecycleOwner: LifecycleOwner
) : ViewHolder(binding.root) {

    private lateinit var context: Context
    private lateinit var articleAdapter: ArticleAdapter
    private lateinit var viewModel: ContentViewModel

    override fun bind(context: Context, viewModel: ContentViewModel) {
        this.context = context
        this.viewModel = viewModel
        initVariable()
        initListener()
    }

    private fun initVariable() = with(binding) {
        articleAdapter = ArticleAdapter(object : ArticleAdapter.OnClickListener {
            override fun onViewClick(article: Article) {

            }
        })
        rvArticle.setHasFixedSize(true)
        rvArticle.layoutManager = LinearLayoutManager(context)
        rvArticle.adapter = articleAdapter
    }

    private fun initListener() {
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
                            articleAdapter.submitList(viewModel.moreArticle)
                        } else {
                            onErrorHandle()
                        }
                    }
                }
            }
        } else {
            showLoading(false)
            articleAdapter.submitList(viewModel.moreArticle)
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