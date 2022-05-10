package com.app.sehatin.ui.activities.main.fragments.home

import androidx.lifecycle.ViewModel
import com.app.sehatin.data.repository.FoodRepository

class HomeViewModel(private val foodRepository: FoodRepository): ViewModel() {

    fun getFood() = foodRepository.getFood()

}