package com.app.sehatin.ui.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.cachedIn
import com.app.sehatin.data.repository.PostingRepository
import com.app.sehatin.data.Result
import com.app.sehatin.data.model.Comment
import com.app.sehatin.data.model.Posting
import com.google.firebase.firestore.Query
import java.io.File

class PostViewModel(private val postingRepository: PostingRepository, private val queryProductsByDate: Query): ViewModel() {
    val uploadPostState = MutableLiveData<Result<Map<String, Any?>>>()
    val uploadCommentState = MutableLiveData<Result<Comment>>()
    val getPostState = MutableLiveData<Result<List<Comment>>>()

    fun getPosts() = postingRepository.getPosts(queryProductsByDate).cachedIn(viewModelScope)

    fun uploadPost(postImage: File?, postDescription: String, postTags: List<String>?) = postingRepository.uploadPost(uploadPostState, postImage, postDescription, postTags)

    fun togglePostLike(posting: Posting, isLike: Boolean) = postingRepository.togglePostLike(posting, isLike)

    fun getComments(postId: String) = postingRepository.getComments(getPostState, postId)

    fun uploadComment(postId: String, comment: Comment) = postingRepository.uploadComment(uploadCommentState, postId, comment)

}