package com.app.sehatin.ui.activities.main.fragments.content.fragments.home.viewHolder

import android.content.Context
import android.util.Log
import android.view.View
import androidx.lifecycle.LifecycleOwner
import androidx.recyclerview.widget.LinearLayoutManager
import com.app.sehatin.R
import com.app.sehatin.data.Result
import com.app.sehatin.data.model.Exercise
import com.app.sehatin.databinding.ItemHomeExercisesBinding
import com.app.sehatin.ui.activities.main.fragments.content.ContentViewModel
import com.app.sehatin.ui.activities.main.fragments.content.fragments.home.adapter.HorizontalExerciseAdapter
import com.app.sehatin.ui.activities.main.fragments.content.adapter.ContentViewHolder
import com.app.sehatin.ui.activities.main.fragments.content.fragments.health.HealthFragment
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.firebase.auth.FirebaseAuth

class HomeExercisesHolderContent(
    itemView: View,
    private val bottomNavigationView: BottomNavigationView?,
    private val lifecycleOwner: LifecycleOwner
    ): ContentViewHolder(itemView) {

    private lateinit var context: Context
    private lateinit var viewModel: ContentViewModel
    private val binding = ItemHomeExercisesBinding.bind(itemView)

    override fun bind(context: Context, viewModel: ContentViewModel) {
        this.context = context
        this.viewModel = viewModel
        initListener()
        getData()
    }

    private fun initListener() = with(binding) {
        otherBtn.setOnClickListener {
            bottomNavigationView?.selectedItemId = R.id.nav_health
            HealthFragment.selectedViewPagerItem = 1
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
        if(viewModel.goodExercises.isEmpty()) {
            viewModel.getGoodExercises(idToken).observe(lifecycleOwner) {
                when(it) {
                    is Result.Loading -> {
                        Log.d(TAG, "getGoodFoods: loading")
                        showLoading(true)
                    }
                    is Result.Error -> {
                        Log.e(TAG, "getExercises: ${it.error}")
                    }
                    is Result.Success -> {
                        Log.d(TAG, "getExercises: ${it.data?.sport?.size}")
                        val data = it.data
                        if(data != null) {
                            val ok = data.ok
                            ok?.let { isOk ->
                                if(isOk) {
                                    data.sport?.let { exercises ->
                                        viewModel.goodExercises.addAll(exercises)
                                        showLoading(false)
                                        setRvExercises(exercises)
                                    }
                                }
                            }
                        }
                    }
                }
            }
        } else {
            showLoading(false)
            setRvExercises(viewModel.goodExercises)
        }
    }

    private fun setRvExercises(exercises: List<Exercise>) = with(binding) {
        val exerciseAdapter = HorizontalExerciseAdapter(exercises)
        rvExercises.setHasFixedSize(true)
        rvExercises.layoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
        rvExercises.adapter = exerciseAdapter
    }

    private fun showLoading(isLoading: Boolean) = with(binding) {
        if(isLoading) {
            shimmerLoading.visibility = View.VISIBLE
            rvExercises.visibility = View.GONE
        } else {
            shimmerLoading.visibility = View.GONE
            rvExercises.visibility = View.VISIBLE
        }
    }

    private companion object {
        const val TAG = "HomeExercisesHolderContent"
    }

}