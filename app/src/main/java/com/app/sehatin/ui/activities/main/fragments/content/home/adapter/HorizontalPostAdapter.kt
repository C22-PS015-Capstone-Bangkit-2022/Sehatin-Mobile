package com.app.sehatin.ui.activities.main.fragments.content.home.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.app.sehatin.R
import com.app.sehatin.data.model.Posting
import com.app.sehatin.data.model.User
import com.app.sehatin.databinding.ItemPostHorizontalBinding
import com.app.sehatin.injection.Injection
import com.app.sehatin.utils.DEFAULT
import com.app.sehatin.utils.convertToDate
import com.bumptech.glide.Glide

class HorizontalPostAdapter(private val posts: List<Posting>): RecyclerView.Adapter<HorizontalPostAdapter.Holder>() {
    private lateinit var binding: ItemPostHorizontalBinding
    private lateinit var context: Context
    private var userRef = Injection.provideUserCollection()

    inner class Holder(private val binding: ItemPostHorizontalBinding): RecyclerView.ViewHolder(binding.root) {
        fun bind(posting: Posting) = with(binding) {
            setUser(posting.userId)
            setTags(posting.tags)
            if(posting.hasImage) {
                posting.image?.let {
                    Glide.with(context)
                        .load(it)
                        .placeholder(R.color.grey2)
                        .into(postImage)
                }
            } else {
                postImage.visibility = View.GONE
            }
            postDate.text = posting.createdAt?.convertToDate()
            postDescription.text = posting.description
        }

        private fun setTags(tags: List<String>?) = with(binding) {
            val tagBuilder = StringBuilder("tags : ")
            tags?.let { listTag ->
                for(tag in listTag) {
                   tagBuilder.append("#$tag ")
                }
                tagsTv.text = tagBuilder
            }
        }

        private fun setUser(userId: String?) = with(binding) {
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
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Holder {
        context = parent.context
        binding = ItemPostHorizontalBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return Holder(binding)
    }

    override fun onBindViewHolder(holder: Holder, position: Int) {
        holder.bind(posts[position])
    }

    override fun getItemCount(): Int = posts.size

}