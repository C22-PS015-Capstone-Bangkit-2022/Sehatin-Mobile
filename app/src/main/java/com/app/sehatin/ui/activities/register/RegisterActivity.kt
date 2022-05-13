package com.app.sehatin.ui.activities.register

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.app.sehatin.R
import com.app.sehatin.databinding.ActivityRegisterBinding
import com.app.sehatin.utils.Validator

class RegisterActivity : AppCompatActivity() {

    private lateinit var binding: ActivityRegisterBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRegisterBinding.inflate(layoutInflater)
        setContentView(binding.root)
        initVariable()
        initListener()
    }

    private fun initVariable() = with(binding) {

    }

    private fun initListener() = with(binding) {
        loginBtn.setOnClickListener {
            onBackPressed()
        }
        registerBtn.setOnClickListener {
            if(isInputValid()) {
                Intent().apply {
                    val email = emailInputRegister.text.toString()
                    val password = passwordInputRegister.text.toString()
                    putExtra(BioDataActivity.EXTRA_EMAIL, email)
                    putExtra(BioDataActivity.EXTRA_PASSWORD, password)
                    setClass(this@RegisterActivity, BioDataActivity::class.java)
                    startActivity(this)
                }
                finish()
            }
        }
    }

    private fun isInputValid(): Boolean = with(binding) {
        var valid = true
        clearError()

        if(emailInputRegister.text.toString().isEmpty()) {
            emailLayout.error = resources.getString(R.string.error_email_input)
            valid = false
        } else if(!Validator.isEmailValid(emailInputRegister.text.toString())){
            emailLayout.error = resources.getString(R.string.error_email_format)
            valid = false
        }

        if(passwordInputRegister.text.toString().length < 6) {
            passwordLayout.error = resources.getString(R.string.error_password_input)
            valid = false
        }

        val password = passwordInputRegister.text.toString()
        val confirmPassword = confirmPasswordInputRegister.text.toString()
        if(password != confirmPassword) {
            confirmPasswordLayout.error = "Password wrong !"
            valid = false
        }

        return valid
    }

    private fun clearError() = with(binding) {
        emailLayout.apply {
            error = null
            clearFocus()
        }
        passwordLayout.apply {
            error = null
            clearFocus()
        }
        confirmPasswordLayout.apply {
            error = null
            clearFocus()
        }
    }

}