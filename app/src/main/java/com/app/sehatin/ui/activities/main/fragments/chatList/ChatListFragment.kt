package com.app.sehatin.ui.activities.main.fragments.chatList

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.app.sehatin.data.Result
import com.app.sehatin.data.model.User
import com.app.sehatin.databinding.FragmentChatListBinding
import com.app.sehatin.ui.viewmodel.ViewModelFactory

class ChatListFragment : Fragment() {
    private lateinit var binding: FragmentChatListBinding
    private lateinit var viewModel: ChatListViewModel
    private lateinit var historyChatAdapter: HistoryChatAdapter

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        binding = FragmentChatListBinding.inflate(inflater, container, false)
        initVariable()
        initListener()
        return binding.root
    }

    private fun initVariable() = with(binding) {
        viewModel = ViewModelProvider(this@ChatListFragment, ViewModelFactory.getInstance())[ChatListViewModel::class.java]
        User.currentUser.id?.let { viewModel.getChatHistory(it) }
        historyChatAdapter = HistoryChatAdapter { userId, isDoctor, isActive ->
            val direction = ChatListFragmentDirections.actionChatListFragmentToSendChatFragment(userId)
            if (isDoctor != null) {
                direction.isDoctor = isDoctor
                direction.isSessionActive = isActive
            }
            findNavController().navigate(direction)
        }
        rvChatList.setHasFixedSize(true)
        rvChatList.layoutManager = LinearLayoutManager(requireContext())
        rvChatList.adapter = historyChatAdapter
    }

    private fun initListener() = with(binding) {
        toolbar.setNavigationOnClickListener {
            requireActivity().onBackPressed()
        }
        viewModel.historyChatState.observe(viewLifecycleOwner) {
            when(it) {
                is Result.Loading -> {
                    showLoading(true)
                }
                is Result.Error -> {
                    showLoading(false)
                    Toast.makeText(requireContext(), it.error, Toast.LENGTH_SHORT).show()
                }
                is Result.Success -> {
                    showLoading(false)
                    if(it.data.isNotEmpty()) {
                        showEmptyInfo(false)
                        historyChatAdapter.submitList(it.data)
                    } else {
                        showEmptyInfo(true)
                    }
                }
            }
        }
    }

    private fun showLoading(isLoading: Boolean) = with(binding) {
        if(isLoading) {
            progressBar.visibility = View.VISIBLE
            rvChatList.visibility = View.GONE
        } else {
            progressBar.visibility = View.GONE
            rvChatList.visibility = View.VISIBLE
        }
    }

    private fun showEmptyInfo(isEmpty: Boolean) = with(binding) {
        if(isEmpty) {
            rvChatList.visibility = View.GONE
            emptyInfo.visibility = View.VISIBLE
        } else {
            rvChatList.visibility = View.VISIBLE
            emptyInfo.visibility = View.GONE
        }
    }

}