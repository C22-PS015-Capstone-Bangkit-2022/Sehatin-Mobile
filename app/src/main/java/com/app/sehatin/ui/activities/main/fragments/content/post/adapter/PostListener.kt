package com.app.sehatin.ui.activities.main.fragments.content.post.adapter

import android.widget.ImageView
import android.widget.TextView
import com.app.sehatin.data.model.Posting

interface PostListener {
    fun onLikeClick(posting: Posting, position: Int)
    fun onUnlikeClick(posting: Posting, position: Int)
    fun onCommentClick(posting: Posting, commentBtn: ImageView, commentCount: TextView)
    fun onBookmarkClick(posting: Posting, bookmarkBtn: ImageView, position: Int)
    fun onImageClick(posting: Posting)
}