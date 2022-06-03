package com.app.sehatin.ui.activities.main.fragments.content.fragments.consultation

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.app.sehatin.databinding.FragmentConsultationBinding
import com.app.sehatin.ui.viewmodel.ViewModelFactory

class ConsultationFragment : Fragment() {
    private lateinit var binding: FragmentConsultationBinding
    private lateinit var viewModel: ConsultationViewModel

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        binding = FragmentConsultationBinding.inflate(inflater, container, false)
        initVariable()
        initListener()
        return binding.root
    }

    private fun initVariable() = with(binding) {
        viewModel = ViewModelProvider(this@ConsultationFragment, ViewModelFactory.getInstance())[ConsultationViewModel::class.java]
        rvListDoctor.setHasFixedSize(true)
        rvListDoctor.layoutManager = LinearLayoutManager(requireContext())
    }

    private fun initListener() {

    }

}