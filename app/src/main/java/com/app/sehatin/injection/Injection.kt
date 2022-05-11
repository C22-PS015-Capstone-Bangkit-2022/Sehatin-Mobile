package com.app.sehatin.injection

import com.app.sehatin.data.remote.RetrofitClient
import com.app.sehatin.data.repository.AuthenticationRepository
import com.app.sehatin.data.repository.FoodRepository

object Injection {
    private val apiService = RetrofitClient.getInstance()

    fun provideFoodRepository(): FoodRepository {
        return FoodRepository(apiService)
    }

    fun provideAuthRepository(): AuthenticationRepository {
        return AuthenticationRepository(apiService)
    }

}