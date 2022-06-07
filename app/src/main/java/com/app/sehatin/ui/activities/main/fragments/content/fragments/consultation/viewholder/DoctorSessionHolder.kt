package com.app.sehatin.ui.activities.main.fragments.content.fragments.consultation.viewholder

import android.content.Context
import android.view.View
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.LifecycleOwner
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.app.sehatin.data.Result
import com.app.sehatin.data.model.DoctorActiveSession
import com.app.sehatin.data.model.User
import com.app.sehatin.databinding.ItemConsultationDoctorSessionBinding
import com.app.sehatin.ui.activities.main.fragments.content.ContentFragmentDirections
import com.app.sehatin.ui.activities.main.fragments.content.fragments.consultation.ConsultationViewModel
import com.app.sehatin.ui.activities.main.fragments.content.fragments.consultation.adapter.ConsultationViewHolder
import com.app.sehatin.ui.activities.main.fragments.content.fragments.consultation.adapter.DoctorSessionAdapter
import com.app.sehatin.ui.activities.main.fragments.content.fragments.consultation.adapter.DoctorSessionAdapterListener

class DoctorSessionHolder(
    itemView: View,
    private val lifecycleOwner: LifecycleOwner,
    private val parent: Fragment,
    ): ConsultationViewHolder(itemView)  {

    private val binding = ItemConsultationDoctorSessionBinding.bind(itemView)
    private lateinit var context: Context
    private lateinit var viewModel: ConsultationViewModel
    private lateinit var adapter: DoctorSessionAdapter

    override fun bind(context: Context, viewModel: ConsultationViewModel) {
        this.context = context
        this.viewModel = viewModel
        initVariable()
        initListener()
    }

    private fun initVariable() = with(binding) {
        adapter = DoctorSessionAdapter(object : DoctorSessionAdapterListener {
            override fun onActiveClick(doctorActiveSession: DoctorActiveSession) {
                doctorActiveSession.doctorId?.let {
                    val direction = ContentFragmentDirections.actionContentFragmentToSendChatFragment(it)
                    direction.isDoctor = true
                    doctorActiveSession.active?.let { isActive ->
                        direction.isSessionActive = isActive
                    }
                    parent.findNavController().navigate(direction)
                }
            }
        })
        rvActiveSession.setHasFixedSize(true)
        rvActiveSession.layoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
        rvActiveSession.adapter = adapter
    }

    private fun initListener() {
        User.currentUser.id?.let {
            viewModel.getUserDoctorSession(it)
            viewModel.getUserDoctorSessionState.observe(lifecycleOwner) { result ->
                when(result) {
                    is Result.Loading -> {
                        showLoading(true)
                    }
                    is Result.Error -> {
                        Toast.makeText(context, result.error, Toast.LENGTH_SHORT).show()
                    }
                    is Result.Success -> {
                        showLoading(false)
                        showContent(result.data.isNotEmpty(), result.data)
                    }
                }
            }
        }
    }

    private fun showLoading(isLoading: Boolean) = with(binding) {
        if(isLoading) {
            progressBar.visibility = View.VISIBLE
            contentLayout.visibility = View.GONE
        } else {
            progressBar.visibility = View.GONE
            contentLayout.visibility = View.VISIBLE
        }
    }

    private fun showContent(isNotEmpty: Boolean, data: List<DoctorActiveSession>) = with(binding) {
        if(isNotEmpty) {
            contentLayout.visibility = View.VISIBLE
            adapter.submitList(data)
        } else {
            contentLayout.visibility = View.GONE
        }
    }

}