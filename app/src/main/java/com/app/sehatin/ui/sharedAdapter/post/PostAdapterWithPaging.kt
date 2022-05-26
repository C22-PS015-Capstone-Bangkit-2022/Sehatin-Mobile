package com.app.sehatin.ui.sharedAdapter.post

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import com.app.sehatin.data.model.Posting
import com.app.sehatin.databinding.ItemPostBinding

class PostAdapterWithPaging : PagingDataAdapter<Posting, PostViewHolder>(Companion) {
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
        Log.d("PostAdapter", "onBindViewHolder: $position")
        val post = getItem(position) ?: return
        holder.bind(post)
        holder.setListener(post)
    }

    companion object : DiffUtil.ItemCallback<Posting>() {
        override fun areItemsTheSame(oldItem: Posting, newItem: Posting): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: Posting, newItem: Posting): Boolean {
            return oldItem == newItem
        }
    }

}