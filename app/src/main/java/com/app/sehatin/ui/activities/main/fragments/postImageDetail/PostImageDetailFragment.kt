package com.app.sehatin.ui.activities.main.fragments.postImageDetail

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.app.sehatin.R
import com.app.sehatin.data.model.Posting
import com.app.sehatin.data.model.User
import com.app.sehatin.databinding.FragmentPostImageDetailBinding
import com.app.sehatin.injection.Injection
import com.app.sehatin.ui.viewmodel.PostViewModel
import com.app.sehatin.ui.viewmodel.ViewModelFactory
import com.app.sehatin.utils.LIKES_COLLECTION
import com.bumptech.glide.Glide

class PostImageDetailFragment : Fragment() {
    private var _binding: FragmentPostImageDetailBinding? = null
    private val binding get() = _binding!!
    private lateinit var posting: Posting
    private lateinit var postViewModel: PostViewModel
    private val postRef = Injection.providePostCollection()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        @Suppress("DEPRECATION")
        requireActivity().window.setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN)
        _binding = FragmentPostImageDetailBinding.inflate(inflater, container, false)
        initVariable()
        initListener()
        return binding.root
    }

    private fun initVariable() = with(binding) {
        posting = PostImageDetailFragmentArgs.fromBundle(arguments as Bundle).post
        postViewModel = ViewModelProvider(this@PostImageDetailFragment, ViewModelFactory.getInstance())[PostViewModel::class.java]
        Glide.with(requireContext())
            .load(posting.image)
            .into(postImage)
        likeCountTV.text = posting.likeCount.toString()
        commentCountTV.text = posting.commentCount.toString()
    }

    private fun initListener() = with(binding) {
        backBtn.setOnClickListener {
            requireActivity().onBackPressed()
        }
        commentBtn.setOnClickListener {
            val direction = PostImageDetailFragmentDirections.actionPostImageDetailFragmentToPostDetailFragment(posting)
            findNavController().navigate(direction)
        }
        initLikeButton()
    }

    private fun initLikeButton() = with(binding) {
        posting.id?.let { postId ->
            val userId = User.currentUser.id
            userId?.let {
                postRef
                    .document(postId)
                    .collection(LIKES_COLLECTION)
                    .document(it)
                    .addSnapshotListener { doc, error ->
                        if(error != null) {
                            Log.e(TAG, "setListener on like: $error")
                            return@addSnapshotListener
                        }
                        if(doc != null && doc.exists()) {
                            likeBtn.setImageResource(R.drawable.ic_liked)
                            likeBtn.setOnClickListener {
                                posting.likeCount = (posting.likeCount - 1)
                                likeCountTV.text = posting.likeCount.toString()
                                postViewModel.togglePostLike(posting, false)
                            }
                        } else {
                            likeBtn.setImageResource(R.drawable.ic_like)
                            likeBtn.setOnClickListener {
                                posting.likeCount = (posting.likeCount + 1)
                                likeCountTV.text = posting.likeCount.toString()
                                postViewModel.togglePostLike(posting, true)
                            }
                        }
                    }
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
        @Suppress("DEPRECATION")
        requireActivity().window.clearFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN)
    }

    private companion object {
        const val TAG = "PostImageDetailFragment"
    }

}