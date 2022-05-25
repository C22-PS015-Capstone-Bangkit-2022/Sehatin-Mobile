package com.app.sehatin.data.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class User(
    var id: String? = null,
    var username: String? = null,
    var email: String? = null,
    var dateOfBirth: String? = null,
    var imageUrl: String? = null,
    var gender: Int? = null,
    var diseases: List<String>? = null
) : Parcelable {
    companion object {
        var currentUser: User? = null
        const val USERNAME = "username"
        const val EMAIL = "email"
        const val DATE_OF_BIRTH = "dateOfBirth"
        const val IMAGE_URL = "imageUrl"
        const val GENDER = "gender"
        const val DISEASES = "diseases"
        const val ID = "id"
    }
}