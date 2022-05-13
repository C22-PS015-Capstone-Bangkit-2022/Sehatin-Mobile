package com.app.sehatin.ui.activities.main.fragments.content.home.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.app.sehatin.data.model.Article
import com.app.sehatin.databinding.ItemHomeTopHeroBinding
import com.app.sehatin.ui.sharedAdapter.TagAdapter
import com.bumptech.glide.Glide

class HeroAdapter(private val articles: List<Article>): RecyclerView.Adapter<HeroAdapter.Holder>() {
    private lateinit var binding : ItemHomeTopHeroBinding
    private lateinit var context: Context

    inner class Holder(binding: ItemHomeTopHeroBinding): RecyclerView.ViewHolder(binding.root) {
        fun bind(article: Article) = with(binding) {
            Glide.with(this.root)
                .load(article.thumbnail)
                .into(image)
            title.text = article.title

            val tagAdapter = TagAdapter(article.tags)
            rvTags.setHasFixedSize(true)
            rvTags.layoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
            rvTags.adapter = tagAdapter
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Holder {
        context = parent.context
        binding = ItemHomeTopHeroBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return Holder(binding)
    }

    override fun onBindViewHolder(holder: Holder, position: Int) {
        holder.bind(articles[position])
    }

    override fun getItemCount(): Int = articles.size

}