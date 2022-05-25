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
import com.app.sehatin.data.model.Disease
import com.app.sehatin.databinding.ItemAskBinding

class DiseasesAdapter(private val onAnswerListen: () -> Unit): ListAdapter<Disease, DiseasesAdapter.Holder>(DIFF_CALLBACK) {
    private lateinit var binding: ItemAskBinding
    private lateinit var context: Context
    var answeredDiseases = mutableListOf<Disease>()

    inner class Holder(binding: ItemAskBinding): RecyclerView.ViewHolder(binding.root) {
        fun bind(disease : Disease) = with(binding) {
            title.text = disease.diseaseName
            radioBtnGroup.setOnCheckedChangeListener { _, id ->
                when(id) {
                    R.id.yesBtn -> {
                        answer(disease, true)
                    }
                    R.id.noBtn -> {
                        answer(disease, false)
                    }
                }
            }
        }

        private fun answer(disease : Disease, answer: Boolean) {
            disease.answer = answer
            if(!answeredDiseases.contains(disease)) {
                answeredDiseases.add(disease)
                onAnswerListen()
            } else {
                val index = answeredDiseases.indexOf(disease)
                answeredDiseases.removeAt(index)
                answeredDiseases.add(index, disease)
            }
            Log.d(TAG, "answer: $answeredDiseases")
        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Holder {
        context = parent.context
        binding = ItemAskBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return Holder(binding)
    }

    override fun onBindViewHolder(holder: Holder, position: Int) {
        val disease = getItem(position)
        holder.bind(disease)
    }

    companion object {
        private const val TAG = "DiseasesAdapter"
        val DIFF_CALLBACK: DiffUtil.ItemCallback<Disease> = object : DiffUtil.ItemCallback<Disease>() {
                override fun areItemsTheSame(oldUser: Disease, newUser: Disease): Boolean {
                    return oldUser.id == newUser.id
                }

                @SuppressLint("DiffUtilEquals")
                override fun areContentsTheSame(oldUser: Disease, newUser: Disease): Boolean {
                    return oldUser == newUser
                }
            }
    }

}