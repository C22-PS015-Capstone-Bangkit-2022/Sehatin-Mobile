package com.app.sehatin.ui.activities.main.fragments.content.home.viewHolder

import android.content.Context
import android.util.Log
import android.widget.Toast
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
                    Log.d(TAG, "getArticles loading")
                }
                is Result.Error -> {
                    Log.e(TAG, "getArticles error : ${it.error}")
                }
                is Result.Success -> {
                    Log.d(TAG, "getArticles success : ${it.data}")
                    val article = it.data?.articles
                    if(article != null) {
                        articleAdapter.submitList(article)
                    } else {
                        Toast.makeText(context, "data null", Toast.LENGTH_SHORT).show()
                    }
                }
            }
        }
    }

    companion object {
        private const val TAG = "HomeArticleHolder"
    }

}