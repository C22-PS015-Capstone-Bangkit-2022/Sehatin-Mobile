package com.app.sehatin.ui.activities.main.fragments.userPost

import android.os.Bundle
import android.util.Log
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
import com.app.sehatin.data.Result
import com.app.sehatin.data.model.Posting
import com.app.sehatin.data.model.User
import com.app.sehatin.databinding.FragmentUserPostBinding
import com.app.sehatin.ui.sharedAdapter.post.PostListener
import com.app.sehatin.ui.sharedAdapter.post.PostAdapter
import com.app.sehatin.ui.viewmodel.PostViewModel
import com.app.sehatin.ui.viewmodel.ViewModelFactory

class UserPostFragment : Fragment() {
    private lateinit var binding: FragmentUserPostBinding
    private lateinit var postType: String
    private lateinit var postViewModel: PostViewModel
    private val postAdapter = PostAdapter()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        binding = FragmentUserPostBinding.inflate(inflater, container, false)
        initVariable()
        initListener()
        initData()
        return binding.root
    }

    private fun initVariable() = with(binding) {
        postType = UserPostFragmentArgs.fromBundle(arguments as Bundle).type
        postViewModel = ViewModelProvider(this@UserPostFragment, ViewModelFactory.getInstance())[PostViewModel::class.java]
        rvPost.setHasFixedSize(true)
        rvPost.layoutManager = LinearLayoutManager(requireContext())
        rvPost.adapter = postAdapter
    }

    private fun initListener() = with(binding) {
        toolbar.setNavigationOnClickListener {
            requireActivity().onBackPressed()
        }
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
                val direction = UserPostFragmentDirections.actionUserPostFragmentToPostDetailFragment(posting)
                findNavController().navigate(direction)
            }

            override fun onBookmarkClick(posting: Posting, bookmarkBtn: ImageView, position: Int) {
                Toast.makeText(requireContext(), "bookmark ${posting.id}", Toast.LENGTH_SHORT).show()
            }

            override fun onImageClick(posting: Posting) {
                val direction = UserPostFragmentDirections.actionUserPostFragmentToPostImageDetailFragment(posting)
                findNavController().navigate(direction)
            }

        })
    }

    private fun initData() {
        when(postType) {
            TYPE_USER_POST -> {
                val user = User.currentUser?.id
                user?.let {
                    getUserPost(it)
                }
            }
            TYPE_SAVED_POST -> {

            }
        }
    }

    private fun getUserPost(userId: String) {
        postViewModel.getUserPost(userId)
        postViewModel.userPostState.observe(viewLifecycleOwner) {
            when(it) {
                is Result.Loading -> {
                    Log.d(TAG, "getUserPost loading")
                }
                is Result.Error -> {
                    Log.e(TAG, "getUserPost error : ${it.error}")
                }
                is Result.Success -> {
                    Log.d(TAG, "getUserPost success : ${it.data}")
                    val data = it.data
                    if(data.isNotEmpty()) {
                        postAdapter.submitList(data)
                    }
                }
            }
        }

    }

    companion object {
        const val TYPE_USER_POST = "user post"
        const val TYPE_SAVED_POST = "saved post"
        const val TAG = "UserPostFragment"
    }

}