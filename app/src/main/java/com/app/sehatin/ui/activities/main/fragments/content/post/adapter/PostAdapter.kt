package com.app.sehatin.ui.activities.main.fragments.content.post.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.app.sehatin.R
import com.app.sehatin.data.model.Posting
import com.app.sehatin.databinding.ItemPostBinding
import com.app.sehatin.utils.convertToDate
import com.bumptech.glide.Glide

class PostAdapter : PagingDataAdapter<Posting, PostAdapter.PostViewHolder>(Companion) {
    private lateinit var onClickListener: OnClickListener
    private lateinit var context: Context

    fun setListener(onClickListener: OnClickListener) {
        this.onClickListener = onClickListener
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PostViewHolder {
        context = parent.context
        val binding = ItemPostBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return PostViewHolder(binding, onClickListener)
    }

    override fun onBindViewHolder(holder: PostViewHolder, position: Int) {
        val post = getItem(position) ?: return
        holder.bind(post)
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
            setUserData(posting.userId)
            setContent(posting)
            setListener(posting)
        }

        private fun setUserData(userId: String?) = with(binding) {
            userImageIV.setImageResource(R.drawable.mamad)
            usernameTv.text = "Ahmad Fathanah"
        }

        private fun setListener(posting: Posting) = with(binding) {
            likeBtn.setOnClickListener { onClickListener.onLikeClick(posting, likeBtn, likeCountTV) }
            commentBtn.setOnClickListener { onClickListener.onCommentClick(posting, commentBtn, commentCountTV) }
            bookmarkBtn.setOnClickListener { onClickListener.onBookmarkClick(posting, bookmarkBtn) }
        }

        private fun setContent(posting: Posting) = with(binding) {
            posting.hasImage?.let { hasImage ->
                if(hasImage) {
                    Glide.with(this.root)
                        .load(posting.image)
                        .into(postImage)
                } else {
                    postImage.visibility = View.GONE
                    postDescription.maxLines = 10
                    val params = postDescription.layoutParams as ConstraintLayout.LayoutParams
                    params.topToBottom = verticalLine.id
                    postDescription.requestLayout()
                }
            }

            postDescription.text = posting.description
            likeCountTV.text = posting.likeCount.toString()
            postDate.text = posting.createdAt?.convertToDate()

            commentCountTV.text = posting.commentCount.toString()

            val tags = posting.tags
            if(tags != null) {
                val postTagAdapter = PostTagAdapter(tags)
                rvTags.setHasFixedSize(true)
                rvTags.layoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
                rvTags.adapter = postTagAdapter
            } else {
                rvTags.visibility = View.GONE
                val params = likeBtn.layoutParams as ConstraintLayout.LayoutParams
                params.topToBottom = postDescription.id
                likeBtn.requestLayout()
            }
        }
    }

    interface OnClickListener {
        fun onLikeClick(posting: Posting, likeBtn: ImageView, likeCount: TextView)
        fun onCommentClick(posting: Posting, commentBtn: ImageView, commentCount: TextView)
        fun onBookmarkClick(posting: Posting, bookmarkBtn: ImageView)
    }
}

//class PostAdapter: ListAdapter<Posting, PostAdapter.Holder>(DIFF_CALLBACK)  {
//    private lateinit var onClickListener: OnClickListener
//    private lateinit var context: Context
//
//    fun setListener(onClickListener: OnClickListener) {
//        this.onClickListener = onClickListener
//    }
//
//    inner class Holder(private val binding: ItemPostBinding, private val onClickListener: OnClickListener): RecyclerView.ViewHolder(binding.root) {
//        fun bind(posting: Posting) = with(binding) {
//            setUserData(posting.userId)
//            setContent(posting)
//            setListener(posting)
//        }
//
//        private fun setUserData(userId: String?) = with(binding) {
//            userImageIV.setImageResource(R.drawable.mamad)
//            usernameTv.text = "Ahmad Fathanah"
//        }
//
//        private fun setListener(posting: Posting) = with(binding) {
//            likeBtn.setOnClickListener { onClickListener.onLikeClick(posting, likeBtn, likeCountTV) }
//            commentBtn.setOnClickListener { onClickListener.onCommentClick(posting, commentBtn, commentCountTV) }
//            bookmarkBtn.setOnClickListener { onClickListener.onBookmarkClick(posting, bookmarkBtn) }
//        }
//
//        private fun setContent(posting: Posting) = with(binding) {
//            posting.hasImage?.let { hasImage ->
//                if(hasImage) {
//                    Glide.with(this.root)
//                        .load(posting.image)
//                        .into(postImage)
//                } else {
//                    postImage.visibility = View.GONE
//                    postDescription.maxLines = 10
//                    val params = postDescription.layoutParams as ConstraintLayout.LayoutParams
//                    params.topToBottom = verticalLine.id
//                    postDescription.requestLayout()
//                }
//            }
//
//            postDescription.text = posting.description
//            likeCountTV.text = posting.likes.toString()
//            postDate.text = posting.createdAt?.convertToDate()
//
//            val commentCount = posting.comment?.size
//            if(commentCount != null) {
//                commentCountTV.text = commentCount.toString()
//            } else {
//                commentCountTV.text = "0"
//            }
//
//            val tags = posting.tags
//            if(tags != null) {
//                val postTagAdapter = PostTagAdapter(tags)
//                rvTags.setHasFixedSize(true)
//                rvTags.layoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
//                rvTags.adapter = postTagAdapter
//            } else {
//                rvTags.visibility = View.GONE
//                val params = likeBtn.layoutParams as ConstraintLayout.LayoutParams
//                params.topToBottom = postDescription.id
//                likeBtn.requestLayout()
//            }
//        }
//
//    }
//
//    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Holder {
//        context = parent.context
//        val binding = ItemPostBinding.inflate(LayoutInflater.from(parent.context), parent, false)
//        return Holder(binding, onClickListener)
//    }
//
//    override fun onBindViewHolder(holder: Holder, position: Int) {
//        val post = getItem(position)
//        holder.bind(post)
//    }
//
//    interface OnClickListener {
//        fun onLikeClick(posting: Posting, likeBtn: ImageView, likeCount: TextView)
//        fun onCommentClick(posting: Posting, commentBtn: ImageView, commentCount: TextView)
//        fun onBookmarkClick(posting: Posting, bookmarkBtn: ImageView)
//    }
//
//    companion object {
//        val DIFF_CALLBACK: DiffUtil.ItemCallback<Posting> =
//            object : DiffUtil.ItemCallback<Posting>() {
//                override fun areItemsTheSame(oldPost: Posting, newPost: Posting): Boolean {
//                    return oldPost.id == newPost.id
//                }
//
//                @SuppressLint("DiffUtilEquals")
//                override fun areContentsTheSame(oldPost: Posting, newPost: Posting): Boolean {
//                    return oldPost == newPost
//                }
//            }
//    }
//
//}