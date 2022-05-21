package com.app.sehatin.ui.activities.main.fragments.content.home

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.app.sehatin.data.Result
import com.app.sehatin.data.model.Posting
import com.app.sehatin.data.repository.FoodRepository
import com.app.sehatin.data.repository.PostingRepository

class HomeViewModel(private val foodRepository: FoodRepository, private val postingRepository: PostingRepository): ViewModel() {

    val trendingPostState = MutableLiveData<Result<List<Posting>>>()

    fun getTrendingPost(size: Long) = postingRepository.getTrendingPost(trendingPostState, size)

}