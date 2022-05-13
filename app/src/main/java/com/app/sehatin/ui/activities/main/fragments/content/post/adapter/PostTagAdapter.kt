package com.app.sehatin.ui.activities.main.fragments.content.post.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.app.sehatin.databinding.ItemPostTagBinding

class PostTagAdapter(private val tags: List<String>): RecyclerView.Adapter<PostTagAdapter.Holder>() {
    private lateinit var binding: ItemPostTagBinding

    inner class Holder(binding: ItemPostTagBinding): RecyclerView.ViewHolder(binding.root) {
        fun bind(tag: String) = with(binding) {
            tagTitle.text = tag
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Holder {
        binding = ItemPostTagBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return Holder(binding)
    }

    override fun onBindViewHolder(holder: Holder, position: Int) {
        holder.bind(tags[position])
    }

    override fun getItemCount(): Int = tags.size

}