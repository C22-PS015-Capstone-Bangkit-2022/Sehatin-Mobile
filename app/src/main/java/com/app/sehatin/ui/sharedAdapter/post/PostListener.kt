package com.app.sehatin.ui.sharedAdapter.post

import android.widget.ImageView
import android.widget.TextView
import com.app.sehatin.data.model.Posting
import com.app.sehatin.data.model.User

interface PostListener {
    fun onLikeClick(posting: Posting, position: Int)
    fun onUnlikeClick(posting: Posting, position: Int)
    fun onCommentClick(posting: Posting, commentBtn: ImageView, commentCount: TextView)
    fun onImageClick(posting: Posting)
    fun onUserInfoClick(user: User)
}