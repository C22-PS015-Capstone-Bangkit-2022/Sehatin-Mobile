package com.app.sehatin.ui.sharedAdapter

import android.view.View
import androidx.recyclerview.widget.RecyclerView

abstract class ViewHolder(itemView: View): RecyclerView.ViewHolder(itemView) {
    abstract fun bind()
}