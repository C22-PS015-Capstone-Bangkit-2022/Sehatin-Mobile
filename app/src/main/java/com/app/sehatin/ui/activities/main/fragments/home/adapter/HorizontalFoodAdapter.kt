package com.app.sehatin.ui.activities.main.fragments.home.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.app.sehatin.data.model.Food
import com.app.sehatin.databinding.ItemFoodHorizontalBinding
import com.bumptech.glide.Glide

class HorizontalFoodAdapter(private val foods: List<Food>): RecyclerView.Adapter<HorizontalFoodAdapter.Holder>() {

    private lateinit var binding: ItemFoodHorizontalBinding
    inner class Holder(binding: ItemFoodHorizontalBinding): RecyclerView.ViewHolder(binding.root) {
        fun bind(food: Food) = with(binding) {
            Glide.with(this.root)
                .load(food.thumbnail)
                .into(foodImage)
            foodName.text = food.name
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HorizontalFoodAdapter.Holder {
        binding = ItemFoodHorizontalBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return Holder(binding)
    }

    override fun onBindViewHolder(holder: HorizontalFoodAdapter.Holder, position: Int) {
        holder.bind(foods[position])
    }

    override fun getItemCount(): Int = foods.size


}