package com.app.sehatin.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.app.sehatin.data.repository.AuthenticationRepository
import com.app.sehatin.data.repository.FoodRepository
import com.app.sehatin.data.repository.PostingRepository
import com.app.sehatin.injection.Injection
import com.app.sehatin.ui.activities.main.fragments.content.home.HomeViewModel
import com.app.sehatin.ui.activities.main.fragments.content.post.PostViewModel
import com.app.sehatin.ui.activities.start.fragments.register.AuthenticationViewModel

class ViewModelFactory private constructor(
    private val foodRepository: FoodRepository,
    private val postingRepository: PostingRepository,
    private val authenticationRepository: AuthenticationRepository
    ): ViewModelProvider.NewInstanceFactory() {

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return when {
            modelClass.isAssignableFrom(HomeViewModel::class.java) -> {
                HomeViewModel(foodRepository) as T
            }
            modelClass.isAssignableFrom(PostViewModel::class.java) -> {
                PostViewModel(postingRepository) as T
            }
            modelClass.isAssignableFrom(AuthenticationViewModel::class.java) -> {
                AuthenticationViewModel(authenticationRepository) as T
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
                Injection.provideAuthRepository()
            )
        }.also {
            instance = it
        }
    }

}