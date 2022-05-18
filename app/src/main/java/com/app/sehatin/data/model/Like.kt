package com.app.sehatin.data.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class Like(
    var userId: String,
    var postId: String,
    var createAt: String,
) : Parcelable