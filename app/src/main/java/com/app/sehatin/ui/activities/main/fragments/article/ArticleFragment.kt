package com.app.sehatin.ui.activities.main.fragments.article

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.app.sehatin.data.Result
import com.app.sehatin.databinding.FragmentArticleBinding
import com.app.sehatin.ui.activities.main.fragments.content.ContentFragment
import com.app.sehatin.ui.activities.main.fragments.content.ContentFragmentDirections

class ArticleFragment : Fragment() {
    private lateinit var binding : FragmentArticleBinding
    private lateinit var articleAdapter: ArticleAdapter
    private val viewModel = ContentFragment.viewModel

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        binding = FragmentArticleBinding.inflate(inflater, container, false)
        initVariable()
        initListener()
        initData()
        return binding.root
    }

    private fun initVariable() = with(binding) {
        articleAdapter = ArticleAdapter {
            val direction = ContentFragmentDirections.actionContentFragmentToArticleDetailFragment(it)
            findNavController().navigate(direction)
        }
        rvArticle.setHasFixedSize(true)
        rvArticle.layoutManager = LinearLayoutManager(requireContext())
        rvArticle.adapter = articleAdapter
    }

    private fun initListener() = with(binding) {
        toolbar.setNavigationOnClickListener {
            requireActivity().onBackPressed()
        }
        refreshLayout.setOnRefreshListener {
            viewModel.clearArticleFragmentState()
            initData()
            refreshLayout.isRefreshing = false
        }
    }

    private fun initData() {
        if(viewModel.articles.isEmpty()) {
            viewModel.getArticles().observe(viewLifecycleOwner) {
                when(it) {
                    is Result.Loading -> {
                        showLoading(true)
                    }
                    is Result.Error -> {
                        Toast.makeText(requireContext(), it.error, Toast.LENGTH_SHORT).show()
                        showLoading(false)
                    }
                    is Result.Success -> {
                        val data = it.data
                        if(data != null) {
                            showLoading(false)
                            articleAdapter.submitList(data.articles)
                            data.articles?.let { articles ->
                                viewModel.articles.addAll(articles)
                            }
                        }
                    }
                }
            }
        } else {
            showLoading(false)
            articleAdapter.submitList(viewModel.articles)
        }
    }

    private fun showLoading(isLoading: Boolean) = with(binding) {
        if(isLoading) {
            progressBar.visibility = View.VISIBLE
            refreshLayout.visibility = View.GONE
        } else {
            progressBar.visibility = View.GONE
            refreshLayout.visibility = View.VISIBLE
        }
    }

}