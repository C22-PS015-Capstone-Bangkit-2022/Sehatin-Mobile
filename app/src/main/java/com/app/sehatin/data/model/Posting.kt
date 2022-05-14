package com.app.sehatin.data.model

data class Posting(
    var id:  String? = null,
    var userId: String? = null,
    var createdAt: String? = null,
    var hasImage: Boolean? = null,
    var image: String? = null,
    var description: String? = null,
    var tags: List<String>? = null,
    var likes: Int? = null,
    var comment: List<Comment>? = null,

    var isLiked: Boolean = false,
    var isCommented: Boolean = false
)