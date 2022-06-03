package com.app.sehatin.ui.activities.main.fragments.userPage

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.app.sehatin.R
import com.app.sehatin.data.Result
import com.app.sehatin.data.model.Posting
import com.app.sehatin.data.model.User
import com.app.sehatin.databinding.FragmentUserPageBinding
import com.app.sehatin.ui.sharedAdapter.post.PostAdapter
import com.app.sehatin.ui.sharedAdapter.post.PostListener
import com.app.sehatin.ui.viewmodel.ViewModelFactory
import com.bumptech.glide.Glide

class UserPageFragment : Fragment() {
    private lateinit var binding: FragmentUserPageBinding
    private lateinit var user: User
    private lateinit var viewModel: UserPageViewModel
    private val postAdapter = PostAdapter()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        binding = FragmentUserPageBinding.inflate(inflater, container, false)
        initVariable()
        initListener()
        return binding.root
    }

    private fun initVariable() = with(binding) {
        viewModel = ViewModelProvider(this@UserPageFragment, ViewModelFactory.getInstance())[UserPageViewModel::class.java]
        user = UserPageFragmentArgs.fromBundle(arguments as Bundle).user
        Glide.with(requireContext())
            .load(user.imageUrl)
            .placeholder(R.drawable.user_default)
            .error(R.drawable.user_default)
            .into(userImageIV)
        usernameTv.text = user.username
        user.id?.let { viewModel.getUserPost(it) }
        rvPost.layoutManager = LinearLayoutManager(requireContext())
        rvPost.adapter = postAdapter
    }

    private fun initListener() = with(binding) {
        toolbar.setNavigationOnClickListener {
            requireActivity().onBackPressed()
        }

        messageBtn.setOnClickListener {
            user.id?.let {
                val direction = UserPageFragmentDirections.actionUserPageFragmentToSendChatFragment(it)
                findNavController().navigate(direction)
            }
        }

        if(viewModel.userPost.isEmpty()) {
            viewModel.userPostState.observe(viewLifecycleOwner) {
                when(it) {
                    is Result.Loading -> {
                        Log.d(TAG, "userPostState: loading")
                    }
                    is Result.Error -> {
                        Log.e(TAG, "userPostState: error = ${it.error}")
                    }
                    is Result.Success -> {
                        Log.d(TAG, "userPostState: success, data = ${it.data}")
                        viewModel.userPost.clear()
                        viewModel.userPost.addAll(it.data)
                        postAdapter.submitList(it.data)
                    }
                }
            }
        } else {
            postAdapter.submitList(viewModel.userPost)
        }

        postAdapter.setListener(object : PostListener {
            override fun onLikeClick(posting: Posting, position: Int) {
                viewModel.togglePostLike(posting, true)
                postAdapter.notifyItemChanged(position)
            }

            override fun onUnlikeClick(posting: Posting, position: Int) {
                viewModel.togglePostLike(posting, false)
                postAdapter.notifyItemChanged(position)
            }

            override fun onCommentClick(posting: Posting, commentBtn: ImageView, commentCount: TextView) {
                val direction = UserPageFragmentDirections.actionUserPageFragmentToPostDetailFragment(posting)
                findNavController().navigate(direction)
            }

            override fun onImageClick(posting: Posting) {
                val direction = UserPageFragmentDirections.actionUserPageFragmentToPostImageDetailFragment(posting)
                findNavController().navigate(direction)
            }

        })
    }

    private companion object {
        const val TAG = "UserPageFragment"
    }

}