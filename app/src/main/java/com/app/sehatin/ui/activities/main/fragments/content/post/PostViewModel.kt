package com.app.sehatin.ui.activities.main.fragments.content.post

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.cachedIn
import com.app.sehatin.data.repository.PostingRepository
import com.app.sehatin.data.Result
import com.app.sehatin.data.model.Posting
import com.google.firebase.firestore.Query
import java.io.File

class PostViewModel(private val postingRepository: PostingRepository, private val queryProductsByDate: Query): ViewModel() {
    val uploadPostState = MutableLiveData<Result<Map<String, Any?>>>()

    fun getPosts() = postingRepository.getPosts(queryProductsByDate).cachedIn(viewModelScope)

    fun uploadPost(postImage: File?, postDescription: String, postTags: List<String>?) = postingRepository.uploadPost(uploadPostState, postImage, postDescription, postTags)

    fun togglePostLike(posting: Posting, isLike: Boolean) = postingRepository.togglePostLike(posting, isLike)

}