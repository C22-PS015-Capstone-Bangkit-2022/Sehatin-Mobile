package com.app.sehatin.ui.activities.main.fragments.diagnosis

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import androidx.core.content.ContextCompat
import com.app.sehatin.R
import com.app.sehatin.databinding.FragmentDiagnosisBinding

class DiagnosisFragment : Fragment() {
    private lateinit var binding: FragmentDiagnosisBinding

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

    private fun initVariable() {

    }

    private fun initListener() {

    }

}