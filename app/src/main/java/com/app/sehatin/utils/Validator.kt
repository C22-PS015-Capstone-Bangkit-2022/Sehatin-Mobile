package com.app.sehatin.utils



object Validator {

    private const val emailPattern = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+"
    fun isEmailValid(email: String) : Boolean {
        return email.matches(emailPattern.toRegex())
    }

}