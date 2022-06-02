package com.app.sehatin.ui.activities.main.fragments.chatList

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
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
        historyChatAdapter = HistoryChatAdapter {
            Log.d(TAG, "historyChatAdapter: $it")
            val direction = ChatListFragmentDirections.actionChatListFragmentToSendChatFragment(it)
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
                    Log.d(TAG, "historyChatState: Loading")
                }
                is Result.Error -> {
                    Log.d(TAG, "historyChatState: Error = ${it.error}")
                }
                is Result.Success -> {
                    Log.d(TAG, "historyChatState: success = ${it.data}")
                    historyChatAdapter.submitList(it.data)
                }
            }
        }
    }

    private companion object {
        const val TAG = "ChatListFragment"
    }

}