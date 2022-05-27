package com.app.sehatin.ui.activities.main.fragments.content.fragments.article

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.app.sehatin.data.model.Article
import com.app.sehatin.databinding.ItemArticleBinding
import com.app.sehatin.databinding.ItemArticleTagBinding
import com.app.sehatin.utils.convertToDate
import com.bumptech.glide.Glide

class ArticleAdapter(private val onViewClick: (Article) -> Unit): ListAdapter<Article, ArticleAdapter.Holder>(DIFF_CALLBACK) {
    private lateinit var context: Context
    private lateinit var binding: ItemArticleBinding

    inner class Holder(binding: ItemArticleBinding): RecyclerView.ViewHolder(binding.root) {
        fun bind(article: Article) = with(binding) {
            Glide.with(this.root)
                .load(article.thumbnail)
                .into(articleImage)
            articleTitle.text = article.title
            articleDate.text = article.createdAt?.convertToDate()
            articleSource.text = article.sourceName
            articleDesc.text = article.content
            val tags = article.tags
            chipsGroup.removeAllViews()
            if(tags != null) {
                for (str in tags) {
                    val chip = ItemArticleTagBinding.inflate(LayoutInflater.from(context), binding.chipsGroup, false)
                    val tag = chip.root
                    tag.text = str
                    binding.chipsGroup.addView(tag)
                }
            }
            this.root.setOnClickListener {
                onViewClick(article)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Holder {
        context = parent.context
        binding = ItemArticleBinding.inflate(LayoutInflater.from(parent.context), parent, false)
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