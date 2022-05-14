package com.app.sehatin.ui.activities.main.fragments.content.post

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.app.sehatin.R
import com.app.sehatin.data.model.Posting
import com.app.sehatin.databinding.FragmentPostBinding
import com.app.sehatin.ui.activities.main.fragments.content.post.adapter.PostAdapter
import com.app.sehatin.ui.sharedAdapter.LoadingStateAdapter
import com.app.sehatin.ui.viewmodel.ViewModelFactory

class PostFragment : Fragment() {

    private lateinit var binding: FragmentPostBinding
    private lateinit var postViewModel: PostViewModel
    private var postAdapter = PostAdapter()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        binding = FragmentPostBinding.inflate(inflater, container, false)
        initVariable()
        initListener()
        initView()
        return binding.root
    }

    private fun initVariable() {
        postViewModel = ViewModelProvider(this, ViewModelFactory.getInstance())[PostViewModel::class.java]
    }

    private fun initListener() = with(binding) {
        postAdapter.setListener(object : PostAdapter.OnClickListener {
            override fun onLikeClick(posting: Posting, likeBtn: ImageView, likeCount: TextView) {
                Toast.makeText(requireContext(), "like ${posting.id}", Toast.LENGTH_SHORT).show()
            }

            override fun onCommentClick(posting: Posting, commentBtn: ImageView, commentCount: TextView) {
                Toast.makeText(requireContext(), "comment ${posting.id}", Toast.LENGTH_SHORT).show()
            }

            override fun onBookmarkClick(posting: Posting, bookmarkBtn: ImageView) {
                Toast.makeText(requireContext(), "bookmark ${posting.id}", Toast.LENGTH_SHORT).show()
            }
        })

        addPostBtn.setOnClickListener {
            findNavController().navigate(R.id.action_contentFragment_to_addPostFragment)
        }
    }

    private fun initView() = with(binding) {
        rvPost.setHasFixedSize(true)
        rvPost.layoutManager = LinearLayoutManager(requireContext())
        rvPost.adapter = postAdapter.withLoadStateFooter(
            footer = LoadingStateAdapter {
                postAdapter.retry()
            }
        )

        postViewModel.getPosts().observe(viewLifecycleOwner) {
            postAdapter.submitData(lifecycle, it)
        }
    }

}