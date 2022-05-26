package com.app.sehatin.ui.activities.main.fragments.content.fragments.home.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.app.sehatin.data.model.Exercise
import com.app.sehatin.databinding.ItemExerciseHorizontalBinding
import com.bumptech.glide.Glide

class HorizontalExerciseAdapter(private val exercises: List<Exercise>): RecyclerView.Adapter<HorizontalExerciseAdapter.Holder>() {
    private lateinit var binding: ItemExerciseHorizontalBinding

    inner class Holder(binding: ItemExerciseHorizontalBinding): RecyclerView.ViewHolder(binding.root) {
        fun bind(exercise: Exercise) = with(binding) {
            Glide.with(this.root)
                .load(exercise.thumbnail)
                .into(exerciseImage)
            exerciseName.text = exercise.name
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Holder {
        binding = ItemExerciseHorizontalBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return Holder(binding)
    }

    override fun onBindViewHolder(holder: Holder, position: Int) {
        holder.bind(exercises[position])
    }

    override fun getItemCount(): Int = exercises.size

}