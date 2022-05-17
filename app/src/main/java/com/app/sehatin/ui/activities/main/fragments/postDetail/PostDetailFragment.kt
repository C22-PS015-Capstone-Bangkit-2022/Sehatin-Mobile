package com.app.sehatin.ui.activities.main.fragments.postDetail

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.app.sehatin.R
import com.app.sehatin.data.model.Posting
import com.app.sehatin.data.model.User
import com.app.sehatin.databinding.FragmentPostDetailBinding
import com.app.sehatin.injection.Injection
import com.app.sehatin.ui.activities.main.fragments.content.post.adapter.PostTagAdapter
import com.app.sehatin.ui.activities.main.fragments.postImageDetail.PostImageDetailFragment
import com.app.sehatin.ui.viewmodel.PostViewModel
import com.app.sehatin.ui.viewmodel.ViewModelFactory
import com.app.sehatin.utils.DEFAULT
import com.app.sehatin.utils.LIKES_COLLECTION
import com.app.sehatin.utils.convertToDate
import com.bumptech.glide.Glide
import com.google.firebase.firestore.FirebaseFirestore

class PostDetailFragment : Fragment() {
    private var _binding: FragmentPostDetailBinding? = null
    private val binding get() = _binding!!
    private lateinit var posting: Posting
    private val userRef = Injection.provideUserCollection()
    private val postRef = Injection.providePostCollection()
    private lateinit var postViewModel: PostViewModel

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        _binding = FragmentPostDetailBinding.inflate(inflater, container, false)
        initVariable()
        initListener()
        return binding.root
    }

    private fun initVariable() = with(binding) {
        postViewModel = ViewModelProvider(this@PostDetailFragment, ViewModelFactory.getInstance())[PostViewModel::class.java]
        posting = PostDetailFragmentArgs.fromBundle(arguments as Bundle).post
        if(posting.hasImage) {
            Glide.with(requireContext())
                .load(posting.image)
                .placeholder(R.color.grey2)
                .into(postImage)
        } else {
            postImage.visibility = View.GONE
        }
        postDescription.text = posting.description
        likeCountTV.text = posting.likeCount.toString()
        commentCountTV.text = posting.commentCount.toString()
        postDate.text = posting.createdAt?.convertToDate()
        val tags = posting.tags
        if(tags != null) {
            rvTags.visibility = View.VISIBLE
            val postTagAdapter = PostTagAdapter(tags)
            rvTags.setHasFixedSize(true)
            rvTags.layoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
            rvTags.adapter = postTagAdapter
        } else {
            rvTags.visibility = View.GONE
        }
        initSelectedUserData()
    }

    private fun initSelectedUserData() = with(binding) {
        posting.userId?.let { userId ->
            userRef
                .document(userId)
                .get()
                .addOnSuccessListener { doc ->
                    val user = doc.toObject(User::class.java)
                    val imageUrl = user?.imageUrl
                    if(imageUrl != null && imageUrl != DEFAULT) {
                        Glide.with(this.root)
                            .load(imageUrl)
                            .placeholder(R.drawable.user_default)
                            .error(R.drawable.user_default)
                            .into(userImageIV)
                    } else {
                        userImageIV.setImageResource(R.drawable.user_default)
                    }
                    usernameTv.text = user?.username.toString()
                }
        }
    }

    private fun initListener() = with(binding) {
        initLikeButton()
        commentBtn.setOnClickListener {
            commentInput.requestFocus()
        }
        bookmarkBtn.setOnClickListener {
            Toast.makeText(requireContext(), "bookmark ${posting.id}", Toast.LENGTH_SHORT).show()
        }
        backBtn.setOnClickListener {
            requireActivity().onBackPressed()
        }
        moreBtn.setOnClickListener {
            Toast.makeText(requireContext(), "more ${posting.id}", Toast.LENGTH_SHORT).show()
        }
    }

    private fun initLikeButton() = with(binding) {
        posting.id?.let { postId ->
            val userId = User.currentUser?.id
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
    }

    private companion object {
        const val TAG = "PostDetailFragment"
    }

}