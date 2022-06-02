package com.app.sehatin.ui.activities.main.fragments.chatList

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.app.sehatin.R
import com.app.sehatin.data.model.HistoryChat
import com.app.sehatin.data.model.User
import com.app.sehatin.databinding.ItemHistoryChatBinding
import com.app.sehatin.utils.USER_COLLECTION
import com.bumptech.glide.Glide
import com.google.firebase.firestore.FirebaseFirestore

class HistoryChatAdapter: ListAdapter<HistoryChat, HistoryChatAdapter.Holder>(DIFF_CALLBACK) {

    inner class Holder(private val binding: ItemHistoryChatBinding): RecyclerView.ViewHolder(binding.root) {
        fun bind(historyChat: HistoryChat) = with(binding) {
            getWithUserData(historyChat.withUser)
            message.text = historyChat.message
        }

        private fun getWithUserData(withUserId: String) = with(binding) {
            FirebaseFirestore.getInstance()
                .collection(USER_COLLECTION)
                .document(withUserId)
                .get()
                .addOnSuccessListener {
                    val user = it.toObject(User::class.java)
                    user?.let { userData ->
                        Glide.with(this.root)
                            .load(userData.imageUrl)
                            .placeholder(R.drawable.user_default)
                            .error(R.drawable.user_default)
                            .into(userImage)
                        usernameTv.text = userData.username
                    }
                }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Holder {
        val binding = ItemHistoryChatBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return Holder(binding)
    }

    override fun onBindViewHolder(holder: Holder, position: Int) {
        holder.bind(getItem(position))
    }

    companion object {
        val DIFF_CALLBACK: DiffUtil.ItemCallback<HistoryChat> =
            object : DiffUtil.ItemCallback<HistoryChat>() {
                override fun areItemsTheSame(oldUser: HistoryChat, newUser: HistoryChat): Boolean {
                    return oldUser.id == newUser.id
                }

                @SuppressLint("DiffUtilEquals")
                override fun areContentsTheSame(oldUser: HistoryChat, newUser: HistoryChat): Boolean {
                    return oldUser == newUser
                }
            }
    }

}