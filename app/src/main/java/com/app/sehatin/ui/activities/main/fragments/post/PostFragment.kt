package com.app.sehatin.ui.activities.main.fragments.post

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.lifecycle.ViewModelProvider
import com.app.sehatin.data.model.Posting
import com.app.sehatin.databinding.FragmentPostBinding
import com.app.sehatin.ui.activities.main.fragments.post.adapter.PostAdapter
import com.app.sehatin.ui.viewmodel.ViewModelFactory


class PostFragment : Fragment() {

    private lateinit var binding: FragmentPostBinding
    private lateinit var postViewModel: PostViewModel
    private lateinit var postAdapter: PostAdapter

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        binding = FragmentPostBinding.inflate(inflater, container, false)
        initVariable()
        initListener()
        return binding.root
    }

    private fun initVariable() {
        postViewModel = ViewModelProvider(this, ViewModelFactory.getInstance())[PostViewModel::class.java]
        postAdapter = PostAdapter()
    }

    private fun initListener() {
        postAdapter.setListener(object : PostAdapter.OnClickListener {
            override fun onLikeClick(posting: Posting, likeBtn: ImageView, likeCount: TextView) {

            }

            override fun onCommentClick(posting: Posting, commentBtn: ImageView, commentCount: TextView) {

            }

            override fun onBookmarkClick(posting: Posting, bookmarkBtn: ImageView) {

            }

        })
    }


}