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
import com.app.sehatin.ui.activities.main.fragments.content.adapter.ViewHolder
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.firebase.auth.FirebaseAuth

class HomeFoodHolder(
    itemView: View,
    private val bottomNavigationView: BottomNavigationView,
    private val lifecycleOwner: LifecycleOwner
    ): ViewHolder(itemView) {

    private val binding = ItemHomeFoodBinding.bind(itemView)
    private lateinit var viewModel: ContentViewModel
    private lateinit var foodAdapter: HorizontalFoodAdapter
    private lateinit var context: Context

    override fun bind(context: Context, viewModel: ContentViewModel) {
        this.viewModel = viewModel
        this.context = context
        initVariable()
        initListener()
        getData()
        setRvFoods()
    }

    private fun initVariable() {
        foodAdapter = HorizontalFoodAdapter(foods)
    }

    private fun initListener() = with(binding) {
        CameraMLBtn.setOnClickListener {
            context.startActivity(Intent(context, ObjectDetectionActivity::class.java))
        }
        otherFoodBtn.setOnClickListener {
            bottomNavigationView.selectedItemId = R.id.nav_health
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
                        Log.d(TAG, "getGoodFoods: ${it.data}")
                        val data = it.data
                        if(data != null) {
                            val ok = data.ok
                            ok?.let { isOk ->
                                if(isOk) {
                                    data.food?.let { foods ->
                                        viewModel.goodFoods.addAll(foods)
                                    }
                                    showLoading(false)
                                    setRvFoods()
                                }
                            }
                        }
                    }
                }
            }
        } else {
            showLoading(false)
            setRvFoods()
        }
    }

    private fun setRvFoods() = with(binding) {
        rvFoods.setHasFixedSize(true)
        rvFoods.layoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
        rvFoods.adapter = foodAdapter
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

    private val foods = arrayListOf(
        Food(
            name = "Jahe",
            thumbnail = "https://www.tokoindonesia.co.uk/wp-content/uploads/2020/05/jaheputih.png"
        ),
        Food(
            name = "Madu",
            thumbnail = "https://d3avoj45mekucs.cloudfront.net/astrogempak/media/articleasset/2018/nov/lovoury1_2.jpg"
        ),
        Food(
            name = "Bayam",
            thumbnail = "https://d2ncjxd2rk2vpl.cloudfront.net/e-petani/product/608ca9780826280006508aa5/600x600/95/outside/382e1d02-05f7-4d26-9a06-fd9509c6db03"
        ),
        Food(
            name = "Apel",
            thumbnail = "https://1.bp.blogspot.com/-rVn4xEKKGJc/XadDLQIVloI/AAAAAAAABXM/I16Jue0pFnc6EXHD02fw38jaxnf38a-ggCLcBGAsYHQ/s1600/apple-2788616_1280.jpg"
        ),
        Food(
            name = "Kacang Hijau",
            thumbnail = "https://www.kampustani.com/wp-content/uploads/2019/01/Teknologi-Produksi-Benih-Kacang-Hijau.jpg"
        ),
    )

    private companion object {
        const val TAG = "HomeFoodHolder"
    }
}