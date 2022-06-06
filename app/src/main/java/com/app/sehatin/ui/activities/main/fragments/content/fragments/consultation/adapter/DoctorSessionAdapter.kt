package com.app.sehatin.ui.activities.main.fragments.content.fragments.consultation.adapter

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.app.sehatin.R
import com.app.sehatin.data.model.Doctor
import com.app.sehatin.data.model.DoctorActiveSession
import com.app.sehatin.databinding.ItemDoctorActiveSessionBinding
import com.app.sehatin.injection.Injection
import com.app.sehatin.utils.convertToDate
import com.bumptech.glide.Glide

class DoctorSessionAdapter(private val doctorSessionAdapterListener: DoctorSessionAdapterListener): ListAdapter<DoctorActiveSession, DoctorSessionAdapter.Holder>(DIFF_CALLBACK) {
    private lateinit var context: Context
    private val doctorRef = Injection.provideDoctorCollection()

    inner class Holder(private val binding: ItemDoctorActiveSessionBinding): RecyclerView.ViewHolder(binding.root) {
        fun bind(doctorActiveSession: DoctorActiveSession) = with(binding) {
            val isActive = doctorActiveSession.active
            isActive?.let { active ->
                if(active) {
                    this.root.setOnClickListener {
                        doctorSessionAdapterListener.onActiveClick(doctorActiveSession)
                    }
                }
            }
            sessionTime.text = doctorActiveSession.validUntil?.convertToDate()
            doctorActiveSession.doctorId?.let { initDoctorData(it) }
        }

        private fun initDoctorData(doctorId: String) = with(binding) {
            doctorRef
                .document(doctorId)
                .get()
                .addOnSuccessListener {
                    val doctor = it.toObject(Doctor::class.java)
                    if (doctor != null) {
                        Glide.with(context)
                            .load(doctor.imageUrl)
                            .error(R.drawable.user_default)
                            .placeholder(R.drawable.user_default)
                            .into(doctorImage)
                        doctorName.text = doctor.name
                        doctorSpecialist.text = doctor.specialist
                    }
                }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Holder {
        context = parent.context
        val binding = ItemDoctorActiveSessionBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return Holder(binding)
    }

    override fun onBindViewHolder(holder: Holder, position: Int) {
        holder.bind(getItem(position))
    }

    companion object {
        val DIFF_CALLBACK: DiffUtil.ItemCallback<DoctorActiveSession> =
            object : DiffUtil.ItemCallback<DoctorActiveSession>() {
                override fun areItemsTheSame(oldArticle: DoctorActiveSession, newArticle: DoctorActiveSession): Boolean {
                    return oldArticle.id == newArticle.id
                }

                @SuppressLint("DiffUtilEquals")
                override fun areContentsTheSame(oldArticle: DoctorActiveSession, newArticle: DoctorActiveSession): Boolean {
                    return oldArticle == newArticle
                }
            }
    }

}