package com.app.sehatin.data.model

data class Posting(
    var id:  String? = null,
    var userId: String? = null,
    var createdAt: String? = null,
    var hasImage: Boolean = false,
    var image: String? = null,
    var description: String? = null,
    var tags: List<String>? = null,
    var comment: List<Comment>? = null,
    var commentCount: Int = 0,
    var likeCount: Int = 0,
)