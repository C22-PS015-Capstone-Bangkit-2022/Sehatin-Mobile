package com.app.sehatin.ui.activities.main.fragments.content.home.viewHolder

import android.content.Context
import android.util.Log
import android.view.View
import androidx.lifecycle.LifecycleOwner
import androidx.recyclerview.widget.LinearLayoutManager
import com.app.sehatin.data.Result
import com.app.sehatin.data.model.Article
import com.app.sehatin.databinding.ItemHomeArticleBinding
import com.app.sehatin.ui.activities.main.fragments.content.home.HomeViewModel
import com.app.sehatin.ui.sharedAdapter.ArticleAdapter
import com.app.sehatin.ui.sharedAdapter.ViewHolder

class HomeArticleHolder(
    private val binding: ItemHomeArticleBinding,
    private val homeViewModel: HomeViewModel,
    private val lifecycleOwner: LifecycleOwner
) : ViewHolder(binding.root) {
    private lateinit var context: Context
    private lateinit var articleAdapter: ArticleAdapter

    override fun bind(context: Context) {
        this.context = context
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
        homeViewModel.getArticles(1, 5).observe(lifecycleOwner) {
            when(it) {
                is Result.Loading -> {
                    showLoading(true)
                }
                is Result.Error -> {
                    onErrorHandle()
                }
                is Result.Success -> {
                    showLoading(false)
                    Log.d(TAG, "getArticles success : ${it.data}")
                    val article = it.data?.articles
                    if(article != null) {
                        articleAdapter.submitList(article)
                    } else {
                        onErrorHandle()
                    }
                }
            }
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
        textView4.visibility = View.GONE
        rvArticle.visibility = View.GONE
        shimmerLoading.visibility = View.GONE
    }

    companion object {
        private const val TAG = "HomeArticleHolder"
    }

}