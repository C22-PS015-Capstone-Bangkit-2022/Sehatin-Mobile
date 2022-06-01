package com.app.sehatin.data.repository

import androidx.lifecycle.MutableLiveData
import com.app.sehatin.data.Result
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
    private val historyChatRef = Firebase.database.reference.child(HISTORY_CHAT_REFERENCE)

    fun getUserChatHistory(historyChatState: MutableLiveData<Result<List<HistoryChat>>>, userId: String) {
        historyChatState.value = Result.Loading
        historyChatRef.child(userId)
            .orderByChild(HistoryChat.CREATED_AT)
            .addValueEventListener(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    val histories = mutableListOf<HistoryChat>()
                    for(snap in snapshot.children) {
                        val history = snap.getValue(HistoryChat::class.java)
                        if (history != null) {
                            histories.add(history)
                        }
                    }
                    historyChatState.value = Result.Success(histories)
                }
                override fun onCancelled(error: DatabaseError) {
                    historyChatState.value = Result.Error(error.message)
                }
            })
    }

}