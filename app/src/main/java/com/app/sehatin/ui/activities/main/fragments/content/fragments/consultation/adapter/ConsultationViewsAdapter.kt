package com.app.sehatin.ui.activities.main.fragments.content.fragments.consultation.adapter

import android.content.Context
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.app.sehatin.ui.activities.main.fragments.content.fragments.consultation.ConsultationViewModel

class ConsultationViewsAdapter(private val viewHolder : List<ConsultationViewHolder>, private val viewModel: ConsultationViewModel): RecyclerView.Adapter<ConsultationViewHolder>() {
    private lateinit var context: Context

    override fun getItemViewType(position: Int): Int {
        return position
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ConsultationViewHolder {
        context = parent.context
        return viewHolder[viewType]
    }

    override fun onBindViewHolder(holder: ConsultationViewHolder, position: Int) {
        viewHolder[position].bind(context, viewModel)
    }

    override fun getItemCount(): Int = viewHolder.size

}