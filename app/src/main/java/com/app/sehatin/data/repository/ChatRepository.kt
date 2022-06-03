package com.app.sehatin.data.repository

import android.util.Log
import androidx.lifecycle.MutableLiveData
import com.app.sehatin.data.Result
import com.app.sehatin.data.model.Chat
import com.app.sehatin.data.model.HistoryChat
import com.app.sehatin.utils.CHAT_REFERENCE
import com.app.sehatin.utils.DateHelper
import com.app.sehatin.utils.HISTORY_CHAT_REFERENCE
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase

class ChatRepository {
    private val DATABASE_URL = "https://sehatin-eab72-default-rtdb.asia-southeast1.firebasedatabase.app/"
    private val chatRef = Firebase.database(DATABASE_URL).reference.child(CHAT_REFERENCE)
    private val historyChatRef = Firebase.database(DATABASE_URL).reference.child(HISTORY_CHAT_REFERENCE)

    // CHAT

    fun sendChat(userId: String, withUserId: String, message: String) {
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
        addChatHistory(userId, withUserId, chat)
        addChatHistory(withUserId, userId, chat)
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

    private fun addChatHistory(userId: String, withUserId: String, chat: Chat) {
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
            message = chat.message
        )
        historyChatRef
            .child(userId)
            .child(withUserId)
            .setValue(historyChat)
    }

    fun getUserChatHistory(historyChatState: MutableLiveData<Result<List<HistoryChat>>>, userId: String) {
        historyChatState.value = Result.Loading
        historyChatRef.child(userId)
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
                    historyChatState.value = Result.Success(histories)
                }
                override fun onCancelled(error: DatabaseError) {
                    historyChatState.value = Result.Error(error.message)
                }
            })
    }

    private val historyDummy = listOf(
        HistoryChat(
            "123",
            "123123123",
            "X5YIVVUgxWgNkqUv82Ju3K1c2F43",
            "CoVR2i2uM9YbYqdwQ4CzTMW5fuJ3",
            "asdasd",
            "CoVR2i2uM9YbYqdwQ4CzTMW5fuJ3"
        ),
        HistoryChat(
            "123",
            "123123123",
            "X5YIVVUgxWgNkqUv82Ju3K1c2F43",
            "CoVR2i2uM9YbYqdwQ4CzTMW5fuJ3",
            "asdasd",
            "CoVR2i2uM9YbYqdwQ4CzTMW5fuJ3"
        ),
        HistoryChat(
            "123",
            "123123123",
            "X5YIVVUgxWgNkqUv82Ju3K1c2F43",
            "CoVR2i2uM9YbYqdwQ4CzTMW5fuJ3",
            "asdasd",
            "CoVR2i2uM9YbYqdwQ4CzTMW5fuJ3"
        )
    )

    private companion object {
        const val TAG = "ChatRepository"
    }

}