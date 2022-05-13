package com.app.sehatin.ui.activities.start.fragments.register

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.CheckBox
import androidx.recyclerview.widget.RecyclerView
import com.app.sehatin.data.model.Disease
import com.app.sehatin.databinding.ItemAskDiseaseBinding

class AskDiseasesAdapter(private val diseases: ArrayList<Disease>): RecyclerView.Adapter<AskDiseasesAdapter.Holder>() {

    private lateinit var onItemClickListener: OnItemClickListener

    inner class Holder(private val binding: ItemAskDiseaseBinding, private val onItemClickListener: OnItemClickListener): RecyclerView.ViewHolder(binding.root) {
        fun bind(disease: Disease) = with(binding) {
            diseaseName.text = disease.name
            this.root.setOnClickListener {
                checkbox.toggle()
                onItemClickListener.onViewClick(disease, checkbox)
            }
            checkbox.setOnClickListener {
                onItemClickListener.onCheckBoxClick(disease, checkbox)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AskDiseasesAdapter.Holder {
        val binding = ItemAskDiseaseBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return Holder(binding, onItemClickListener)
    }

    override fun onBindViewHolder(holder: AskDiseasesAdapter.Holder, position: Int) {
        holder.bind(diseases[position])
    }

    override fun getItemCount(): Int = diseases.size

    fun setListener(onItemClickListener: OnItemClickListener) {
        this.onItemClickListener = onItemClickListener
    }

    interface OnItemClickListener {
        fun onViewClick(disease: Disease, checkBox: CheckBox)
        fun onCheckBoxClick(disease: Disease, checkBox: CheckBox)
    }

}