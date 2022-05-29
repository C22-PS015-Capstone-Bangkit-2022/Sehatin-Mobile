package com.app.sehatin.ui.activities.main.fragments.searchUserAndTag

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.app.sehatin.data.Result
import com.app.sehatin.data.model.User
import com.app.sehatin.data.repository.UserRepository

class SearchUserAndTagViewModel(private val userRepository: UserRepository): ViewModel() {

    val searchUserState = MutableLiveData<Result<List<User>>>()
    fun searchUser(keyword: String) = userRepository.searsUser(keyword, searchUserState)

}