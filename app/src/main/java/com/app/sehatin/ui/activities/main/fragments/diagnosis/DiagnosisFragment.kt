package com.app.sehatin.ui.activities.main.fragments.diagnosis

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import androidx.core.content.ContextCompat
import androidx.lifecycle.ViewModelProvider
import com.app.sehatin.R
import com.app.sehatin.data.Result
import com.app.sehatin.databinding.FragmentDiagnosisBinding
import com.app.sehatin.ui.viewmodel.DiagnosisViewModel
import com.app.sehatin.ui.viewmodel.ViewModelFactory

class DiagnosisFragment : Fragment() {
    private lateinit var binding: FragmentDiagnosisBinding
    private lateinit var viewModel: DiagnosisViewModel

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
                    Log.d(TAG, "getDiseases: loading")
                }
                is Result.Error -> {
                    Log.d(TAG, "getDiseases: error = ${it.error}")
                }
                is Result.Success -> {
                    Log.d(TAG, "getDiseases: success = ${it.data}")
                }
            }
        }
    }

    private companion object {
        const val TAG = "DiagnosisFragment"
    }

}