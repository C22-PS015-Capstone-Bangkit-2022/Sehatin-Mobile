package com.app.sehatin.ui.sharedAdapter

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.app.sehatin.data.model.Article
import com.app.sehatin.databinding.ItemArticleMinBinding
import com.app.sehatin.utils.convertToDate
import com.bumptech.glide.Glide

class ArticleMinAdapter(private val onClick: (Article) -> Unit): ListAdapter<Article, ArticleMinAdapter.Holder>(DIFF_CALLBACK) {

    inner class Holder(private val binding: ItemArticleMinBinding): RecyclerView.ViewHolder(binding.root) {
        fun bind(article: Article) = with(binding) {
            Glide.with(this.root)
                .load(article.thumbnail)
                .into(articleImage)
            articleTitle.text = article.title
            val str = StringBuilder()
            article.sourceName?.let {
                str.append(it)
            }
            article.createdAt?.let {
                str.append(" | "+it.convertToDate())
            }
            articleSource.text = str

            this.root.setOnClickListener {
                onClick(article)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Holder {
        val binding = ItemArticleMinBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return Holder(binding)
    }

    override fun onBindViewHolder(holder: Holder, position: Int) {
        val article = getItem(position)
        holder.bind(article)
    }

    companion object {
        val DIFF_CALLBACK: DiffUtil.ItemCallback<Article> =
            object : DiffUtil.ItemCallback<Article>() {
                override fun areItemsTheSame(oldArticle: Article, newArticle: Article): Boolean {
                    return oldArticle.id == newArticle.id
                }

                @SuppressLint("DiffUtilEquals")
                override fun areContentsTheSame(oldArticle: Article, newArticle: Article): Boolean {
                    return oldArticle == newArticle
                }
            }
    }

}