package com.app.sehatin.ui.activities.start.fragments.register

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.app.sehatin.data.model.User
import com.app.sehatin.data.repository.AuthenticationRepository
import com.app.sehatin.data.Result

class AuthenticationViewModel(private val authenticationRepository: AuthenticationRepository): ViewModel() {

    val registerState = MutableLiveData<Result<User>>()

    fun register(email: String, password: String, userData: Map<String, Any?>) = authenticationRepository.register(registerState, email, password, userData)

}