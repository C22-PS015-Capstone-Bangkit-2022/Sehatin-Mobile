package com.app.sehatin.ui.activities.main.fragments.sendChat

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import com.app.sehatin.R
import com.app.sehatin.data.Result
import com.app.sehatin.data.model.User
import com.app.sehatin.databinding.FragmentSendChatBinding
import com.app.sehatin.ui.viewmodel.ViewModelFactory
import com.bumptech.glide.Glide

class SendChatFragment : Fragment() {
    private lateinit var binding: FragmentSendChatBinding
    private lateinit var viewModel: SendChatViewModel
    private lateinit var withUserId: String

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        binding = FragmentSendChatBinding.inflate(inflater, container, false)
        initVariable()
        initListener()
        return binding.root
    }

    private fun initVariable() {
        viewModel = ViewModelProvider(this, ViewModelFactory.getInstance())[SendChatViewModel::class.java]
        withUserId = SendChatFragmentArgs.fromBundle(arguments as Bundle).withUserId
    }

    private fun initListener() = with(binding) {
        val userId = User.currentUser.id
        userId?.let { viewModel.getChat(userId, withUserId) }
        viewModel.getUserData(withUserId)

        viewModel.getChatState.observe(viewLifecycleOwner) {
            when(it) {
                is Result.Loading -> {
                    Log.d(TAG, "getChatState: loading")
                }
                is Result.Error -> {
                    Log.e(TAG, "getChatState: error = ${it.error}")
                }
                is Result.Success -> {
                    Log.d(TAG, "getChatState: success = ${it.data}")
                }
            }
        }

        viewModel.getUserState.observe(viewLifecycleOwner) {
            when(it) {
                is Result.Loading -> {
                    Log.d(TAG, "getChatState: loading")
                }
                is Result.Error -> {
                    Log.e(TAG, "getChatState: error = ${it.error}")
                }
                is Result.Success -> {
                    val user = it.data
                    user?.let { data ->
                        usernameTv.text = data.username
                        Glide.with(this@SendChatFragment)
                            .load(data.imageUrl)
                            .placeholder(R.drawable.user_default)
                            .error(R.drawable.user_default)
                            .into(userImageIV)
                    }
                }
            }
        }

        sendBtn.setOnClickListener {
            val message = chatInput.text.toString()
            if(message.trim().isNotEmpty() && userId != null) {
                viewModel.sendChat(userId, withUserId, message)
                chatInput.text = null
            } else {
                Toast.makeText(requireContext(), "Ketik sesuatu", Toast.LENGTH_SHORT).show()
            }
        }

        toolbar.setNavigationOnClickListener {
            requireActivity().onBackPressed()
        }
    }

    private companion object {
        const val TAG = "SendChatFragment"
    }

}