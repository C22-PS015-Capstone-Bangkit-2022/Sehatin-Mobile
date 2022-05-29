package com.app.sehatin.ui.sharedAdapter.post

import android.content.Context
import android.util.Log
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.app.sehatin.R
import com.app.sehatin.data.model.Posting
import com.app.sehatin.data.model.User
import com.app.sehatin.databinding.ItemPostBinding
import com.app.sehatin.injection.Injection
import com.app.sehatin.utils.DEFAULT
import com.app.sehatin.utils.LIKES_COLLECTION
import com.app.sehatin.utils.convertToDate
import com.bumptech.glide.Glide

class PostViewHolder(private val binding: ItemPostBinding, private val context: Context, private val postListener: PostListener) : RecyclerView.ViewHolder(binding.root) {
    private val postRef = Injection.providePostCollection()
    private val userRef = Injection.provideUserCollection()

    fun bind(posting: Posting) = with(binding) {
        Log.d("PostAdapter", "bind: ")
        setUserData(posting.userId)
        setContent(posting)
    }

    private fun setUserData(userId: String?) = with(binding) {
        userImageIV.setImageResource(R.drawable.user_default)
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
            val userId = User.currentUser.id
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
                            likeBtn.setImageResource(R.drawable.ic_liked)
                            likeBtn.setOnClickListener {
                                posting.likeCount = (posting.likeCount - 1)
                                postListener.onUnlikeClick(posting, bindingAdapterPosition)
                            }
                        } else {
                            likeBtn.setImageResource(R.drawable.ic_like)
                            likeBtn.setOnClickListener {
                                posting.likeCount = (posting.likeCount + 1)
                                postListener.onLikeClick(posting, bindingAdapterPosition)
                            }
                        }
                    }
            }
        }
        commentBtn.setOnClickListener { postListener.onCommentClick(posting, commentBtn, commentCountTV) }
        bookmarkBtn.setOnClickListener { postListener.onBookmarkClick(posting, bookmarkBtn, bindingAdapterPosition) }
        postImage.setOnClickListener { postListener.onImageClick(posting) }
    }
}