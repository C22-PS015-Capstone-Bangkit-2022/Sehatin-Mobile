package com.app.sehatin.ui.activities.main.fragments.content.home.viewHolder

import android.content.Context
import androidx.lifecycle.LifecycleOwner
import com.app.sehatin.databinding.ItemArticleBinding
import com.app.sehatin.ui.activities.main.fragments.content.home.HomeViewModel
import com.app.sehatin.ui.sharedAdapter.ViewHolder

class HomeArticleHolder(
    private val binding: ItemArticleBinding,
    private val homeViewModel: HomeViewModel,
    private val lifecycleOwner: LifecycleOwner
) : ViewHolder(binding.root) {
    private lateinit var context: Context
    override fun bind(context: Context) {
        this.context = context
        initVariable()
        initListener()
    }

    private fun initVariable() = with(binding) {

    }

    private fun initListener() = with(binding) {

    }

}