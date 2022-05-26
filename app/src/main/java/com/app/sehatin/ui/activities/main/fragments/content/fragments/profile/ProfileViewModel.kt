package com.app.sehatin.ui.activities.main.fragments.content.fragments.profile

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.app.sehatin.data.Result
import com.app.sehatin.data.model.Posting
import com.app.sehatin.data.repository.PostingRepository

class ProfileViewModel(private val postingRepository: PostingRepository): ViewModel() {
    val userPostState = MutableLiveData<Result<List<Posting>>>()

    fun getUserPost(userId: String) = postingRepository.getUserPost(userPostState, userId)

}