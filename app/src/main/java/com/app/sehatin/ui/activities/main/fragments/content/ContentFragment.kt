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
import com.app.sehatin.ui.activities.main.fragments.content.fragments.article.ArticleFragment
import com.app.sehatin.ui.activities.main.fragments.content.fragments.health.HealthFragment
import com.app.sehatin.ui.activities.main.fragments.content.fragments.home.HomeFragment
import com.app.sehatin.ui.activities.main.fragments.content.fragments.post.PostFragment
import com.app.sehatin.ui.activities.main.fragments.content.fragments.profile.ProfileFragment
import com.app.sehatin.ui.viewmodel.ViewModelFactory

class ContentFragment : Fragment() {
    private lateinit var binding: FragmentContentBinding
    private lateinit var viewModel: ContentViewModel

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        binding = FragmentContentBinding.inflate(inflater, container, false)
        initVariable()
        initListener()
        return binding.root
    }

    private fun initVariable() {
        viewModel = ViewModelProvider(this@ContentFragment, ViewModelFactory.getInstance())[ContentViewModel::class.java]
    }

    override fun onResume() {
        super.onResume()
        binding.bottomNavigationView.selectedItemId = viewModel.selectedItemId
    }

    private fun initListener() = with(binding) {
        bottomNavigationView.setOnItemSelectedListener { menu ->
            when(menu.itemId) {
                R.id.nav_home -> {
                    setFragment(HomeFragment(viewModel, bottomNavigationView), R.id.nav_home)
                }
                R.id.nav_health -> {
                    setFragment(HealthFragment(), R.id.nav_health)
                }
                R.id.nav_post -> {
                    setFragment(PostFragment(), R.id.nav_post)
                }
                R.id.nav_article -> {
                    setFragment(ArticleFragment(), R.id.nav_article)
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