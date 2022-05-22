package com.app.sehatin.ui.activities.main.fragments.diagnosis

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.app.sehatin.R
import com.app.sehatin.data.Result
import com.app.sehatin.data.model.Disease
import com.app.sehatin.data.model.ScreeningQuestion
import com.app.sehatin.databinding.FragmentDiagnosisBinding
import com.app.sehatin.ui.viewmodel.DiagnosisViewModel
import com.app.sehatin.ui.viewmodel.ViewModelFactory

class DiagnosisFragment : Fragment() {
    private lateinit var binding: FragmentDiagnosisBinding
    private lateinit var viewModel: DiagnosisViewModel
    private var screeningQuestions = mutableListOf<ScreeningQuestion>()

    @Suppress("DEPRECATION")
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        requireActivity().window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS)
        requireActivity().window.statusBarColor = ContextCompat.getColor(requireContext(), R.color.primary)
        requireActivity().window.decorView.systemUiVisibility = View.VISIBLE
        binding = FragmentDiagnosisBinding.inflate(inflater, container, false)
        initVariable()
        initListener()
        return binding.root
    }

    private fun initVariable() = with(binding) {
        viewModel = ViewModelProvider(this@DiagnosisFragment, ViewModelFactory.getInstance())[DiagnosisViewModel::class.java]
    }

    private fun initListener() {
        viewModel.getDiseases().observe(viewLifecycleOwner) {
            when(it) {
                is Result.Loading -> {
                    showLoading(true)
                }
                is Result.Error -> {
                    showLoading(false)
                    Toast.makeText(requireContext(), it.error, Toast.LENGTH_SHORT).show()
                    Log.e(TAG, "getDiseases error : ${it.error}")
                }
                is Result.Success -> {
                    showLoading(false)
                    val data = it.data
                    if(data != null) {
                        setView(data)
                    } else {
                        Toast.makeText(requireContext(), "Something went wrong", Toast.LENGTH_SHORT).show()
                    }
                }
            }
        }
    }

    private fun showLoading(isLoading: Boolean) = with(binding) {
        if(isLoading) {
            contentLayout.visibility = View.GONE
            progressBar.visibility = View.VISIBLE
        } else {
            contentLayout.visibility = View.VISIBLE
            progressBar.visibility = View.GONE
        }
    }

    private fun setView(data: List<Disease>) = with(binding) {
        for(disease in data) {
            val question = disease.screeningQuestions
            screeningQuestions.addAll(question)
        }
        val adapter = ScreeningQuestionAdapter(screeningQuestions)
        rvQuestions.setHasFixedSize(true)
        rvQuestions.layoutManager = LinearLayoutManager(requireContext())
        rvQuestions.adapter = adapter
    }

    private companion object {
        const val TAG = "DiagnosisFragment"
    }

}