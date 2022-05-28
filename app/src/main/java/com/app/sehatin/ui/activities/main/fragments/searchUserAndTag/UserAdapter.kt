package com.app.sehatin.ui.activities.main.fragments.searchUserAndTag

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.app.sehatin.R
import com.app.sehatin.data.model.User
import com.app.sehatin.databinding.ItemUserBinding
import com.bumptech.glide.Glide

class UserAdapter(private val onClick: (User) -> Unit): ListAdapter<User, UserAdapter.Holder>(DIFF_CALLBACK){

    class Holder(private val binding: ItemUserBinding): RecyclerView.ViewHolder(binding.root) {
        fun bind(user: User) = with(binding) {
            Glide.with(this.root)
                .load(user.imageUrl)
                .placeholder(R.drawable.user_default)
                .error(R.drawable.user_default)
                .into(userImageIV)
            usernameTv.text = user.username
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Holder {
        val binding = ItemUserBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return Holder(binding)
    }

    override fun onBindViewHolder(holder: Holder, position: Int) {
        val user = getItem(position)
        holder.bind(user)
    }

    companion object {
        val DIFF_CALLBACK: DiffUtil.ItemCallback<User> =
            object : DiffUtil.ItemCallback<User>() {
                override fun areItemsTheSame(oldUser: User, newUser: User): Boolean {
                    return oldUser.id == newUser.id
                }

                @SuppressLint("DiffUtilEquals")
                override fun areContentsTheSame(oldUser: User, newUser: User): Boolean {
                    return oldUser == newUser
                }
            }
    }

}