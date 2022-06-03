package com.app.sehatin.ui.activities.main.fragments.content.fragments.post

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.airbnb.lottie.animation.content.Content
import com.app.sehatin.R
import com.app.sehatin.data.model.Posting
import com.app.sehatin.data.model.User
import com.app.sehatin.databinding.FragmentPostBinding
import com.app.sehatin.ui.activities.main.fragments.content.ContentFragmentDirections
import com.app.sehatin.ui.sharedAdapter.LoadingStateAdapter
import com.app.sehatin.ui.sharedAdapter.post.PostAdapterWithPaging
import com.app.sehatin.ui.sharedAdapter.post.PostListener
import com.app.sehatin.ui.viewmodel.PostViewModel
import com.app.sehatin.ui.viewmodel.ViewModelFactory
import com.bumptech.glide.Glide

class PostFragment : Fragment() {

    private lateinit var binding: FragmentPostBinding
    private lateinit var postViewModel: PostViewModel
    private var postAdapter = PostAdapterWithPaging()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        binding = FragmentPostBinding.inflate(inflater, container, false)
        initVariable()
        initListener()
        initView()
        return binding.root
    }

    private fun initVariable() = with(binding) {
        postViewModel = ViewModelProvider(this@PostFragment, ViewModelFactory.getInstance())[PostViewModel::class.java]
        Glide.with(requireContext())
            .load(User.currentUser.imageUrl)
            .placeholder(R.drawable.user_default)
            .error(R.drawable.user_default)
            .into(postToolbarContent.userImageIV)
    }

    private fun initListener() = with(binding) {
        postAdapter.setListener(object : PostListener {
            override fun onLikeClick(posting: Posting, position: Int) {
                postViewModel.togglePostLike(posting, true)
                postAdapter.notifyItemChanged(position)
            }

            override fun onUnlikeClick(posting: Posting, position: Int) {
                postViewModel.togglePostLike(posting, false)
                postAdapter.notifyItemChanged(position)
            }

            override fun onCommentClick(posting: Posting, commentBtn: ImageView, commentCount: TextView) {
                val direction = ContentFragmentDirections.actionContentFragmentToPostDetailFragment(posting)
                findNavController().navigate(direction)
            }

            override fun onImageClick(posting: Posting) {
                val direction = ContentFragmentDirections.actionContentFragmentToPostImageDetailFragment(posting)
                findNavController().navigate(direction)
            }

            override fun onUserInfoClick(user: User) {
                val direction = ContentFragmentDirections.actionContentFragmentToUserPageFragment(user)
                findNavController().navigate(direction)
            }
        })

        addPostBtn.setOnClickListener {
            findNavController().navigate(R.id.action_contentFragment_to_addPostFragment)
        }

        postToolbarContent.searchLayout.setOnClickListener {
            findNavController().navigate(R.id.action_contentFragment_to_searchUserAndTagFragment)
        }

        postToolbarContent.chatBtn.setOnClickListener {
            findNavController().navigate(R.id.action_contentFragment_to_chatListFragment)
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