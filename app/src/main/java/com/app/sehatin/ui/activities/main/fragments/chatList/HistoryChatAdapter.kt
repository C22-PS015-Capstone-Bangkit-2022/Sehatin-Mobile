package com.app.sehatin.ui.activities.main.fragments.chatList

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.app.sehatin.R
import com.app.sehatin.data.model.Doctor
import com.app.sehatin.data.model.HistoryChat
import com.app.sehatin.data.model.User
import com.app.sehatin.databinding.ItemHistoryChatBinding
import com.app.sehatin.injection.Injection
import com.bumptech.glide.Glide

class HistoryChatAdapter(private val onClick: (String, Boolean?, Boolean) -> Unit): ListAdapter<HistoryChat, HistoryChatAdapter.Holder>(DIFF_CALLBACK) {
    private val userRef = Injection.provideUserCollection()
    private val doctorRef = Injection.provideDoctorCollection()

    inner class Holder(private val binding: ItemHistoryChatBinding): RecyclerView.ViewHolder(binding.root) {
        fun bind(historyChat: HistoryChat) = with(binding) {
            historyChat.withUser?.let {
                getWithUserData(it, historyChat.forDoctor)
            }
            message.text = historyChat.message
            this.root.setOnClickListener {
                historyChat.withUser?.let { it1 ->
                    onClick(it1, historyChat.forDoctor, false)
                }
            }
        }

        private fun getWithUserData(withUserId: String, forDoctor: Boolean? = null) = with(binding) {
            if(forDoctor != null && forDoctor) {
                doctorRef
                    .document(withUserId)
                    .get()
                    .addOnSuccessListener {
                        val doctor = it.toObject(Doctor::class.java)
                        doctor?.let { doctorData ->
                            Glide.with(this.root)
                                .load(doctorData.imageUrl)
                                .placeholder(R.drawable.user_default)
                                .error(R.drawable.user_default)
                                .into(userImage)
                            usernameTv.text = doctorData.name
                        }
                    }
            } else {
                userRef
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