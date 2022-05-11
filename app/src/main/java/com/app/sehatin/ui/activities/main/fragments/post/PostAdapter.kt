package com.app.sehatin.ui.activities.main.fragments.post

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.app.sehatin.R
import com.app.sehatin.data.model.Posting
import com.app.sehatin.databinding.ItemPostBinding
import com.bumptech.glide.Glide

class PostAdapter: ListAdapter<Posting, PostAdapter.Holder>(DIFF_CALLBACK)  {
    private lateinit var onClickListener: OnClickListener

    inner class Holder(private val binding: ItemPostBinding, private val onClickListener: OnClickListener): RecyclerView.ViewHolder(binding.root) {
        fun bind(posting: Posting) = with(binding) {
            setUserData(posting.userId)
            setContent(posting)
            setListener(posting)
        }

        private fun setUserData(userId: String) = with(binding) {
            userImageIV.setImageResource(R.drawable.mamad)
            usernameTv.text = "Ahmad Fathanah"
        }

        private fun setListener(posting: Posting) = with(binding) {
            likeBtn.setOnClickListener { onClickListener.onLikeClick(posting, likeBtn, likeCountTV) }
            commentBtn.setOnClickListener { onClickListener.onCommentClick(posting, commentBtn, commentCountTV) }
            bookmarkBtn.setOnClickListener { onClickListener.onBookmarkClick(posting, bookmarkBtn) }
        }

        private fun setContent(posting: Posting) = with(binding) {
            Glide.with(this.root)
                .load(posting.image)
                .into(postImage)
            postDescription.text = posting.description
        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PostAdapter.Holder {
        val binding = ItemPostBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return Holder(binding, onClickListener)
    }

    override fun onBindViewHolder(holder: PostAdapter.Holder, position: Int) {
        val post = getItem(position)
        holder.bind(post)
    }

    interface OnClickListener {
        fun onLikeClick(posting: Posting, likeBtn: ImageView, likeCount: TextView)
        fun onCommentClick(posting: Posting, commentBtn: ImageView, commentCount: TextView)
        fun onBookmarkClick(posting: Posting, bookmarkBtn: ImageView)
    }

    companion object {
        val DIFF_CALLBACK: DiffUtil.ItemCallback<Posting> =
            object : DiffUtil.ItemCallback<Posting>() {
                override fun areItemsTheSame(oldPost: Posting, newPost: Posting): Boolean {
                    return oldPost.id == newPost.id
                }

                @SuppressLint("DiffUtilEquals")
                override fun areContentsTheSame(oldPost: Posting, newPost: Posting): Boolean {
                    return oldPost == newPost
                }
            }
    }

}