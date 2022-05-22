package com.app.sehatin.ui.activities.main.fragments.diagnosis.adapter

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.app.sehatin.R
import com.app.sehatin.data.model.ScreeningQuestion
import com.app.sehatin.databinding.ItemAskBinding

class ScreeningQuestionAdapter(private val questions: List<ScreeningQuestion>, private val onClickListener: OnClickListener): RecyclerView.Adapter<ScreeningQuestionAdapter.Holder>() {
    private lateinit var binding: ItemAskBinding
    private lateinit var context: Context
    var answeredQuestion = mutableListOf<ScreeningQuestion>()

    inner class Holder(binding: ItemAskBinding): RecyclerView.ViewHolder(binding.root) {
        fun bind(question: ScreeningQuestion) = with(binding) {
            title.text = question.question
            binding.yesBtn.setOnClickListener {
                answer(question, ScreeningQuestion.YES)
                onClickListener.onAnswerClick(true, question)
            }
            binding.noBtn.setOnClickListener {
                answer(question, ScreeningQuestion.NO)
                onClickListener.onAnswerClick(false, question)
            }
        }

        private fun answer(question: ScreeningQuestion, answer: String) {
            if(!answeredQuestion.contains(question)) {
                answeredQuestion.add(question)
            }
            question.answer = answer
            notifyItemChanged(bindingAdapterPosition)
            Log.d(TAG, "answer ${answeredQuestion.size}: $answeredQuestion")
        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Holder {
        context = parent.context
        binding = ItemAskBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return Holder(binding)
    }

    override fun onBindViewHolder(holder: Holder, position: Int) {
        val question = questions[position]
        holder.bind(question)
        toggleBackground(question.answer)
    }

    private fun toggleBackground(answer: String?) = with(binding) {
        when(answer) {
            ScreeningQuestion.YES -> {
                yesBtn.background = ContextCompat.getDrawable(context, R.drawable.circle_primary_background)
                noBtn.background = ContextCompat.getDrawable(context, R.drawable.circle_stroke_background)
            }
            ScreeningQuestion.NO -> {
                yesBtn.background = ContextCompat.getDrawable(context, R.drawable.circle_stroke_background)
                noBtn.background = ContextCompat.getDrawable(context, R.drawable.circle_primary_background)
            }
            else -> {
                yesBtn.background = ContextCompat.getDrawable(context, R.drawable.circle_stroke_background)
                noBtn.background = ContextCompat.getDrawable(context, R.drawable.circle_stroke_background)
            }
        }
    }
    interface OnClickListener {
        fun onAnswerClick(answer: Boolean, question: ScreeningQuestion)
    }

    override fun getItemCount(): Int = questions.size

    private companion object {
        const val TAG = "ScreeningQuestionAdapter"
    }

}