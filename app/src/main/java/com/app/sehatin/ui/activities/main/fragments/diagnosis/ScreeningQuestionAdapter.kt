package com.app.sehatin.ui.activities.main.fragments.diagnosis

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.app.sehatin.data.model.ScreeningQuestion
import com.app.sehatin.databinding.ItemAskDiseaseBinding

class ScreeningQuestionAdapter(private val questions: List<ScreeningQuestion>): RecyclerView.Adapter<ScreeningQuestionAdapter.Holder>() {

    inner class Holder(private val binding: ItemAskDiseaseBinding): RecyclerView.ViewHolder(binding.root) {
        fun bind(question: ScreeningQuestion) = with(binding) {
            title.text = question.question
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Holder {
        val binding = ItemAskDiseaseBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return Holder(binding)
    }

    override fun onBindViewHolder(holder: Holder, position: Int) {
        holder.bind(questions[position])
    }

    override fun getItemCount(): Int = questions.size

}