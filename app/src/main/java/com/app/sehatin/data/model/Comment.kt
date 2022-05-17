package com.app.sehatin.data.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class Comment(
    var id: String,
    var postId: String,
    var userId: String,
    var comment: String,
    var createdAt: String,
) : Parcelable