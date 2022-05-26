package com.app.sehatin.ui.activities.main.fragments.content.health.fragments.food

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.GridLayoutManager
import com.app.sehatin.data.model.Food
import com.app.sehatin.databinding.FragmentFoodBinding
import com.app.sehatin.ui.activities.main.fragments.content.health.HealthViewModel

class FoodFragment(private val healthViewModel: HealthViewModel) : Fragment() {
    private lateinit var binding: FragmentFoodBinding


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        binding = FragmentFoodBinding.inflate(inflater, container, false)
        initVariable()
        return binding.root
    }

    private fun initVariable() = with(binding) {
        rvFoods.setHasFixedSize(true)
        rvFoods.layoutManager = GridLayoutManager(requireContext(), 2)
        rvFoods.adapter = FoodAdapter(foods)
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

}