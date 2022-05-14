package com.app.sehatin.ui.activities.main.fragments.content.post

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.app.sehatin.data.repository.PostingRepository
import com.app.sehatin.data.Result
import com.app.sehatin.data.model.Posting
import java.io.File

class PostViewModel(private val postingRepository: PostingRepository): ViewModel() {
    val uploadPostState = MutableLiveData<Result<Map<String, Any?>>>()
    val getPostState = MutableLiveData<Result<List<Posting>>>()

    fun getPosts() = postingRepository.getPosts(getPostState)

    fun uploadPost(postImage: File?, postDescription: String, postTags: List<String>?) = postingRepository.uploadPost(uploadPostState, postImage, postDescription, postTags)

}