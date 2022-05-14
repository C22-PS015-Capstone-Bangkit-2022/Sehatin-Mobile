package com.app.sehatin.ui.activities.start.fragments.register

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import com.app.sehatin.R
import com.app.sehatin.databinding.FragmentAccountBinding
import com.app.sehatin.utils.Validator

class AccountFragment : Fragment() {
    private lateinit var binding: FragmentAccountBinding

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        binding = FragmentAccountBinding.inflate(inflater, container, false)
        initListener()
        return binding.root
    }

    private fun initListener() = with(binding) {
        loginBtn.setOnClickListener {
            requireActivity().onBackPressed()
        }
        registerBtn.setOnClickListener {
            if(isInputValid()) {
                val email = emailInputRegister.text.toString()
                val password = passwordInputRegister.text.toString()
                val toBioDataFragment = AccountFragmentDirections.actionAccountFragmentToBioDataFragment(email, password)
                findNavController().navigate(toBioDataFragment)
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