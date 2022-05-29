package com.app.sehatin.ui.activities.main.fragments.content.fragments.health.fragments.food

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.GridLayoutManager
import com.app.sehatin.data.Result
import com.app.sehatin.data.model.Food
import com.app.sehatin.databinding.FragmentFoodBinding
import com.app.sehatin.ui.activities.main.fragments.content.ContentFragment
import com.google.firebase.auth.FirebaseAuth

class FoodFragment : Fragment() {
    private lateinit var binding: FragmentFoodBinding
    private var viewModel = ContentFragment.viewModel
    private lateinit var token: String

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        binding = FragmentFoodBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initListener()
    }

    private fun initListener() = with(binding) {
        val mUser = FirebaseAuth.getInstance().currentUser
        mUser?.getIdToken(true)?.addOnCompleteListener { task ->
            if (task.isSuccessful) {
                val idToken = task.result.token
                idToken?.let {
                    token = it
                    getGoodFoods(it)
                }
            } else {
                Log.e(TAG, "getIdToken: ${task.exception}")
            }
        }
        refreshLayout.setOnRefreshListener {
            viewModel.clearFoodFragmentState()
            getGoodFoods(token)
            refreshLayout.isRefreshing = false
        }
    }

    private fun getGoodFoods(idToken: String) {
        try {
            if(viewModel.healthGoodFoods.isEmpty()) {
                viewModel.getGoodFoods(idToken).observe(viewLifecycleOwner) {
                    when(it) {
                        is Result.Loading -> {
                            Log.d(TAG, "getGoodFoods: loading")
                            showLoading(true)
                        }
                        is Result.Error -> {
                            Log.e(TAG, "getGoodFoods error: ${it.error}")
                        }
                        is Result.Success -> {
                            Log.d(TAG, "getGoodFoods success: ${it.data?.food?.size}")
                            val data = it.data
                            if(data != null) {
                                val ok = data.ok
                                ok?.let { isOk ->
                                    if(isOk) {
                                        data.food?.let { foods ->
                                            viewModel.healthGoodFoods.addAll(foods)
                                            showLoading(false)
                                            setRvFoods(foods)
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            } else {
                showLoading(false)
                setRvFoods(viewModel.healthGoodFoods)
            }
        } catch (e: Exception) {
            Log.e(TAG, "getGoodFoods: $e")
        }
    }

    private fun setRvFoods(foods: List<Food>) = with(binding) {
        rvFoods.setHasFixedSize(true)
        rvFoods.layoutManager = GridLayoutManager(requireContext(), 2)
        rvFoods.adapter = FoodAdapter(foods)
    }

    private fun showLoading(isLoading: Boolean) = with(binding) {
        if(isLoading) {
            shimmerLoading.root.visibility = View.VISIBLE
            rvFoods.visibility = View.GONE
        } else {
            shimmerLoading.root.visibility = View.GONE
            rvFoods.visibility = View.VISIBLE
        }
    }

    private companion object {
        const val TAG = "FoodFragment"
    }

}