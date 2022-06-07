package com.app.sehatin.injection

import com.app.sehatin.data.remote.RetrofitClient
import com.app.sehatin.data.repository.*
import com.app.sehatin.utils.*
import com.google.firebase.database.ktx.database
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import com.google.firebase.ktx.Firebase

object Injection {
    private val apiService = RetrofitClient.getInstance()

    fun provideFoodRepository(): FoodRepository {
        return FoodRepository(apiService)
    }

    fun provideExerciseRepository(): ExerciseRepository {
        return ExerciseRepository(apiService)
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

    fun provideArticleRepository(): ArticleRepository {
        return ArticleRepository(apiService)
    }

    fun provideObjectDetectionRepository(): ObjectDetectionRepository {
        return ObjectDetectionRepository()
    }

    fun provideUserRepository(): UserRepository {
        return UserRepository()
    }

    fun provideChatRepository(): ChatRepository {
        return ChatRepository()
    }

    fun provideDoctorRepository(): DoctorRepository {
        return DoctorRepository()
    }

    fun provideDoctorSessionRepository(): DoctorSessionRepository {
        return DoctorSessionRepository()
    }

    fun provideQueryProductsByDate() = FirebaseFirestore.getInstance()
        .collection(POST_COLLECTION)
        .orderBy(DATE_PROPERTY, Query.Direction.DESCENDING)
        .limit(PAGE_SIZE.toLong())

    fun providePostCollection() = FirebaseFirestore.getInstance().collection(POST_COLLECTION)
    fun provideUserCollection() = FirebaseFirestore.getInstance().collection(USER_COLLECTION)
    fun provideDoctorCollection() = FirebaseFirestore.getInstance().collection(DOCTOR_COLLECTION)

    private const val RealtimeDatabaseUrl = "https://sehatin-eab72-default-rtdb.asia-southeast1.firebasedatabase.app/"
    fun provideChatReference() = Firebase.database(RealtimeDatabaseUrl).reference.child(CHAT_REFERENCE)
    fun provideHistoryChatReference() = Firebase.database(RealtimeDatabaseUrl).reference.child(HISTORY_CHAT_REFERENCE)
    fun provideUserDoctorActiveReference() = Firebase.database(RealtimeDatabaseUrl).reference.child(DOCTOR_SESSION_REFERENCE)

}