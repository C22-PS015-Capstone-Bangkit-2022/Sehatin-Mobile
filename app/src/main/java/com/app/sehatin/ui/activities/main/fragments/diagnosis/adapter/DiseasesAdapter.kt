package com.app.sehatin.ui.activities.main.fragments.diagnosis.adapter

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.app.sehatin.R
import com.app.sehatin.data.model.Disease
import com.app.sehatin.databinding.ItemAskBinding

class DiseasesAdapter(private val diseases: List<Disease>): RecyclerView.Adapter<DiseasesAdapter.Holder>() {
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
        holder.bind(diseases[position])
    }

    override fun getItemCount(): Int = diseases.size

    private companion object {
        const val TAG = "DiseasesAdapter"
    }

}