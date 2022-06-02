package com.app.sehatin.ui.activities.main.fragments.sendChat

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.app.sehatin.data.Result
import com.app.sehatin.data.model.Chat
import com.app.sehatin.data.repository.ChatRepository

class SendChatViewModel(private val chatRepository: ChatRepository) : ViewModel(){

    val getChatState = MutableLiveData<Result<List<Chat>>>()

    fun getChat(userId: String, withUserId: String) = chatRepository.getChat(getChatState, userId, withUserId)

    fun sendChat(userId: String, withUserId: String, message: String) = chatRepository.sendChat(userId, withUserId, message)

}