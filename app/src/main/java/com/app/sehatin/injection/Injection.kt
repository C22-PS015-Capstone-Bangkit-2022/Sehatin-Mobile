package com.app.sehatin.injection

import com.app.sehatin.data.remote.RetrofitClient
import com.app.sehatin.data.repository.AuthenticationRepository
import com.app.sehatin.data.repository.FoodRepository
import com.app.sehatin.data.repository.PostingRepository
import com.app.sehatin.utils.DATE_PROPERTY
import com.app.sehatin.utils.PAGE_SIZE
import com.app.sehatin.utils.POST_COLLECTION
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query

object Injection {
    private val apiService = RetrofitClient.getInstance()

    fun provideFoodRepository(): FoodRepository {
        return FoodRepository(apiService)
    }

    fun provideAuthRepository(): AuthenticationRepository {
        return AuthenticationRepository()
    }

    fun providePostingRepository(): PostingRepository {
        return PostingRepository()
    }

    fun provideQueryProductsByDate() = FirebaseFirestore.getInstance()
        .collection(POST_COLLECTION)
        .orderBy(DATE_PROPERTY, Query.Direction.DESCENDING)
        .limit(PAGE_SIZE.toLong())

}