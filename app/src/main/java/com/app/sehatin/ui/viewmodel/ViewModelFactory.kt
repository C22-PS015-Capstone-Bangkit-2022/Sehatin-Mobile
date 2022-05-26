package com.app.sehatin.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.app.sehatin.data.repository.*
import com.app.sehatin.injection.Injection
import com.app.sehatin.ui.activities.main.fragments.content.home.HomeViewModel
import com.app.sehatin.ui.activities.main.fragments.content.post.PostViewModel
import com.app.sehatin.ui.activities.main.fragments.content.profile.ProfileViewModel
import com.app.sehatin.ui.activities.main.fragments.diagnosis.DiagnosisViewModel
import com.app.sehatin.ui.activities.main.fragments.userDiseases.UserDiseasesViewModel
import com.app.sehatin.ui.activities.main.fragments.userPost.UserPostViewModel
import com.app.sehatin.ui.activities.start.fragments.AuthenticationViewModel
import com.google.firebase.firestore.Query

class ViewModelFactory private constructor(
    private val foodRepository: FoodRepository,
    private val postingRepository: PostingRepository,
    private val authenticationRepository: AuthenticationRepository,
    private val diseaseRepository: DiseaseRepository,
    private val articleRepository: ArticleRepository,
    private val queryProductsByDate: Query,
    ): ViewModelProvider.NewInstanceFactory() {

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return when {
            modelClass.isAssignableFrom(HomeViewModel::class.java) -> {
                HomeViewModel(foodRepository, postingRepository, articleRepository) as T
            }
            modelClass.isAssignableFrom(PostViewModel::class.java) -> {
                PostViewModel(postingRepository, queryProductsByDate) as T
            }
            modelClass.isAssignableFrom(AuthenticationViewModel::class.java) -> {
                AuthenticationViewModel(authenticationRepository) as T
            }
            modelClass.isAssignableFrom(DiagnosisViewModel::class.java) -> {
                DiagnosisViewModel(diseaseRepository) as T
            }
            modelClass.isAssignableFrom(UserDiseasesViewModel::class.java) -> {
                UserDiseasesViewModel(diseaseRepository) as T
            }
            modelClass.isAssignableFrom(ProfileViewModel::class.java) -> {
                ProfileViewModel(postingRepository) as T
            }
            modelClass.isAssignableFrom(UserPostViewModel::class.java) -> {
                UserPostViewModel(postingRepository) as T
            }
            else -> throw IllegalArgumentException("Unknown ViewModel class: " + modelClass.name)
        }
    }

    companion object {
        @Volatile
        private var instance: ViewModelFactory? = null

        fun getInstance(): ViewModelFactory = instance ?: synchronized(this) {
            instance ?: ViewModelFactory(
                Injection.provideFoodRepository(),
                Injection.providePostingRepository(),
                Injection.provideAuthRepository(),
                Injection.provideDiseaseRepository(),
                Injection.provideArticleRepository(),
                Injection.provideQueryProductsByDate(),
            )
        }.also {
            instance = it
        }
    }

}