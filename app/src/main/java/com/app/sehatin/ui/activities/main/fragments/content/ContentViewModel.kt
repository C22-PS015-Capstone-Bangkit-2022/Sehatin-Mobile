package com.app.sehatin.ui.activities.main.fragments.content

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.app.sehatin.R
import com.app.sehatin.data.Result
import com.app.sehatin.data.model.Article
import com.app.sehatin.data.model.Food
import com.app.sehatin.data.model.Posting
import com.app.sehatin.data.repository.ArticleRepository
import com.app.sehatin.data.repository.FoodRepository
import com.app.sehatin.data.repository.PostingRepository

class ContentViewModel(
    private val foodRepository: FoodRepository,
    private val postingRepository: PostingRepository,
    private val articleRepository: ArticleRepository
): ViewModel() {

    var selectedItemId = R.id.nav_home
    val trendingPostState = MutableLiveData<Result<List<Posting>>>()

    //FOR HOME FRAGMENT
    val topArticle = mutableListOf<Article>()
    val goodFoods = mutableListOf<Food>()
    val trendingPost = mutableListOf<Posting>()
    val moreArticle = mutableListOf<Article>()

    //FOR HEALTH FRAGMENT
    val healthGoodFoods = mutableListOf<Food>()

    //FOR ARTICLE FRAGMENT
    val articles = mutableListOf<Article>()

    fun getGoodFoods(token: String) = foodRepository.getGoodFoods(token)

    fun getTrendingPost(size: Long) = postingRepository.getTrendingPost(trendingPostState, size)

    fun getArticles() = articleRepository.getArticles()

    fun getArticles(page: Int, size: Int) = articleRepository.getArticles(page, size)
}