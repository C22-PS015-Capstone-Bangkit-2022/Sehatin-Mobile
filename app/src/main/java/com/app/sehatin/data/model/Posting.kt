package com.app.sehatin.data.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class Posting(
    var id:  String? = null,
    var userId: String? = null,
    var createdAt: String? = null,
    var hasImage: Boolean = false,
    var image: String? = null,
    var description: String? = null,
    var tags: List<String>? = null,
    var comments: List<Comment>? = null,
    var commentCount: Int = 0,
    var likes: List<Like>? = null,
    var likeCount: Int = 0,
) : Parcelable