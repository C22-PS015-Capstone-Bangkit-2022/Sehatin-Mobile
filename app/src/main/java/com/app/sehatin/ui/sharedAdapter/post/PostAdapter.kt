package com.app.sehatin.ui.sharedAdapter.post

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import com.app.sehatin.data.model.Posting
import com.app.sehatin.databinding.ItemPostBinding

class PostAdapter: ListAdapter<Posting, PostViewHolder>(DIFF_CALLBACK) {
    private lateinit var onClickListener: PostListener
    private lateinit var context: Context

    fun setListener(onClickListener: PostListener) {
        this.onClickListener = onClickListener
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PostViewHolder {
        context = parent.context
        val binding = ItemPostBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return PostViewHolder(binding, context, onClickListener)
    }

    override fun onBindViewHolder(holder: PostViewHolder, position: Int) {
        val posting = getItem(position)
        holder.bind(posting)
        holder.setListener(posting)
    }

    companion object {
        val DIFF_CALLBACK: DiffUtil.ItemCallback<Posting> =
            object : DiffUtil.ItemCallback<Posting>() {
                override fun areItemsTheSame(oldUser: Posting, newUser: Posting): Boolean {
                    return oldUser.id == newUser.id
                }

                @SuppressLint("DiffUtilEquals")
                override fun areContentsTheSame(oldUser: Posting, newUser: Posting): Boolean {
                    return oldUser == newUser
                }
            }
    }

}