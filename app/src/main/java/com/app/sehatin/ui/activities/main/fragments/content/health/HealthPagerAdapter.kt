@file:Suppress("DEPRECATION")

package com.app.sehatin.ui.activities.main.fragments.content.health

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentStatePagerAdapter

class HealthPagerAdapter(fm: FragmentManager, private val data: List<HealthPagerData>) : FragmentStatePagerAdapter(fm) {

    override fun getCount(): Int  = data.size

    override fun getItem(position: Int): Fragment {
        return data[position].fragment
    }

    override fun getPageTitle(position: Int): CharSequence {
        return data[position].title
    }

}

data class HealthPagerData(
    val fragment: Fragment,
    val title: String
)