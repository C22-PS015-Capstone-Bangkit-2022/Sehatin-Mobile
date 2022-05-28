package com.app.sehatin.ui.activities.main.fragments.content.fragments.home.viewHolder

import android.content.Context
import android.view.View
import androidx.lifecycle.LifecycleOwner
import androidx.recyclerview.widget.LinearLayoutManager
import com.app.sehatin.data.model.Exercise
import com.app.sehatin.databinding.ItemHomeExercisesBinding
import com.app.sehatin.ui.activities.main.fragments.content.ContentViewModel
import com.app.sehatin.ui.activities.main.fragments.content.fragments.home.adapter.HorizontalExerciseAdapter
import com.app.sehatin.ui.activities.main.fragments.content.adapter.ViewHolder
import com.google.android.material.bottomnavigation.BottomNavigationView

class HomeExercisesHolder(
    itemView: View,
    private val bottomNavigationView: BottomNavigationView?,
    private val lifecycleOwner: LifecycleOwner
    ): ViewHolder(itemView) {

    private lateinit var exerciseAdapter: HorizontalExerciseAdapter
    private lateinit var context: Context
    private lateinit var viewModel: ContentViewModel
    private val binding = ItemHomeExercisesBinding.bind(itemView)

    override fun bind(context: Context, viewModel: ContentViewModel) {
        this.context = context
        this.viewModel = viewModel
        exerciseAdapter = HorizontalExerciseAdapter(exercises)
        setRvExercises()
    }

    private fun setRvExercises() = with(binding) {
        rvExercises.setHasFixedSize(true)
        rvExercises.layoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
        rvExercises.adapter = exerciseAdapter
    }

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

}