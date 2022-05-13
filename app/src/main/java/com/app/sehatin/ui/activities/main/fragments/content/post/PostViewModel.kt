package com.app.sehatin.ui.activities.main.fragments.content.post

import androidx.lifecycle.ViewModel
import com.app.sehatin.data.repository.PostingRepository

class PostViewModel(private val postingRepository: PostingRepository): ViewModel() {

    fun getPosts() = postingRepository.getPosts()

}