package com.app.sehatin.ui.activities.start.fragments

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.app.sehatin.data.model.User
import com.app.sehatin.data.repository.AuthenticationRepository
import com.app.sehatin.data.Result
import java.io.File

class AuthenticationViewModel(private val authenticationRepository: AuthenticationRepository): ViewModel() {

    val registerState = MutableLiveData<Result<User>>()
    val loginState = MutableLiveData<Result<User>>()
    val updateUserState = MutableLiveData<Result<String>>()

    fun login(email: String, password: String) = authenticationRepository.login(loginState, email, password)

    fun register(email: String, password: String, userData: Map<String, Any?>) = authenticationRepository.register(registerState, email, password, userData)

    fun updateUser(userId: String, image: File?, userData: MutableMap<String, Any>) = authenticationRepository.updateUser(updateUserState, userId, image, userData)

}