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
import com.app.sehatin.data.model.ScreeningQuestion
import com.app.sehatin.databinding.FragmentDiagnosisBinding
import com.app.sehatin.ui.activities.main.fragments.diagnosis.adapter.DiseasesAdapter
import com.app.sehatin.ui.activities.main.fragments.diagnosis.adapter.ScreeningQuestionAdapter
import com.app.sehatin.ui.viewmodel.DiagnosisViewModel
import com.app.sehatin.ui.viewmodel.ViewModelFactory
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

const val ANSWER_QUESTION = "answer question"
const val SELECT_DISEASES = "select diseases"

class DiagnosisFragment : Fragment() {
    private lateinit var binding: FragmentDiagnosisBinding
    private lateinit var viewModel: DiagnosisViewModel
    private var screeningQuestions = mutableListOf<ScreeningQuestion>()
    private var diseases = listOf<Disease>()
    private var currentAction = ANSWER_QUESTION

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
        rvQuestions.setHasFixedSize(true)
        rvQuestions.layoutManager = LinearLayoutManager(requireContext())
    }

    private fun initListener() = with(binding) {
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
                        diseases = data
                        setScreeningQuestionView(data)
                    } else {
                        Toast.makeText(requireContext(), "Something went wrong", Toast.LENGTH_SHORT).show()
                    }
                }
            }
        }

        infoBtn.setOnClickListener {
            val modalBottomSheet = BottomSheetDiagnosis(currentAction, object : BottomSheetDiagnosis.OnClickListener {
                override fun onActionClick() {
                    if(currentAction == ANSWER_QUESTION) {
                        setSelectDiseasesView()
                    } else {
                        setScreeningQuestionView(diseases)
                    }
                }
            })
            modalBottomSheet.show(requireActivity().supportFragmentManager, BottomSheetDiagnosis.TAG)
        }
    }

    private fun setScreeningQuestionView(data: List<Disease>) = with(binding) {
        showLoading(true)
        currentAction = ANSWER_QUESTION
        submitBtn.isEnabled = false
        lifecycleScope.launch(Dispatchers.Main) {
            for(disease in data) {
                val question = disease.screeningQuestions
                screeningQuestions.addAll(question)
            }
            showLoading(false)
            val adapter = ScreeningQuestionAdapter(screeningQuestions, object : ScreeningQuestionAdapter.OnClickListener {
                override fun onCheckBoxClicked(isChecked: Boolean, question: ScreeningQuestion) {

                }
            })
            rvQuestions.adapter = adapter
        }
    }

    private fun setSelectDiseasesView() = with(binding) {
        currentAction = SELECT_DISEASES
        submitBtn.isEnabled = false
        title.text = getString(R.string.ask_diseases)
        val adapter = DiseasesAdapter(diseases, object : DiseasesAdapter.OnClickListener {
            override fun onCheckBoxClicked(isChecked: Boolean, disease: Disease) {

            }
        })
        rvQuestions.adapter = adapter
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

    private companion object {
        const val TAG = "DiagnosisFragment"
    }

}