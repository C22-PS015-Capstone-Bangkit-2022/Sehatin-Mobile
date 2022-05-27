package com.app.sehatin.ui.activities.main.fragments.articleDetail

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.app.sehatin.data.model.Article
import com.app.sehatin.databinding.FragmentArticleDetailBinding
import com.app.sehatin.databinding.ItemArticleTagBinding
import com.app.sehatin.utils.convertToDate
import com.bumptech.glide.Glide

class ArticleDetailFragment : Fragment() {
    private lateinit var binding: FragmentArticleDetailBinding
    private lateinit var article: Article

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        binding = FragmentArticleDetailBinding.inflate(inflater, container, false)
        initVariable()
        initListener()
        initView()
        return binding.root
    }

    private fun initVariable() {
        article = ArticleDetailFragmentArgs.fromBundle(arguments as Bundle).article
    }


    private fun initListener() = with(binding) {
        backBtn.setOnClickListener {
            requireActivity().onBackPressed()
        }
    }

    private fun initView() = with(binding) {
        Glide.with(this.root)
            .load(article.thumbnail)
            .into(articleImage)
        articleTitle.text = article.title
        val str = StringBuilder()
        article.sourceName?.let {
            str.append(it)
        }
        article.createdAt?.let {
            if(str.isNotEmpty()) {
                str.append(" - ")
            }
            str.append(it.convertToDate())
        }
        articleSource.text = str
        articleContent.text = article.content

        val tags = article.tags
        chipsGroup.removeAllViews()
        if(tags != null) {
            for (tag in tags) {
                val chip = ItemArticleTagBinding.inflate(LayoutInflater.from(context), chipsGroup, false)
                chip.root.text = tag
                binding.chipsGroup.addView(chip.root)
            }
        }
    }

}