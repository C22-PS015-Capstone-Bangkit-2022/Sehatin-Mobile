package com.app.sehatin.ui.activities.main.fragments.content.health

import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter

class HealthPagerAdapter(fragment: Fragment, private val data: List<Fragment>) : FragmentStateAdapter(fragment) {

    override fun getItemCount(): Int = data.size

    override fun createFragment(position: Int): Fragment {
        return data[position]
    }

}
