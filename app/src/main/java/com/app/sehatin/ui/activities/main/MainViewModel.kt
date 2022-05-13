package com.app.sehatin.ui.activities.main

import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModel
import com.app.sehatin.R
import com.app.sehatin.ui.activities.main.fragments.content.home.HomeFragment

class MainViewModel: ViewModel() {

    var selectedFragment: Fragment = HomeFragment()
    var selectedItemId = R.id.nav_home

}