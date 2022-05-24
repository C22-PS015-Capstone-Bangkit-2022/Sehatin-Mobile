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
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.app.sehatin.R
import com.app.sehatin.data.Result
import com.app.sehatin.data.model.Disease
import com.app.sehatin.databinding.FragmentDiagnosisBinding
import com.app.sehatin.ui.activities.main.fragments.diagnosis.adapter.DiseasesAdapter
import com.app.sehatin.ui.activities.main.fragments.diagnosis.adapter.ScreeningQuestionAdapter
import com.app.sehatin.ui.viewmodel.ViewModelFactory
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

const val ANSWER_QUESTION = "answer question"
const val SELECT_DISEASES = "select diseases"

class DiagnosisFragment : Fragment() {
    private lateinit var binding: FragmentDiagnosisBinding
    private lateinit var viewModel: DiagnosisViewModel
    private lateinit var screeningQuestionAdapter: ScreeningQuestionAdapter
    private lateinit var diseasesAdapter: DiseasesAdapter

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
        submitBtn.isEnabled = false
        rvQuestions.setHasFixedSize(true)
        rvQuestions.layoutManager = LinearLayoutManager(requireContext())
        screeningQuestionAdapter = ScreeningQuestionAdapter{
            viewModel.incrementCounter()
        }
        diseasesAdapter = DiseasesAdapter {
            viewModel.incrementCounter()
        }
    }

    private fun initListener() = with(binding) {
        viewModel.getDiseases().observe(viewLifecycleOwner) {
            when (it) {
                is Result.Loading -> {
                    showLoading(true)
                }
                is Result.Error -> {
                    showLoading(false)
                    Toast.makeText(requireContext(), it.error, Toast.LENGTH_SHORT).show()
                    Log.e(TAG, "getDiseases error : ${it.error}")
                }
                is Result.Success -> {
                    initView(it.data)
                }
            }
            submitBtn.setOnClickListener {
                submit()
            }
        }

        infoBtn.setOnClickListener {
            val modalBottomSheet = BottomSheetDiagnosis(viewModel.currentAction, object : BottomSheetDiagnosis.OnClickListener {
                override fun onActionClick() {
                    if (viewModel.currentAction == ANSWER_QUESTION) {
                        setSelectDiseasesView()
                    } else {
                        setScreeningQuestionView()
                    }
                }
            })
            modalBottomSheet.show(requireActivity().supportFragmentManager, BottomSheetDiagnosis.TAG)
        }
    }

    private fun initView(data: List<Disease>?) = with(binding) {
        if (data != null) {
            viewModel.diseases = data
            for (disease in data) {
                val question = disease.screeningQuestions
                viewModel.screeningQuestions.addAll(question)
            }
            setScreeningQuestionView()
        } else {
            Toast.makeText(requireContext(), "Something went wrong", Toast.LENGTH_SHORT).show()
        }

        viewModel.counter.observe(viewLifecycleOwner) { value ->
            when(viewModel.currentAction) {
                ANSWER_QUESTION -> {
                    submitBtn.isEnabled = value == viewModel.screeningQuestions.size
                }
                SELECT_DISEASES -> {
                    submitBtn.isEnabled = value == viewModel.diseases.size
                }
            }
        }

    }

    private fun setScreeningQuestionView() = with(binding) {
        showLoading(true)
        viewModel.currentAction = ANSWER_QUESTION
        viewModel.resetCounter()
        lifecycleScope.launch(Dispatchers.Main) {
            showLoading(false)
            rvQuestions.adapter = screeningQuestionAdapter
            screeningQuestionAdapter.submitList(viewModel.screeningQuestions)
        }
    }

    private fun setSelectDiseasesView() = with(binding) {
        viewModel.currentAction = SELECT_DISEASES
        viewModel.resetCounter()
        title.text = getString(R.string.ask_diseases)
        rvQuestions.adapter = diseasesAdapter
        diseasesAdapter.submitList(viewModel.diseases)
    }

    private fun submit() {
        Toast.makeText(requireContext(), "submit", Toast.LENGTH_SHORT).show()
    }

    private fun showLoading(isLoading: Boolean) = with(binding) {
        if (isLoading) {
            contentLayout.visibility = View.GONE
            progressBar.visibility = View.VISIBLE
        } else {
            contentLayout.visibility = View.VISIBLE
            progressBar.visibility = View.GONE
        }
    }

    private companion object {
        const val TAG = "DiagnosisFragment"
    }

}