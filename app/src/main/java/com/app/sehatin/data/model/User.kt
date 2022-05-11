package com.app.sehatin.data.model

data class User(
    var id: String,
    var username: String,
    var email: String,
    var imageUrl: String,
    var diseases: List<Disease>
) {
    companion object {
        var currentUser: User? = null
    }
}