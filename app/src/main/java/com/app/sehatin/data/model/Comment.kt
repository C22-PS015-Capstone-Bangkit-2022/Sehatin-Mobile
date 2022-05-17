package com.app.sehatin.data.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class Comment(
    var id: String? = null,
    var postId: String? = null,
    var userId: String? = null,
    var comment: String? = null,
    var createdAt: String? = null,
) : Parcelable