package com.app.sehatin.ui.activities.main.fragments.chatList

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.app.sehatin.data.Result
import com.app.sehatin.data.model.HistoryChat
import com.app.sehatin.data.repository.ChatRepository

class ChatListViewModel(private val chatRepository: ChatRepository): ViewModel() {

    val historyChatState = MutableLiveData<Result<List<HistoryChat>>>()

    fun getChatHistory(userId: String) = chatRepository.getUserChatHistory(historyChatState, userId)

}