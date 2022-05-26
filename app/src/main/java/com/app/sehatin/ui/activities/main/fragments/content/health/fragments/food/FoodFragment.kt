package com.app.sehatin.ui.activities.main.fragments.content.health.fragments.food

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.app.sehatin.R
import com.app.sehatin.databinding.FragmentFoodBinding

class FoodFragment : Fragment() {
    private lateinit var binding: FragmentFoodBinding

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        binding = FragmentFoodBinding.inflate(inflater, container, false)
        return binding.root
    }

}