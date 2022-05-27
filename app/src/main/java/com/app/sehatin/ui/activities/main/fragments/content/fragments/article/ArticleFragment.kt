package com.app.sehatin.ui.activities.main.fragments.content.fragments.article

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import com.app.sehatin.data.Result
import com.app.sehatin.databinding.FragmentArticleBinding
import com.app.sehatin.ui.activities.main.fragments.content.ContentViewModel

class ArticleFragment : Fragment() {
    private lateinit var binding : FragmentArticleBinding
    private lateinit var articleAdapter: ArticleAdapter

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        binding = FragmentArticleBinding.inflate(inflater, container, false)
        initVariable()
        if(viewModel != null) {
            initData()
        } else {
            Log.d(TAG, "initVariable: viewmodel null")
        }
        return binding.root
    }

    private fun initVariable() = with(binding) {
        articleAdapter = ArticleAdapter {
            Toast.makeText(requireContext(), it.id.toString(), Toast.LENGTH_SHORT).show()
        }
        rvArticle.setHasFixedSize(true)
        rvArticle.layoutManager = LinearLayoutManager(requireContext())
        rvArticle.adapter = articleAdapter
    }

    private fun initData() {
        viewModel?.getArticles()?.observe(viewLifecycleOwner) {
            when(it) {
                is Result.Loading -> {
                    Log.d(TAG, "getArticles: loading")
                    showLoading(true)
                }
                is Result.Error -> {
                    Log.e(TAG, "getArticles: error = ${it.error}")
                    showLoading(false)
                }
                is Result.Success -> {
                    Log.d(TAG, "getArticles: success = ${it.data}")
                    val data = it.data
                    if(data != null) {
                        showLoading(false)
                        articleAdapter.submitList(data.articles)
                    }
                }
            }
        }
    }

    private fun showLoading(isLoading: Boolean) = with(binding) {
        if(isLoading) {
            progressBar.visibility = View.VISIBLE
            rvArticle.visibility = View.GONE
        } else {
            progressBar.visibility = View.GONE
            rvArticle.visibility = View.VISIBLE
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