package com.app.sehatin.ui.sharedAdapter

import android.content.Context
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView

class ViewsAdapter(private val viewHolder : List<ViewHolder>): RecyclerView.Adapter<ViewHolder>() {
    private lateinit var context: Context

    override fun getItemViewType(position: Int): Int {
        return position
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        context = parent.context
        return viewHolder[viewType]
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        viewHolder[position].bind(context)
    }

    override fun getItemCount(): Int = viewHolder.size

}