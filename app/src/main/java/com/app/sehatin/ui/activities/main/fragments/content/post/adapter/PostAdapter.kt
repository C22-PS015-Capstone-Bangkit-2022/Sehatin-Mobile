package com.app.sehatin.ui.activities.main.fragments.content.post.adapter

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.app.sehatin.R
import com.app.sehatin.data.model.Posting
import com.app.sehatin.data.model.User
import com.app.sehatin.databinding.ItemPostBinding
import com.app.sehatin.utils.*
import com.bumptech.glide.Glide
import com.google.firebase.firestore.FirebaseFirestore

class PostAdapter : PagingDataAdapter<Posting, PostAdapter.PostViewHolder>(Companion) {
    private lateinit var onClickListener: OnClickListener
    private lateinit var context: Context
    private val postRef = FirebaseFirestore.getInstance().collection(POST_COLLECTION)
    private val userRef = FirebaseFirestore.getInstance().collection(USER_COLLECTION)

    fun setListener(onClickListener: OnClickListener) {
        this.onClickListener = onClickListener
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PostViewHolder {
        context = parent.context
        val binding = ItemPostBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return PostViewHolder(binding, onClickListener)
    }

    override fun onBindViewHolder(holder: PostViewHolder, position: Int) {
        Log.d("PostAdapter", "onBindViewHolder: $position")
        val post = getItem(position) ?: return
        holder.bind(post)
        holder.setListener(post)
    }

    companion object : DiffUtil.ItemCallback<Posting>() {
        override fun areItemsTheSame(oldItem: Posting, newItem: Posting): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: Posting, newItem: Posting): Boolean {
            return oldItem == newItem
        }
    }

    inner class PostViewHolder(private val binding: ItemPostBinding, private val onClickListener: OnClickListener) : RecyclerView.ViewHolder(binding.root) {
        fun bind(posting: Posting) = with(binding) {
            Log.d("PostAdapter", "bind: ")
            setUserData(posting.userId)
            setContent(posting)
        }

        private fun setUserData(userId: String?) = with(binding) {
            userId?.let {
                userRef
                    .document(it)
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

        private fun setContent(posting: Posting) = with(binding) {
            if(posting.hasImage) {
                postImage.visibility = View.VISIBLE
                Glide.with(this.root)
                    .load(posting.image)
                    .placeholder(R.color.grey)
                    .into(postImage)
            } else {
                postImage.visibility = View.GONE
                postDescription.maxLines = 10
            }

            postDescription.text = posting.description
            likeCountTV.text = posting.likeCount.toString()
            postDate.text = posting.createdAt?.convertToDate()
            commentCountTV.text = posting.commentCount.toString()

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
        }

        fun setListener(posting: Posting) = with(binding) {
            posting.id?.let { postId ->
                val userId = User.currentUser?.id
                userId?.let {
                    postRef.document(postId)
                        .collection(LIKES_COLLECTION)
                        .document(it)
                        .addSnapshotListener { doc, error ->
                            if(error != null) {
                                Log.e("PostAdapter", "setListener on like: $error")
                                return@addSnapshotListener
                            }
                            if(doc != null && doc.exists()) {
                                Log.d("PostAdapter", "setListener on like: post $postId liked")
                                likeBtn.setImageResource(R.drawable.ic_liked)
                                likeBtn.setOnClickListener { onClickListener.onUnlikeClick(posting, likeBtn, likeCountTV, bindingAdapterPosition) }
                            } else {
                                Log.d("PostAdapter", "setListener on like: post $postId not liked")
                                likeBtn.setImageResource(R.drawable.ic_like)
                                likeBtn.setOnClickListener { onClickListener.onLikeClick(posting, likeBtn, likeCountTV, bindingAdapterPosition) }
                            }
                        }
                }
            }
            commentBtn.setOnClickListener { onClickListener.onCommentClick(posting, commentBtn, commentCountTV) }
            bookmarkBtn.setOnClickListener { onClickListener.onBookmarkClick(posting, bookmarkBtn, bindingAdapterPosition) }
        }
    }

    interface OnClickListener {
        fun onLikeClick(posting: Posting, likeBtn: ImageView, likeCount: TextView, position: Int)
        fun onUnlikeClick(posting: Posting, likeBtn: ImageView, likeCount: TextView, position: Int)
        fun onCommentClick(posting: Posting, commentBtn: ImageView, commentCount: TextView)
        fun onBookmarkClick(posting: Posting, bookmarkBtn: ImageView, position: Int)
    }

}