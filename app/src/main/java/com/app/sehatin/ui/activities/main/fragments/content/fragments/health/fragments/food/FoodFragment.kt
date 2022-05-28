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

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        binding = FragmentFoodBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initListener()
    }

    private fun initListener() {
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
                            Log.d(TAG, "getGoodFoods success: ${it.data}")
                            val data = it.data
                            if(data != null) {
                                val ok = data.ok
                                ok?.let { isOk ->
                                    if(isOk) {
                                        data.food?.let { foods ->
                                            viewModel.healthGoodFoods.addAll(foods)
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
        } catch (e: Exception) {
            Log.e(TAG, "getGoodFoods: $e")
        }
    }

    private fun setRvFoods() = with(binding) {
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
        const val TAG = "FoodFragment"
    }

}