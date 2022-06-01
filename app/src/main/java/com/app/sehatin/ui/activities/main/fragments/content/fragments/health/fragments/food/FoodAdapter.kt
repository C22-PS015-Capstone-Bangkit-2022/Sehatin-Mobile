package com.app.sehatin.ui.activities.main.fragments.content.fragments.health.fragments.food

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.app.sehatin.data.model.Food
import com.app.sehatin.databinding.ItemFoodBinding
import com.bumptech.glide.Glide

class FoodAdapter(private val foods: List<Food>): RecyclerView.Adapter<FoodAdapter.Holder>() {

    private lateinit var binding: ItemFoodBinding
    inner class Holder(binding: ItemFoodBinding): RecyclerView.ViewHolder(binding.root) {
        fun bind(food: Food) = with(binding) {
            Glide.with(this.root)
                .load(food.thumbnail_image)
                .into(foodImage)
            foodName.text = food.nameId
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Holder {
        binding = ItemFoodBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return Holder(binding)
    }

    override fun onBindViewHolder(holder: Holder, position: Int) {
        holder.bind(foods[position])
    }

    override fun getItemCount(): Int = foods.size


}