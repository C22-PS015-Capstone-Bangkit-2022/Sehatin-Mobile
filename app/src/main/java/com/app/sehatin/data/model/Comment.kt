package com.app.sehatin.data.model

data class Comment(
    var id: String,
    var postId: String,
    var userId: String,
    var comment: String,
    var createdAt: String,
)