package com.app.sehatin.ui.activities.main

import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModel
import com.app.sehatin.ui.activities.main.fragments.home.HomeFragment

class MainViewModel: ViewModel() {

    var selectedFragment: Fragment = HomeFragment()

}