package com.app.sehatin.ui.activities.main.fragments.userDiseases

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.app.sehatin.data.model.Disease
import com.app.sehatin.databinding.ItemDiseaseBinding

class UserDiseasesAdapter(private val diseases: List<Disease>): RecyclerView.Adapter<UserDiseasesAdapter.Holder>() {

    class Holder(private val binding: ItemDiseaseBinding): RecyclerView.ViewHolder(binding.root) {
        fun bind(disease: Disease) = with(binding) {
            title.text = disease.diseaseName
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Holder {
        val binding = ItemDiseaseBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return Holder(binding)
    }

    override fun onBindViewHolder(holder: Holder, position: Int) {
        holder.bind(diseases[position])
    }

    override fun getItemCount(): Int = diseases.size

}