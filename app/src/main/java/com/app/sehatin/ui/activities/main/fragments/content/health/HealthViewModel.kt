package com.app.sehatin.ui.activities.main.fragments.content.health

import androidx.lifecycle.ViewModel
import com.app.sehatin.data.repository.ExerciseRepository
import com.app.sehatin.data.repository.FoodRepository

class HealthViewModel(private val foodRepository: FoodRepository, private val exerciseRepository: ExerciseRepository): ViewModel() {

    fun getGoodFoods(token: String) = foodRepository.getGoodFoods(token)

}