package com.app.sehatin.ui.activities.main.fragments.userPost

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.app.sehatin.data.Result
import com.app.sehatin.data.model.Posting
import com.app.sehatin.data.repository.PostingRepository

class UserPostViewModel(private val postingRepository: PostingRepository): ViewModel() {

    val userPostState = MutableLiveData<Result<List<Posting>>>()

    fun getPosts(userId: String) = postingRepository.getUserPost(userPostState, userId)

}