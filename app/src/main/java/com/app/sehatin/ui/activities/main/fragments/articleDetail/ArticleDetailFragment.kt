package com.app.sehatin.ui.activities.main.fragments.articleDetail

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.app.sehatin.databinding.FragmentArticleDetailBinding

class ArticleDetailFragment : Fragment() {
    private lateinit var binding: FragmentArticleDetailBinding

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        binding = FragmentArticleDetailBinding.inflate(inflater, container, false)
        return binding.root
    }

}