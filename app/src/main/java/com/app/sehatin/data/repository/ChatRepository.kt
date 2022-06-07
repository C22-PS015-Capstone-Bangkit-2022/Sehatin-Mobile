package com.app.sehatin.data.repository

import android.util.Log
import androidx.lifecycle.MutableLiveData
import com.app.sehatin.data.Result
import com.app.sehatin.data.model.Chat
import com.app.sehatin.data.model.HistoryChat
import com.app.sehatin.injection.Injection
import com.app.sehatin.utils.DateHelper
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener

class ChatRepository {

    private val chatRef = Injection.provideChatReference()
    private val historyChatRef = Injection.provideHistoryChatReference()

    // CHAT
    fun sendChat(userId: String, withUserId: String, message: String, isDoctor: Boolean? = null) {
        Log.d(TAG, "sendChat: $message")
        val userChatRef = chatRef.child(userId).child(withUserId)
        val withUserChatRef = chatRef.child(withUserId).child(userId)
        val key = userChatRef.push().key ?: return
        Log.d(TAG, "sendChat: $key")
        val chat = Chat(
            id = key,
            createdAt = DateHelper.getCurrentDate(),
            sender = userId,
            receiver = withUserId,
            message = message
        )
        //SEND CHAT
        userChatRef.child(key).setValue(chat)
        withUserChatRef.child(key).setValue(chat)
        //UPDATE CHAT HISTORY
        addChatHistory(userId, withUserId, chat, isDoctor)
        addChatHistory(withUserId, userId, chat, isDoctor)
    }

    fun getChat(getChatState: MutableLiveData<Result<List<Chat>>>, userId: String, withUserId: String) {
        getChatState.value = Result.Loading
        chatRef
            .child(userId)
            .child(withUserId)
            .orderByKey()
            .addValueEventListener(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    val chats = mutableListOf<Chat>()
                    for(snap in snapshot.children) {
                        val chat = snap.getValue(Chat::class.java)
                        if(chat != null) {
                            chats.add(chat)
                        }
                    }
                    getChatState.value = Result.Success(chats)
                }
                override fun onCancelled(error: DatabaseError) {
                    getChatState.value = Result.Error(error.message)
                }
            })
    }

    // HISTORY

    private fun addChatHistory(userId: String, withUserId: String, chat: Chat, isDoctor: Boolean? = null) {
        historyChatRef
            .child(userId)
            .child(withUserId)
            .removeValue()
        val historyChat = HistoryChat(
            id = chat.id,
            read = false,
            sender = chat.sender,
            receiver = chat.receiver,
            createdAt = chat.createdAt,
            message = chat.message,
            forDoctor = isDoctor
        )
        historyChatRef
            .child(userId)
            .child(withUserId)
            .setValue(historyChat)
    }

    fun getUserChatHistory(historyChatState: MutableLiveData<Result<List<HistoryChat>>>, userId: String) {
        historyChatState.value = Result.Loading
        historyChatRef.child(userId)
            .orderByChild("id")
            .addValueEventListener(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    Log.d(TAG, "onDataChange : snapshot = $snapshot")
                    val histories = mutableListOf<HistoryChat>()
                    for(snap in snapshot.children) {
                        val history = snap.getValue(HistoryChat::class.java)
                        if (history != null) {
                            history.withUser = snap.key
                            histories.add(history)
                        }
                    }
                    Log.d(TAG, "onDataChange: ${histories.size}")
                    histories.reverse()
                    historyChatState.value = Result.Success(histories)
                }
                override fun onCancelled(error: DatabaseError) {
                    historyChatState.value = Result.Error(error.message)
                }
            })
    }

    private companion object {
        const val TAG = "ChatRepository"
    }

}