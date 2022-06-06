package com.app.sehatin.ui.activities.main.fragments.content.fragments.consultation.viewholder

import android.content.Context
import android.view.View
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.LifecycleOwner
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.app.sehatin.data.Result
import com.app.sehatin.databinding.ItemConsultationDoctorsBinding
import com.app.sehatin.ui.activities.main.fragments.content.ContentFragmentDirections
import com.app.sehatin.ui.activities.main.fragments.content.fragments.consultation.ConsultationViewModel
import com.app.sehatin.ui.activities.main.fragments.content.fragments.consultation.adapter.ConsultationViewHolder
import com.app.sehatin.ui.activities.main.fragments.content.fragments.consultation.adapter.DoctorAdapter

class DoctorHolder(
    itemView: View,
    private val lifecycleOwner: LifecycleOwner,
    private val parent: Fragment,
    ): ConsultationViewHolder(itemView) {

    private val binding = ItemConsultationDoctorsBinding.bind(itemView)
    private lateinit var context: Context
    private lateinit var viewModel: ConsultationViewModel
    private lateinit var doctorAdapter: DoctorAdapter

    override fun bind(context: Context, viewModel: ConsultationViewModel) {
        this.context = context
        this.viewModel = viewModel
        initVariable()
        initListener()
    }

    private fun initVariable() = with(binding) {
        doctorAdapter = DoctorAdapter {
            val direction = ContentFragmentDirections.actionContentFragmentToPaymentDoctorFragment(it)
            parent.findNavController().navigate(direction)
        }
        rvListDoctor.setHasFixedSize(true)
        rvListDoctor.layoutManager = LinearLayoutManager(context)
        rvListDoctor.adapter = doctorAdapter
    }

    private fun initListener() {
        viewModel.getDoctor()
        viewModel.getDoctorState.observe(lifecycleOwner) {
            when(it) {
                is Result.Loading -> {
                    showLoading(true)
                }
                is Result.Error -> {
                    Toast.makeText(context, it.error, Toast.LENGTH_SHORT).show()
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
            content.visibility = View.GONE
        } else {
            progressBar.visibility = View.GONE
            content.visibility = View.VISIBLE
        }
    }

}