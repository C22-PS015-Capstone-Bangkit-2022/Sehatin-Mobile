package com.app.sehatin.ui.activities.main.fragments.diagnosis.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.app.sehatin.data.model.ScreeningQuestion
import com.app.sehatin.databinding.ItemAskBinding

class ScreeningQuestionAdapter(private val questions: List<ScreeningQuestion>, private val onClickListener: OnClickListener): RecyclerView.Adapter<ScreeningQuestionAdapter.Holder>() {

    inner class Holder(private val binding: ItemAskBinding): RecyclerView.ViewHolder(binding.root) {
        fun bind(question: ScreeningQuestion) = with(binding) {
            title.text = question.question
            checkbox.setOnCheckedChangeListener { _, isChecked ->
                onClickListener.onCheckBoxClicked(isChecked, question)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Holder {
        val binding = ItemAskBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return Holder(binding)
    }

    override fun onBindViewHolder(holder: Holder, position: Int) {
        holder.bind(questions[position])
    }

    interface OnClickListener {
        fun onCheckBoxClicked(isChecked: Boolean, question: ScreeningQuestion)
    }

    override fun getItemCount(): Int = questions.size

}