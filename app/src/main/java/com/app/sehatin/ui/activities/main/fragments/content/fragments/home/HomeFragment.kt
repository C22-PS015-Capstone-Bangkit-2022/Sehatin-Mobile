package com.app.sehatin.ui.activities.main.fragments.content.fragments.home

import android.graphics.Color
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import androidx.core.content.ContextCompat
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.app.sehatin.R
import com.app.sehatin.databinding.*
import com.app.sehatin.ui.activities.main.fragments.content.ContentFragment
import com.app.sehatin.ui.activities.main.fragments.content.fragments.home.viewHolder.*
import com.app.sehatin.ui.activities.main.fragments.content.adapter.ViewHolder
import com.app.sehatin.ui.activities.main.fragments.content.adapter.ViewsAdapter
import kotlinx.coroutines.launch
import kotlin.math.abs

class HomeFragment : Fragment() {
    private lateinit var binding : FragmentHomeBinding
    private lateinit var homeUiAdapter: ViewsAdapter
    private var listHomeUi = mutableListOf<ViewHolder>()
    private val viewModel = ContentFragment.viewModel

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        binding = FragmentHomeBinding.inflate(inflater, container, false)
        initVariable()
        initListener()
        return binding.root
    }

    @Suppress("DEPRECATION")
    override fun onResume() {
        super.onResume()
        requireActivity().window.decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_LAYOUT_STABLE or View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
        setWindowFlag(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS, false)
        requireActivity().window.statusBarColor = Color.TRANSPARENT
    }

    @Suppress("DEPRECATION")
    override fun onStop() {
        super.onStop()
        requireActivity().window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS)
        requireActivity().window.statusBarColor = ContextCompat.getColor(requireContext(), R.color.primary)
        requireActivity().window.decorView.systemUiVisibility = View.VISIBLE
    }

    private fun initListener() = with(binding) {
        lifecycleScope.launch {
            appbarChangeColorListener()
        }
        refreshLayout.setOnRefreshListener {
            viewModel.clearHomeFragmentState()
            initVariable()
            refreshLayout.isRefreshing = false
        }
    }

    private fun initVariable() = with(binding) {
        listHomeUi = mutableListOf(
            HomeTopHolder(
                this@HomeFragment,
                ItemHomeTopBinding.inflate(LayoutInflater.from(requireContext()), binding.root, false).root,
                viewLifecycleOwner
            ),
            HomeFoodHolder(
                ItemHomeFoodBinding.inflate(LayoutInflater.from(requireContext()), binding.root, false).root,
                ContentFragment.bottomNavigationView,
                viewLifecycleOwner
            ),
            HomeExercisesHolder(
                ItemHomeExercisesBinding.inflate(LayoutInflater.from(requireContext()), binding.root, false).root,
                ContentFragment.bottomNavigationView,
                viewLifecycleOwner
            ),
            HomePostHolder(
                ItemHomePostBinding.inflate(LayoutInflater.from(requireContext()), binding.root, false),
                viewLifecycleOwner
            ),
            HomeArticleHolder(
                this@HomeFragment,
                ItemHomeArticleBinding.inflate(LayoutInflater.from(requireContext()), binding.root, false),
                viewLifecycleOwner
            )
        )
        homeUiAdapter = ViewsAdapter(listHomeUi, viewModel)
        rvUi.layoutManager = LinearLayoutManager(requireContext())
        rvUi.adapter = homeUiAdapter
    }

    private fun appbarChangeColorListener() = with(binding) {
        var floatValue = 1f
        var totalValue = 550
        rvUi.setOnScrollChangeListener { _, _, _, _, oldScrollY ->
            if(oldScrollY < 0) { //scroll ke bawah
                totalValue -= abs(oldScrollY)
                if(totalValue >= 0) {
                    if(floatValue > 0) {
                        floatValue -= abs(oldScrollY.toFloat()/400f)
                        if(floatValue < 0f) {
                            floatValue = 0f
                        }
                    }
                }
            } else { //scroll ke atas
                totalValue += abs(oldScrollY)
                if(totalValue > 550) {
                    totalValue = 550
                }
                if(floatValue < 1.0f) {
                    floatValue += abs(oldScrollY.toFloat()/400f)
                    if(floatValue > 1f) {
                        floatValue = 1f
                    }
                }
            }

            if(totalValue < 0 && floatValue != 0.0f) {
                setBackgroundColorOfTopBar(0.0f)
                floatValue = 0.0f
            }

            if(totalValue in 1..550) {
                setBackgroundColorOfTopBar(floatValue)
            }

            if(totalValue == 550) {
                setBackgroundColorOfTopBar(2.0f)
            }
        }
    }

    private fun setBackgroundColorOfTopBar(value: Float) {
        when (value) {
            in 0.0..0.1 -> {
                setColor(R.color.p_1)
            }
            in 0.1..0.2 -> {
                setColor(R.color.p_09)
            }
            in 0.2..0.3 -> {
                setColor(R.color.p_08)
            }
            in 0.3..0.4 -> {
                setColor(R.color.p_07)
            }
            in 0.4..0.5 -> {
                setColor(R.color.p_06)
            }
            in 0.5..0.6 -> {
                setColor(R.color.p_05)
            }
            in 0.6..0.7 -> {
                setColor(R.color.p_04)
            }
            in 0.7..0.8 -> {
                setColor(R.color.p_03)
            }
            in 0.8..0.9 -> {
                setColor(R.color.p_02)
            }
            in 0.9..1.0 -> {
                setColor(R.color.p_01)
            }
            else -> {
                setColor(null)
            }
        }
    }

    private fun setColor(id: Int?) = with(binding) {
        if(id != null) {
            homeAppbar.parentLayout.setBackgroundColor(ContextCompat.getColor(requireContext(), id))
        } else {
            homeAppbar.parentLayout.background = null
        }
    }

    @Suppress("SameParameterValue")
    private fun setWindowFlag(bits: Int, on: Boolean) {
        val win = requireActivity().window
        val winParams = win.attributes
        if (on) {
            winParams.flags = winParams.flags or bits
        } else {
            winParams.flags = winParams.flags and bits.inv()
        }
        win.attributes = winParams
    }

}