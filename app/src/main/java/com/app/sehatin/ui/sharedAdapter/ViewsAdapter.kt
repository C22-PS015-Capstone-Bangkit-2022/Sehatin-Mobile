package com.app.sehatin.ui.sharedAdapter

import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView

class ViewsAdapter(private val viewHolder : List<ViewHolder>): RecyclerView.Adapter<ViewHolder>() {

    override fun getItemViewType(position: Int): Int {
        return position
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return viewHolder[viewType]
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        viewHolder[position].bind()
    }

    override fun getItemCount(): Int = viewHolder.size

}