package com.app.sehatin.ui.activities.main.fragments.content

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import com.app.sehatin.R
import com.app.sehatin.databinding.FragmentContentBinding
import com.app.sehatin.ui.activities.main.MainViewModel
import com.app.sehatin.ui.activities.main.fragments.content.exercise.ExerciseFragment
import com.app.sehatin.ui.activities.main.fragments.content.food.FoodFragment
import com.app.sehatin.ui.activities.main.fragments.content.home.HomeFragment
import com.app.sehatin.ui.activities.main.fragments.content.post.PostFragment
import com.app.sehatin.ui.activities.main.fragments.content.profile.ProfileFragment

class ContentFragment : Fragment() {
    private lateinit var binding: FragmentContentBinding
    private lateinit var viewModel: MainViewModel

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        binding = FragmentContentBinding.inflate(inflater, container, false)
        initVariable()
        initListener()
        return binding.root
    }

    private fun initVariable() = with(binding) {
        viewModel = ViewModelProvider(this@ContentFragment)[MainViewModel::class.java]
        setFragment(viewModel.selectedFragment, viewModel.selectedItemId)
    }

    override fun onResume() {
        super.onResume()
        binding.bottomNavigationView.selectedItemId = viewModel.selectedItemId
    }

    private fun initListener() = with(binding) {
        bottomNavigationView.setOnItemSelectedListener { menu ->
            when(menu.itemId) {
                R.id.nav_home -> {
                    setFragment(HomeFragment(), R.id.nav_home)
                }
                R.id.nav_food -> {
                    setFragment(FoodFragment(), R.id.nav_food)
                }
                R.id.nav_post -> {
                    setFragment(PostFragment(), R.id.nav_post)
                }
                R.id.nav_exercise -> {
                    setFragment(ExerciseFragment(), R.id.nav_exercise)
                }
                R.id.nav_profile -> {
                    setFragment(ProfileFragment(), R.id.nav_profile)
                }
            }
            true
        }
    }

    private fun setFragment(fragment: Fragment, id: Int) {
        Log.d(TAG, "setFragment: $fragment")
        viewModel.selectedFragment = fragment
        viewModel.selectedItemId = id
        requireActivity().supportFragmentManager
            .beginTransaction()
            .replace(R.id.fragmentContainer, fragment)
            .commit()
    }

    private companion object {
        const val TAG = "ContentFragment"
    }
    
}