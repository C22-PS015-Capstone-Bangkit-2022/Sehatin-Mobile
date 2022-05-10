package com.app.sehatin.ui.activities.main

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.app.sehatin.R
import com.app.sehatin.databinding.ActivityMainBinding
import com.app.sehatin.ui.activities.main.fragments.exercise.ExerciseFragment
import com.app.sehatin.ui.activities.main.fragments.food.FoodFragment
import com.app.sehatin.ui.activities.main.fragments.home.HomeFragment
import com.app.sehatin.ui.activities.main.fragments.post.PostFragment
import com.app.sehatin.ui.activities.main.fragments.profile.ProfileFragment

class MainActivity : AppCompatActivity() {
    private lateinit var binding : ActivityMainBinding
    private lateinit var viewModel: MainViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        initVariable()
        initListener()
    }

    private fun initVariable() {
        viewModel = ViewModelProvider(this)[MainViewModel::class.java]
        setFragment(viewModel.selectedFragment)
    }

    private fun initListener() = with(binding) {
        bottomNavigationView.setOnItemSelectedListener { menu ->
            when(menu.itemId) {
                R.id.nav_home -> {
                    setFragment(HomeFragment())
                }
                R.id.nav_food -> {
                    setFragment(FoodFragment())
                }
                R.id.nav_post -> {
                    setFragment(PostFragment())
                }
                R.id.nav_exercise -> {
                    setFragment(ExerciseFragment())
                }
                R.id.nav_profile -> {
                    setFragment(ProfileFragment())
                }
            }
            true
        }
    }

    private fun setFragment(fragment: Fragment) {
        viewModel.selectedFragment = fragment
        supportFragmentManager
            .beginTransaction()
            .replace(R.id.fragmentContainer, fragment)
            .commit()
    }


}