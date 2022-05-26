package com.app.sehatin.ui.activities.main.fragments.content.health

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.lifecycle.ViewModelProvider
import com.app.sehatin.R
import com.app.sehatin.databinding.FragmentHealthBinding
import com.app.sehatin.ui.activities.main.fragments.content.health.fragments.exercise.ExerciseFragment
import com.app.sehatin.ui.activities.main.fragments.content.health.fragments.food.FoodFragment
import com.app.sehatin.ui.viewmodel.ViewModelFactory
import com.google.android.material.tabs.TabLayoutMediator

class HealthFragment : Fragment() {
    private lateinit var binding: FragmentHealthBinding
    private lateinit var healthViewModel: HealthViewModel

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        binding = FragmentHealthBinding.inflate(inflater, container, false)
        initVariable()
        initListener()
        return binding.root
    }

    private fun initVariable() = with(binding) {
        healthViewModel = ViewModelProvider(this@HealthFragment, ViewModelFactory.getInstance())[HealthViewModel::class.java]
        val fragments = listOf(
            FoodFragment(healthViewModel),
            ExerciseFragment(healthViewModel)
        )
        viewPager.adapter = HealthPagerAdapter(this@HealthFragment, fragments)
        TabLayoutMediator(tabLayout, viewPager) { tab, position ->
            when (position) {
                0 -> {
                    tab.text = getString(R.string.food)
                    tab.icon = ContextCompat.getDrawable(requireContext(), R.drawable.ic_food)
                }
                1 -> {
                    tab.text = getString(R.string.exercise)
                    tab.icon = ContextCompat.getDrawable(requireContext(), R.drawable.ic_exercise)
                }
            }
        }.attach()
    }

    private fun initListener() {

    }

}