package com.app.sehatin.ui.activities.main.fragments.userPost

import android.annotation.SuppressLint
import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.app.sehatin.R
import com.app.sehatin.data.model.Posting
import com.app.sehatin.data.model.User
import com.app.sehatin.databinding.ItemPostBinding
import com.app.sehatin.injection.Injection
import com.app.sehatin.ui.activities.main.fragments.content.post.adapter.PostListener
import com.app.sehatin.ui.activities.main.fragments.content.post.adapter.PostTagAdapter
import com.app.sehatin.utils.DEFAULT
import com.app.sehatin.utils.LIKES_COLLECTION
import com.app.sehatin.utils.convertToDate
import com.bumptech.glide.Glide

class PostAdapter: ListAdapter<Posting, PostAdapter.Holder>(DIFF_CALLBACK) {
    private lateinit var onClickListener: PostListener
    private lateinit var context: Context
    private val postRef = Injection.providePostCollection()
    private val userRef = Injection.provideUserCollection()

    fun setListener(onClickListener: PostListener) {
        this.onClickListener = onClickListener
    }

    inner class Holder(private val binding: ItemPostBinding): RecyclerView.ViewHolder(binding.root) {
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
                                likeBtn.setImageResource(R.drawable.ic_liked)
                                likeBtn.setOnClickListener {
                                    posting.likeCount = (posting.likeCount - 1)
                                    onClickListener.onUnlikeClick(posting, bindingAdapterPosition)
                                }
                            } else {
                                likeBtn.setImageResource(R.drawable.ic_like)
                                likeBtn.setOnClickListener {
                                    posting.likeCount = (posting.likeCount + 1)
                                    onClickListener.onLikeClick(posting, bindingAdapterPosition)
                                }
                            }
                        }
                }
            }
            commentBtn.setOnClickListener { onClickListener.onCommentClick(posting, commentBtn, commentCountTV) }
            bookmarkBtn.setOnClickListener { onClickListener.onBookmarkClick(posting, bookmarkBtn, bindingAdapterPosition) }
            postImage.setOnClickListener { onClickListener.onImageClick(posting) }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Holder {
        context = parent.context
        val binding = ItemPostBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return Holder(binding)
    }

    override fun onBindViewHolder(holder: Holder, position: Int) {
        val posting = getItem(position)
        holder.bind(posting)
        holder.setListener(posting)
    }

    companion object {
        private const val TAG = "PostAdapter"
        val DIFF_CALLBACK: DiffUtil.ItemCallback<Posting> =
            object : DiffUtil.ItemCallback<Posting>() {
                override fun areItemsTheSame(oldUser: Posting, newUser: Posting): Boolean {
                    return oldUser.id == newUser.id
                }

                @SuppressLint("DiffUtilEquals")
                override fun areContentsTheSame(oldUser: Posting, newUser: Posting): Boolean {
                    return oldUser == newUser
                }
            }
    }

}