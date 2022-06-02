package com.app.sehatin.ui.activities.main.fragments.sendChat

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.app.sehatin.data.model.Chat
import com.app.sehatin.data.model.User
import com.app.sehatin.databinding.ItemChatBinding

class ChatAdapter: ListAdapter<Chat, ChatAdapter.Holder>(DIFF_CALLBACK) {

    inner class Holder(private val binding: ItemChatBinding): RecyclerView.ViewHolder(binding.root) {
        fun bind(chat: Chat) = with(binding) {
            val currentUserId = User.currentUser.id
            currentUserId?.let { userId ->
                val senderId = chat.sender
                if(senderId != null) {
                    if(senderId == userId) {
                        //RIGHT
                        chatRight.visibility = View.VISIBLE
                        chatLeft.visibility = View.GONE
                        messageTextLeft.text = chat.message
                    } else {
                        //LEFT
                        chatRight.visibility = View.GONE
                        chatLeft.visibility = View.VISIBLE
                        messageTextRight.text = chat.message
                    }
                }
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Holder {
        val view = ItemChatBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return Holder(view)
    }

    override fun onBindViewHolder(holder: Holder, position: Int) {
        holder.bind(getItem(position))
    }

    companion object {
        val DIFF_CALLBACK: DiffUtil.ItemCallback<Chat> =
            object : DiffUtil.ItemCallback<Chat>() {
                override fun areItemsTheSame(oldUser: Chat, newUser: Chat): Boolean {
                    return oldUser.id == newUser.id
                }

                @SuppressLint("DiffUtilEquals")
                override fun areContentsTheSame(oldUser: Chat, newUser: Chat): Boolean {
                    return oldUser == newUser
                }
            }
    }


}