package com.app.sehatin.ui.activities.main.fragments.content.fragments.consultation.adapter

import android.content.Context
import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.app.sehatin.ui.activities.main.fragments.content.fragments.consultation.ConsultationViewModel

abstract class ConsultationViewHolder(itemView: View): RecyclerView.ViewHolder(itemView) {
    abstract fun bind(context: Context, viewModel: ConsultationViewModel)
}