package com.app.sehatin.ui.activities.main.fragments.content.fragments.consultation

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.app.sehatin.data.Result
import com.app.sehatin.databinding.FragmentConsultationBinding
import com.app.sehatin.ui.viewmodel.ViewModelFactory

class ConsultationFragment : Fragment() {
    private lateinit var binding: FragmentConsultationBinding
    private lateinit var viewModel: ConsultationViewModel
    private val doctorAdapter = DoctorAdapter()

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
        rvListDoctor.adapter = doctorAdapter
    }

    private fun initListener() {
        viewModel.getDoctor().observe(viewLifecycleOwner) {
            when(it) {
                is Result.Loading -> {
                    showLoading(true)
                }
                is Result.Error -> {
                    showLoading(false)
                    Toast.makeText(requireContext(), it.error, Toast.LENGTH_SHORT).show()
                }
                is Result.Success -> {
                    showLoading(false)
                    doctorAdapter.submitList(it.data)
                }
            }
        }
    }

    private fun showLoading(isLoading: Boolean) = with(binding) {
        if(isLoading) {
            progressBar.visibility = View.VISIBLE
            rvListDoctor.visibility = View.GONE
        } else {
            progressBar.visibility = View.GONE
            rvListDoctor.visibility = View.VISIBLE
        }
    }

}