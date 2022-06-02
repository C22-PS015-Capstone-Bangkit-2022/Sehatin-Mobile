package com.app.sehatin.data.repository

import android.util.Log
import androidx.lifecycle.MutableLiveData
import com.app.sehatin.data.Result
import com.app.sehatin.data.model.Chat
import com.app.sehatin.data.model.HistoryChat
import com.app.sehatin.utils.CHAT_REFERENCE
import com.app.sehatin.utils.HISTORY_CHAT_REFERENCE
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase

class ChatRepository {
    private val chatRef = Firebase.database.reference.child(CHAT_REFERENCE)
    private val historyChatRef = Firebase.database.getReference(HISTORY_CHAT_REFERENCE)

    //SEND CHAT

    fun sendChat(userId: String, withUserId: String, chat: Chat) {
        chatRef
            .child(userId)
            .child(withUserId)
            .setValue(chat)
    }

    fun getChat(getChatState: MutableLiveData<Result<List<Chat>>>, userId: String, withUserId: String) {
        getChatState.value = Result.Loading
        chatRef
            .child(userId)
            .child(withUserId)
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

    // CHAT HISTORY

    fun addChatHistory(userId: String, historyChat: HistoryChat) {
        historyChatRef
            .child(userId)

            .setValue(historyChat)
    }

    fun getUserChatHistory(historyChatState: MutableLiveData<Result<List<HistoryChat>>>, userId: String) {
        historyChatState.value = Result.Loading
        historyChatState.value = Result.Success(historyDummy)
//        historyChatRef.child(userId)
//            .addValueEventListener(object : ValueEventListener {
//                override fun onDataChange(snapshot: DataSnapshot) {
//                    Log.d(TAG, "getUserChatHistory : snapshot = $snapshot")
//                    val histories = mutableListOf<HistoryChat>()
//                    for(snap in snapshot.children) {
//                        val history = snap.getValue(HistoryChat::class.java)
//                        if (history != null) {
//                            histories.add(history)
//                        }
//                    }
//                    historyChatState.value = Result.Success(histories)
//                }
//                override fun onCancelled(error: DatabaseError) {
//                    historyChatState.value = Result.Error(error.message)
//                }
//            })
    }

    private val historyDummy = listOf(
        HistoryChat(
            "123",
            "1231231",
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