package com.app.sehatin.injection

import com.app.sehatin.data.remote.RetrofitClient
import com.app.sehatin.data.repository.AuthenticationRepository
import com.app.sehatin.data.repository.DiseaseRepository
import com.app.sehatin.data.repository.FoodRepository
import com.app.sehatin.data.repository.PostingRepository
import com.app.sehatin.utils.DATE_PROPERTY
import com.app.sehatin.utils.PAGE_SIZE
import com.app.sehatin.utils.POST_COLLECTION
import com.app.sehatin.utils.USER_COLLECTION
import com.google.firebase.auth.FirebaseAuth
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

    fun provideDiseaseRepository(): DiseaseRepository {
        return DiseaseRepository(apiService)
    }

    fun provideQueryProductsByDate() = FirebaseFirestore.getInstance()
        .collection(POST_COLLECTION)
        .orderBy(DATE_PROPERTY, Query.Direction.DESCENDING)
        .limit(PAGE_SIZE.toLong())

    fun providePostCollection() = FirebaseFirestore.getInstance().collection(POST_COLLECTION)
    fun provideUserCollection() = FirebaseFirestore.getInstance().collection(USER_COLLECTION)

}