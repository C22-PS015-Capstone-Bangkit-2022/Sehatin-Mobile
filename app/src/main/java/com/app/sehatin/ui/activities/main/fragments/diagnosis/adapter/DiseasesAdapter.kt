package com.app.sehatin.ui.activities.main.fragments.diagnosis.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.app.sehatin.data.model.Disease
import com.app.sehatin.databinding.ItemAskBinding

class DiseasesAdapter(private val diseases: List<Disease>, private val onClickListener: OnClickListener): RecyclerView.Adapter<DiseasesAdapter.Holder>() {

    inner class Holder(private val binding: ItemAskBinding): RecyclerView.ViewHolder(binding.root) {
        fun bind(disease : Disease) = with(binding) {
            title.text = disease.diseaseName
            checkbox.setOnCheckedChangeListener { _, isChecked ->
                onClickListener.onCheckBoxClicked(isChecked, disease)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Holder {
        val binding = ItemAskBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return Holder(binding)
    }

    override fun onBindViewHolder(holder: Holder, position: Int) {
        holder.bind(diseases[position])
    }

    interface OnClickListener {
        fun onCheckBoxClicked(isChecked: Boolean, disease: Disease)
    }

    override fun getItemCount(): Int = diseases.size

}