package com.app.sehatin.ui.activities.main.fragments.content.health

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.app.sehatin.R
import com.app.sehatin.databinding.FragmentHealthBinding
import com.app.sehatin.ui.activities.main.fragments.content.health.fragments.exercise.ExerciseFragment
import com.app.sehatin.ui.activities.main.fragments.content.health.fragments.food.FoodFragment

class HealthFragment : Fragment() {
    private lateinit var binding: FragmentHealthBinding

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        binding = FragmentHealthBinding.inflate(inflater, container, false)
        initVariable()
        initListener()
        return binding.root
    }

    private fun initVariable() = with(binding) {
        val healthPagerData = listOf(
            HealthPagerData(
                fragment = FoodFragment(),
                title = getString(R.string.food)
            ),
            HealthPagerData(
                fragment = ExerciseFragment(),
                title = getString(R.string.exercise)
            )
        )
        viewPager.adapter = HealthPagerAdapter(childFragmentManager, healthPagerData)
        tabLayout.setupWithViewPager(viewPager)
    }

    private fun initListener() {

    }

}