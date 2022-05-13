package com.app.sehatin.data.model

data class User(
    var id: String,
    var username: String,
    var email: String,
    var dateOfBirth: String,
    var imageUrl: String,
    var gender: Int,
    var diseases: List<Disease>?
) {
    companion object {
        var currentUser: User? = null
        const val USERNAME = "username"
        const val EMAIL = "email"
        const val DATE_OF_BIRTH = "dateOfBirth"
        const val IMAGE_URL = "imageUrl"
        const val GENDER = "gender"
        const val DISEASES = "diseases"
    }
}