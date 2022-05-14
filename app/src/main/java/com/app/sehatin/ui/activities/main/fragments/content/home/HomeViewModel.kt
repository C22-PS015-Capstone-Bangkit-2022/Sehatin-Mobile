package com.app.sehatin.ui.activities.main.fragments.content.home

import androidx.lifecycle.ViewModel
import com.app.sehatin.data.repository.FoodRepository
import com.app.sehatin.data.repository.PostingRepository

class HomeViewModel(
    private val foodRepository: FoodRepository,
    private val postingRepository: PostingRepository
    ): ViewModel() {

    fun getFood() = foodRepository.getFood()

    fun getPost(size: Int) = postingRepository.getPosts(size)

}