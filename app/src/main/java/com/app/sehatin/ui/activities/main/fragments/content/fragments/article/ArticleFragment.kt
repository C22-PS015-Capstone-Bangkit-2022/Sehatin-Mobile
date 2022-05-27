package com.app.sehatin.ui.activities.main.fragments.content.fragments.article

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.app.sehatin.R
import com.app.sehatin.databinding.FragmentArticleBinding
import com.app.sehatin.ui.activities.main.fragments.content.ContentViewModel

class ArticleFragment : Fragment() {
    private lateinit var binding : FragmentArticleBinding

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        binding = FragmentArticleBinding.inflate(inflater, container, false)
        initVariable()
        return binding.root
    }

    private fun initVariable() {
        if(viewModel != null) {
            Log.d(TAG, "initVariable: viewmodel not null")
        } else {
            Log.d(TAG, "initVariable: viewmodel null")
        }
    }


    override fun onDestroyView() {
        super.onDestroyView()
        viewModel = null
        Log.d(TAG, "onDestroyView: ")
    }

    companion object {
        private const val TAG = "ArticleFragment"
        var viewModel: ContentViewModel? = null
    }

}