package com.app.sehatin.data.model

data class Posting(
    var id:  String,
    var userId: String,
    var createdAt: String,
    var image: String,
    var description: String,
    var tags: List<String>,
    var likes: Int,
    var comment: List<Comment>
)