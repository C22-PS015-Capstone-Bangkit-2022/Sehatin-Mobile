package com.app.sehatin.ui.activities.main.fragments.diagnosis.adapter

import android.annotation.SuppressLint
import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.app.sehatin.R
import com.app.sehatin.data.model.ScreeningQuestion
import com.app.sehatin.databinding.ItemAskBinding

class ScreeningQuestionAdapter(private val onAnswerListen: () -> Unit): ListAdapter<ScreeningQuestion, ScreeningQuestionAdapter.Holder>(DIFF_CALLBACK) {
    private lateinit var binding: ItemAskBinding
    private lateinit var context: Context
    var answeredQuestion = mutableListOf<ScreeningQuestion>()

    inner class Holder(binding: ItemAskBinding): RecyclerView.ViewHolder(binding.root) {
        fun bind(question: ScreeningQuestion) = with(binding) {
            title.text = question.question
            radioBtnGroup.setOnCheckedChangeListener { _, id ->
                when(id) {
                    R.id.yesBtn -> {
                        answer(question, true)
                    }
                    R.id.noBtn -> {
                        answer(question, false)
                    }
                }
            }
        }

        private fun answer(question: ScreeningQuestion, answer: Boolean) {
            question.answer = answer
            if(!answeredQuestion.contains(question)) {
                answeredQuestion.add(question)
                onAnswerListen()
            } else {
                val index = answeredQuestion.indexOf(question)
                answeredQuestion.removeAt(index)
                answeredQuestion.add(index, question)
            }
            Log.d(TAG, "answer: $answeredQuestion")
        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Holder {
        context = parent.context
        binding = ItemAskBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return Holder(binding)
    }

    override fun onBindViewHolder(holder: Holder, position: Int) {
        val question = getItem(position)
        holder.bind(question)
    }

    companion object {
        private const val TAG = "ScreeningQuestionAdapter"
        val DIFF_CALLBACK: DiffUtil.ItemCallback<ScreeningQuestion> =
            object : DiffUtil.ItemCallback<ScreeningQuestion>() {
                override fun areItemsTheSame(oldUser: ScreeningQuestion, newUser: ScreeningQuestion): Boolean {
                    return oldUser.question == newUser.question
                }

                @SuppressLint("DiffUtilEquals")
                override fun areContentsTheSame(oldUser: ScreeningQuestion, newUser: ScreeningQuestion): Boolean {
                    return oldUser == newUser
                }
            }
    }

}