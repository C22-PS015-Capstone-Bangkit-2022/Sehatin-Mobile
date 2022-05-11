package com.app.sehatin.data.remote.response

import com.app.sehatin.data.model.User

data class LoginResponse(
    var error: Boolean,
    var messsage: String,
    var user: User?
)