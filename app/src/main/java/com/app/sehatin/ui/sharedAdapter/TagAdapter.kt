package com.app.sehatin.ui.sharedAdapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.app.sehatin.databinding.ItemHomeTopHeroTagBinding
import com.app.sehatin.utils.capital
import java.util.*

class TagAdapter(private val tags: List<String>): RecyclerView.Adapter<TagAdapter.Holder>() {
    private lateinit var binding: ItemHomeTopHeroTagBinding

    inner class Holder(binding: ItemHomeTopHeroTagBinding): RecyclerView.ViewHolder(binding.root) {
        fun bind(tag: String) = with(binding) {
            tagTitle.text = tag.capital()
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Holder {
        binding = ItemHomeTopHeroTagBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return Holder(binding)
    }

    override fun onBindViewHolder(holder: Holder, position: Int) {
        holder.bind(tags[position])
    }

    override fun getItemCount(): Int = tags.size

}