package com.app.sehatin.ui.activities.main.fragments.content.adapter

import android.content.Context
import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.app.sehatin.ui.activities.main.fragments.content.ContentViewModel

abstract class ViewHolder(itemView: View): RecyclerView.ViewHolder(itemView) {
    abstract fun bind(context: Context, viewModel: ContentViewModel)
}