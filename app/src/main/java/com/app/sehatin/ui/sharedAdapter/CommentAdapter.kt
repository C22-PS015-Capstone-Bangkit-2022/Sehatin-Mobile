package com.app.sehatin.ui.sharedAdapter

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.app.sehatin.data.model.Comment
import com.app.sehatin.databinding.ItemCommentBinding
import com.app.sehatin.injection.Injection
import com.app.sehatin.utils.convertToDate

class CommentAdapter(private val comments: List<Comment>): RecyclerView.Adapter<CommentAdapter.Holder>() {
    private lateinit var binding: ItemCommentBinding
    private lateinit var context: Context
    private val userRef = Injection.provideUserCollection()

    inner class Holder(binding: ItemCommentBinding): RecyclerView.ViewHolder(binding.root) {
        fun bind(comment: Comment) = with(binding) {
            setUserData(comment)
            commentTv.text = comment.comment
            commentDate.text = comment.createdAt?.convertToDate()
        }

        private fun setUserData(comment: Comment) {
            comment.userId?.let {

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