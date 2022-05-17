package com.app.sehatin.ui.sharedAdapter

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.app.sehatin.R
import com.app.sehatin.data.model.Comment
import com.app.sehatin.data.model.User
import com.app.sehatin.databinding.ItemCommentBinding
import com.app.sehatin.injection.Injection
import com.app.sehatin.utils.DEFAULT
import com.app.sehatin.utils.convertToDate
import com.bumptech.glide.Glide

class CommentAdapter(private val comments: MutableList<Comment>): RecyclerView.Adapter<CommentAdapter.Holder>() {
    private lateinit var binding: ItemCommentBinding
    private lateinit var context: Context
    private val userRef = Injection.provideUserCollection()

    fun addComment(comment: Comment) {
        comments.add(comment)
        notifyItemInserted((comments.size-1))
    }

    fun removeLastComment() {
        val position = comments.size-1
        comments.removeAt(position)
        notifyItemRemoved(position)
    }

    inner class Holder(binding: ItemCommentBinding): RecyclerView.ViewHolder(binding.root) {
        fun bind(comment: Comment) = with(binding) {
            setUserData(comment)
            commentTv.text = comment.comment
            commentDate.text = comment.createdAt?.convertToDate()
        }

        private fun setUserData(comment: Comment) = with(binding) {
            comment.userId?.let {
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
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Holder {
        context = parent.context
        binding = ItemCommentBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return Holder(binding)
    }

    override fun onBindViewHolder(holder: Holder, position: Int) {
        holder.bind(comments[position])
    }

    override fun getItemCount(): Int = comments.size

}