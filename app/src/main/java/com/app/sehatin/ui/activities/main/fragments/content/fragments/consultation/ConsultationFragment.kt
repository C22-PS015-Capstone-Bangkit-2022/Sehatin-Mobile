package com.app.sehatin.ui.activities.main.fragments.content.fragments.consultation

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.app.sehatin.databinding.FragmentConsultationBinding
import com.app.sehatin.databinding.ItemConsultationDoctorSessionBinding
import com.app.sehatin.databinding.ItemConsultationDoctorsBinding
import com.app.sehatin.ui.activities.main.fragments.content.fragments.consultation.adapter.ConsultationViewHolder
import com.app.sehatin.ui.activities.main.fragments.content.fragments.consultation.adapter.ConsultationViewsAdapter
import com.app.sehatin.ui.activities.main.fragments.content.fragments.consultation.viewholder.DoctorHolder
import com.app.sehatin.ui.activities.main.fragments.content.fragments.consultation.viewholder.DoctorSessionHolder
import com.app.sehatin.ui.viewmodel.ViewModelFactory

class ConsultationFragment : Fragment() {
    private lateinit var binding: FragmentConsultationBinding
    private lateinit var viewModel: ConsultationViewModel
    private var listViews = mutableListOf<ConsultationViewHolder>()
    private lateinit var adapter: ConsultationViewsAdapter

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        binding = FragmentConsultationBinding.inflate(inflater, container, false)
        initVariable()
        return binding.root
    }

    private fun initVariable() = with(binding) {
        viewModel = ViewModelProvider(this@ConsultationFragment, ViewModelFactory.getInstance())[ConsultationViewModel::class.java]
        listViews = mutableListOf(
            DoctorSessionHolder(
                ItemConsultationDoctorSessionBinding.inflate(LayoutInflater.from(requireContext()), binding.root, false).root,
                viewLifecycleOwner,
                this@ConsultationFragment
            ),
            DoctorHolder(
                ItemConsultationDoctorsBinding.inflate(LayoutInflater.from(requireContext()), binding.root, false).root,
                viewLifecycleOwner,
                this@ConsultationFragment
            )
        )
        adapter = ConsultationViewsAdapter(listViews, viewModel)
        rvConsultationLayout.setHasFixedSize(true)
        rvConsultationLayout.layoutManager = LinearLayoutManager(requireContext())
        rvConsultationLayout.adapter = adapter
    }

}