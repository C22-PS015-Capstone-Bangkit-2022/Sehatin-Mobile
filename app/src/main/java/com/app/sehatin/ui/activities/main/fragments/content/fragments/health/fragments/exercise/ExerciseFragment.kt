package com.app.sehatin.ui.activities.main.fragments.content.fragments.health.fragments.exercise

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.GridLayoutManager
import com.app.sehatin.data.Result
import com.app.sehatin.data.model.Exercise
import com.app.sehatin.databinding.FragmentExerciseBinding
import com.app.sehatin.ui.activities.main.fragments.content.ContentFragment
import com.google.firebase.auth.FirebaseAuth

class ExerciseFragment : Fragment() {
    private lateinit var binding: FragmentExerciseBinding
    private var viewModel = ContentFragment.viewModel
    private lateinit var token: String

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        binding = FragmentExerciseBinding.inflate(inflater, container, false)
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
                    getExercise(it)
                }
            } else {
                Log.e(TAG, "getIdToken: ${task.exception}")
            }
        }
        refreshLayout.setOnRefreshListener {
            viewModel.clearExerciseFragmentState()
            getExercise(token)
            refreshLayout.isRefreshing = false
        }
    }

    private fun getExercise(idToken: String){
        try {
            if(viewModel.healthGoodExercises.isEmpty()){
                viewModel.getGoodExercises(idToken).observe(viewLifecycleOwner) {
                    when (it){
                        is Result.Loading -> {
                            showLoading(true)
                        }
                        is Result.Error -> {
                            Toast.makeText(requireContext(), it.error, Toast.LENGTH_SHORT).show()
                        }
                        is Result.Success -> {
                            Log.d(TAG, "getExercise success: ${it.data?.sport?.size}")
                            val data = it.data
                            if (data != null){
                                val ok = data.ok
                                ok?.let { isOk ->
                                    if(isOk) {
                                        data.sport?.let { sports ->
                                            viewModel.healthGoodExercises.addAll(sports)
                                            showLoading(false)
                                            setRvExercise(sports)
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            } else {
                showLoading(false)
                setRvExercise(viewModel.healthGoodExercises)
            }
        } catch (e: Exception) {

        }
    }

    private fun setRvExercise(exercises: List<Exercise>) = with(binding) {
        rvExercises.setHasFixedSize(true)
        rvExercises.layoutManager = GridLayoutManager(requireContext(), 2)
        rvExercises.adapter = ExerciseAdapter(exercises)
    }

    private fun showLoading(isLoading: Boolean) = with(binding) {
        if(isLoading) {
            shimmerLoading.root.visibility = View.VISIBLE
            rvExercises.visibility = View.GONE
        } else {
            shimmerLoading.root.visibility = View.GONE
            rvExercises.visibility = View.VISIBLE
        }
    }

    private companion object {
        const val TAG = "ExerciseFragment"
    }
}