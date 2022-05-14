package com.app.sehatin.ui.activities.start.fragments.login

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.app.sehatin.R
import com.app.sehatin.data.Result
import com.app.sehatin.databinding.FragmentLoginBinding
import com.app.sehatin.ui.activities.main.MainActivity
import com.app.sehatin.ui.activities.start.fragments.AuthenticationViewModel
import com.app.sehatin.ui.viewmodel.ViewModelFactory
import com.app.sehatin.utils.Validator

class LoginFragment : Fragment() {
    private lateinit var binding: FragmentLoginBinding
    private lateinit var authenticationViewModel: AuthenticationViewModel

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = FragmentLoginBinding.inflate(inflater, container, false)
        initVariable()
        initListener()
        return binding.root
    }

    private fun initVariable() {
        authenticationViewModel = ViewModelProvider(this, ViewModelFactory.getInstance())[AuthenticationViewModel::class.java]
    }

    private fun initListener() = with(binding) {
        registerBtn.setOnClickListener {
            findNavController().navigate(R.id.action_loginFragment_to_accountFragment)
        }

        loginBtn.setOnClickListener {
            if(isInputClear()) {
                authenticationViewModel.login(emailInputLogin.text.toString(), passwordInputLogin.text.toString())
            }
        }

        authenticationViewModel.loginState.observe(viewLifecycleOwner) {
            when(it) {
                is Result.Loading -> {
                    showLoading(true)
                }
                is Result.Error -> {
                    showLoading(false)
                    Toast.makeText(requireContext(), it.error, Toast.LENGTH_SHORT).show()
                }
                is Result.Success -> {
                    startActivity(Intent(requireActivity(), MainActivity::class.java))
                    requireActivity().finish()
                }
            }
        }
    }

    private fun showLoading(isLoading: Boolean) = with(binding) {
        if(isLoading) {
            loginBtn.visibility = View.GONE
            progressBar.progressBtn.visibility = View.VISIBLE
        } else {
            loginBtn.visibility = View.VISIBLE
            progressBar.progressBtn.visibility = View.GONE
        }
    }

    private fun isInputClear() : Boolean = with(binding) {
        var valid = true
        clearError()

        if(emailInputLogin.text.toString().isEmpty()) {
            emailLayout.error = resources.getString(R.string.error_email_input)
            valid = false
        } else if(!Validator.isEmailValid(emailInputLogin.text.toString())){
            emailLayout.error = resources.getString(R.string.error_email_format)
            valid = false
        }

        if(passwordInputLogin.text.toString().length < 6) {
            passwordLayout.error = resources.getString(R.string.error_password_input)
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
    }

    private companion object {
        const val TAG = "LoginFragment"
    }

}