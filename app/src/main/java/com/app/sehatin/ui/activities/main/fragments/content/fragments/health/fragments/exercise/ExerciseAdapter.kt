package com.app.sehatin.ui.activities.main.fragments.content.fragments.health.fragments.exercise

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.app.sehatin.data.model.Exercise
import com.app.sehatin.databinding.ItemExerciseBinding
import com.bumptech.glide.Glide

class ExerciseAdapter(private val exercises: List<Exercise>): RecyclerView.Adapter<ExerciseAdapter.Holder>() {
    private lateinit var binding: ItemExerciseBinding
    inner class Holder(binding: ItemExerciseBinding): RecyclerView.ViewHolder(binding.root) {
        fun bind(exercise: Exercise) = with(binding) {
            Glide.with(this.root)
                .load(exercise.thumbnail)
                .into(exerciseImage)
            exerciseName.text = exercise.name
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Holder {
        binding = ItemExerciseBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return Holder(binding)
    }

    override fun onBindViewHolder(holder: Holder, position: Int) {
        holder.bind(exercises[position])
    }

    override fun getItemCount(): Int = exercises.size
}