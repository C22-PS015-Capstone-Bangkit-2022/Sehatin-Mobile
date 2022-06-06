package com.app.sehatin.ui.activities.main.fragments.content.fragments.home.viewHolder

import android.content.Context
import android.content.Intent
import android.util.Log
import android.view.View
import androidx.lifecycle.LifecycleOwner
import androidx.recyclerview.widget.LinearLayoutManager
import com.app.sehatin.R
import com.app.sehatin.data.Result
import com.app.sehatin.data.model.Food
import com.app.sehatin.databinding.ItemHomeFoodBinding
import com.app.sehatin.ui.activities.main.fragments.content.ContentViewModel
import com.app.sehatin.ui.activities.main.fragments.content.fragments.home.adapter.HorizontalFoodAdapter
import com.app.sehatin.ui.activities.objectDetection.ObjectDetectionActivity
import com.app.sehatin.ui.activities.main.fragments.content.adapter.ContentViewHolder
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.firebase.auth.FirebaseAuth

class HomeFoodHolderContent(
    itemView: View,
    private val bottomNavigationView: BottomNavigationView?,
    private val lifecycleOwner: LifecycleOwner
    ): ContentViewHolder(itemView) {

    private val binding = ItemHomeFoodBinding.bind(itemView)
    private lateinit var viewModel: ContentViewModel
    private lateinit var context: Context

    override fun bind(context: Context, viewModel: ContentViewModel) {
        this.viewModel = viewModel
        this.context = context
        initListener()
        getData()
    }

    private fun initListener() = with(binding) {
        CameraMLBtn.setOnClickListener {
            context.startActivity(Intent(context, ObjectDetectionActivity::class.java))
        }
        otherBtn.setOnClickListener {
            bottomNavigationView?.selectedItemId = R.id.nav_health
        }
    }

    private fun getData() {
        val mUser = FirebaseAuth.getInstance().currentUser
        mUser?.getIdToken(true)?.addOnCompleteListener { task ->
            if (task.isSuccessful) {
                val idToken = task.result.token
                idToken?.let { getGoodFoods(it) }
            } else {
                Log.e(TAG, "getIdToken: ${task.exception}")
            }
        }
    }

    private fun getGoodFoods(idToken: String) {
        if(viewModel.goodFoods.isEmpty()) {
            viewModel.getGoodFoods(idToken).observe(lifecycleOwner) {
                when(it) {
                    is Result.Loading -> {
                        showLoading(true)
                    }
                    is Result.Error -> {
                        Log.e(TAG, "getGoodFoods: ${it.error}")
                    }
                    is Result.Success -> {
                        Log.d(TAG, "getGoodFoods: ${it.data?.food?.size}")
                        val data = it.data
                        if(data != null) {
                            val ok = data.ok
                            ok?.let { isOk ->
                                if(isOk) {
                                    data.food?.let { foods ->
                                        viewModel.goodFoods.addAll(foods)
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
            setRvFoods(viewModel.goodFoods)
        }
    }

    private fun setRvFoods(foods: List<Food>) = with(binding) {
        rvFoods.setHasFixedSize(true)
        rvFoods.layoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
        rvFoods.adapter = HorizontalFoodAdapter(foods)
    }

    private fun showLoading(isLoading: Boolean) = with(binding) {
        if(isLoading) {
            contentGroup.visibility = View.GONE
            shimmerLoading.visibility = View.VISIBLE
        } else {
            contentGroup.visibility = View.VISIBLE
            shimmerLoading.visibility = View.GONE
        }
    }

    private companion object {
        const val TAG = "HomeFoodHolder"
    }
}