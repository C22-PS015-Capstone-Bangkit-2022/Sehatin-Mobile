package com.app.sehatin.ui.activities.main.fragments.content.home.viewHolder

import android.content.Context
import android.content.Intent
import android.util.Log
import android.view.View
import androidx.lifecycle.LifecycleOwner
import androidx.recyclerview.widget.LinearLayoutManager
import com.app.sehatin.R
import com.app.sehatin.data.Result
import com.app.sehatin.data.model.Exercise
import com.app.sehatin.data.model.Food
import com.app.sehatin.databinding.ItemHomeContentBinding
import com.app.sehatin.ui.activities.main.fragments.content.home.HomeViewModel
import com.app.sehatin.ui.activities.main.fragments.content.home.adapter.HorizontalExerciseAdapter
import com.app.sehatin.ui.activities.main.fragments.content.home.adapter.HorizontalFoodAdapter
import com.app.sehatin.ui.activities.objectDetection.ObjectDetectionActivity
import com.app.sehatin.ui.sharedAdapter.ViewHolder
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.firebase.auth.FirebaseAuth


class HomeContentHolder(
    itemView: View,
    private val bottomNavigationView: BottomNavigationView,
    private val homeViewModel: HomeViewModel,
    private val lifecycleOwner: LifecycleOwner
    ): ViewHolder(itemView) {

    private val binding = ItemHomeContentBinding.bind(itemView)
    private lateinit var foodAdapter: HorizontalFoodAdapter
    private lateinit var exerciseAdapter: HorizontalExerciseAdapter
    private lateinit var context: Context

    override fun bind(context: Context) {
        this.context = context
        initVariable()
        initListener()
        getData()
        setRvFoods()
        setRvExercises()
    }

    private fun initVariable() {
        foodAdapter = HorizontalFoodAdapter(foods)
        exerciseAdapter = HorizontalExerciseAdapter(exercises)
    }

    private fun initListener() = with(binding) {
        CameraMLBtn.setOnClickListener {
            context.startActivity(Intent(context, ObjectDetectionActivity::class.java))
        }
        otherExerciseBtn.setOnClickListener {
            bottomNavigationView.selectedItemId = R.id.nav_article
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
        homeViewModel.getGoodFoods(idToken).observe(lifecycleOwner) {
            when(it) {
                is Result.Loading -> {
                    Log.d(TAG, "getGoodFoods: loading")
                }
                is Result.Error -> {
                    Log.e(TAG, "getGoodFoods: ${it.error}")
                }
                is Result.Success -> {
                    Log.d(TAG, "getGoodFoods success : ${it.data}")
                }
            }
        }
    }

    private fun setRvFoods() = with(binding) {
        rvFoods.setHasFixedSize(true)
        rvFoods.layoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
        rvFoods.adapter = foodAdapter
    }

    private fun setRvExercises() = with(binding) {
        rvExercises.setHasFixedSize(true)
        rvExercises.layoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
        rvExercises.adapter = exerciseAdapter
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

    private val exercises = arrayListOf(
        Exercise(
            name = "Berenang",
            thumbnail = "https://tulisanku.co.id/uploads/2020/01/1579184123-1-1024x615.jpeg"
        ),
        Exercise(
            name = "Bersepeda",
            thumbnail = "https://olahragapedia.com/wp-content/uploads/2019/10/15-manfaat-bersepeda-59806d20bb7f07a-1024x605.jpg"
        ),
        Exercise(
            name = "Yoga",
            thumbnail = "https://th.bing.com/th/id/R.4be4ebcddc3d9ee7fc2ea10d3bf76fe1?rik=9U%2fYJYTCA4qx7A&riu=http%3a%2f%2fcatherinetingey.com%2fwp-content%2fuploads%2f2013%2f09%2fDSC01303.jpg&ehk=JysFbx9eHSS5UTRhaF2I6SNxzjB6sJXn9eMucazmVxM%3d&risl=&pid=ImgRaw&r=0"
        )
    )

    private companion object {
        const val TAG = "HomeContentHolder"
    }
}