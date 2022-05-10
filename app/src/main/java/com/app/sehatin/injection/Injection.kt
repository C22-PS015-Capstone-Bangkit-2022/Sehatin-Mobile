package com.app.sehatin.injection

import com.app.sehatin.data.remote.RetrofitClient
import com.app.sehatin.data.repository.FoodRepository

object Injection {

    fun provideFoodRepository(): FoodRepository {
        val apiService = RetrofitClient.getInstance()
        return FoodRepository(apiService)
    }

}