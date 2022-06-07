package com.app.sehatin.ui.activities.main.fragments.sendChat

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.app.sehatin.R
import com.app.sehatin.data.Result
import com.app.sehatin.data.model.User
import com.app.sehatin.databinding.FragmentSendChatBinding
import com.app.sehatin.ui.viewmodel.ViewModelFactory
import com.bumptech.glide.Glide

/**
 * ARGUMENTS :
 *
 * [withUserId] = used for selected user id
 *
 * [isDoctor] = used to check if current user want to chat with default user or doctor
 *
 * [isSessionActive] = used to check if session is active or not
  */
class SendChatFragment : Fragment() {
    private lateinit var binding: FragmentSendChatBinding
    private lateinit var viewModel: SendChatViewModel
    private lateinit var withUserId: String
    private var isDoctor = false
    private var isSessionActive = false
    private val chatAdapter = ChatAdapter()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        binding = FragmentSendChatBinding.inflate(inflater, container, false)
        initVariable()
        initListener()
        return binding.root
    }

    private fun initVariable() = with(binding) {
        viewModel = ViewModelProvider(this@SendChatFragment, ViewModelFactory.getInstance())[SendChatViewModel::class.java]
        withUserId = SendChatFragmentArgs.fromBundle(arguments as Bundle).withUserId
        isDoctor = SendChatFragmentArgs.fromBundle(arguments as Bundle).isDoctor
        isSessionActive = SendChatFragmentArgs.fromBundle(arguments as Bundle).isSessionActive
        rvChat.setHasFixedSize(true)
        val layoutManager = LinearLayoutManager(requireContext())
        layoutManager.stackFromEnd = true
        layoutManager.reverseLayout = false
        rvChat.layoutManager = layoutManager
        rvChat.adapter = chatAdapter
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
                    chatAdapter.submitList(it.data)
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
            userId?.let { it1 -> sendMessage(it1) }
        }

        toolbar.setNavigationOnClickListener {
            requireActivity().onBackPressed()
        }
    }

    private fun sendMessage(userId: String) = with(binding) {
        val message = chatInput.text.toString()
        if(!isDoctor) {
            send(userId, message)
        } else {
            if(isSessionActive) {
                send(userId, message, true)
            } else {
                Toast.makeText(requireContext(), "Sesi dengan dokter ini tidak aktif", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun send(userId: String, message: String, isDoctor: Boolean? = null) = with(binding) {
        if(message.trim().isNotEmpty()) {
            viewModel.sendChat(userId, withUserId, message, isDoctor)
            chatInput.text = null
        } else {
            Toast.makeText(requireContext(), "Ketik sesuatu", Toast.LENGTH_SHORT).show()
        }
    }

    private companion object {
        const val TAG = "SendChatFragment"
    }

}