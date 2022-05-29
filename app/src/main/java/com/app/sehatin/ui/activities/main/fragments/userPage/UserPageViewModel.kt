package com.app.sehatin.ui.activities.main.fragments.userPage

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.app.sehatin.data.Result
import com.app.sehatin.data.model.Posting
import com.app.sehatin.data.repository.PostingRepository

class UserPageViewModel(private val postingRepository: PostingRepository): ViewModel() {

    val userPost = mutableListOf<Posting>()

    val userPostState = MutableLiveData<Result<List<Posting>>>()
    fun getUserPost(userId: String) = postingRepository.getUserPost(userPostState, userId)

    fun togglePostLike(posting: Posting, isLike: Boolean) = postingRepository.togglePostLike(posting, isLike)

}