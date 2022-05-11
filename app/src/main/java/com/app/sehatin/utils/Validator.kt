package com.app.sehatin.utils

import android.widget.EditText

object Validator {

    fun isAllFormFilled(args: Array<EditText>): Boolean  {
        for(editText in args) {
            if(editText.text.toString().isEmpty()) {
                return false
            }
        }
        return true
    }

    fun isFormValid(args: Array<EditText>): Boolean  {
        for(editText in args) {
            if(editText.error != null) {
                return false
            }
        }
        return true
    }

    private const val emailPattern = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+"
    fun isEmailValid(email: String) : Boolean {
        return email.matches(emailPattern.toRegex())
    }

}