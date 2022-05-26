package com.app.sehatin.ui.activities.main.fragments.content.adapter

import android.content.Context
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.app.sehatin.ui.activities.main.fragments.content.ContentViewModel

class ViewsAdapter(private val viewHolder : List<ViewHolder>, private val viewModel: ContentViewModel): RecyclerView.Adapter<ViewHolder>() {
    private lateinit var context: Context

    override fun getItemViewType(position: Int): Int {
        return position
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        context = parent.context
        return viewHolder[viewType]
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        viewHolder[position].bind(context, viewModel)
    }

    override fun getItemCount(): Int = viewHolder.size

}