package com.app.sehatin.ui.activities.main.fragments.content.home.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.app.sehatin.data.model.Posting
import com.app.sehatin.databinding.ItemPostHorizontalBinding

class HorizontalPostAdapter(private val posts: List<Posting>): RecyclerView.Adapter<HorizontalPostAdapter.Holder>() {
    private lateinit var binding: ItemPostHorizontalBinding

    inner class Holder(private val binding: ItemPostHorizontalBinding): RecyclerView.ViewHolder(binding.root) {
        fun bind(posting: Posting) = with(binding) {

        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Holder {
        binding = ItemPostHorizontalBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return Holder(binding)
    }

    override fun onBindViewHolder(holder: Holder, position: Int) {
        holder.bind(posts[position])
    }

    override fun getItemCount(): Int = posts.size

}