package com.app.sehatin.ui.activities.main.fragments.content.fragments.consultation

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.app.sehatin.R
import com.app.sehatin.data.model.Doctor
import com.app.sehatin.databinding.ItemListDoctorBinding
import com.bumptech.glide.Glide

class DoctorAdapter: ListAdapter<Doctor, DoctorAdapter.Holder>(DIFF_CALLBACK) {

    inner class Holder(private val binding: ItemListDoctorBinding): RecyclerView.ViewHolder(binding.root) {
        fun bind(doctor: Doctor) = with(binding) {
            Glide.with(this.root)
                .load(doctor.imageUrl)
                .placeholder(R.drawable.user_default)
                .error(R.drawable.user_default)
                .into(doctorImage)
            doctorName.text = doctor.name
            doctorRating.text = doctor.rating.toString()
            doctorReviewCount.text = "${doctor.review.toString()} ulasan"
            doctorSpecialist.text = doctor.specialist
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Holder {
        val binding = ItemListDoctorBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return Holder(binding)
    }

    override fun onBindViewHolder(holder: Holder, position: Int) {
        holder.bind(getItem(position))
    }

    companion object {
        val DIFF_CALLBACK: DiffUtil.ItemCallback<Doctor> =
            object : DiffUtil.ItemCallback<Doctor>() {
                override fun areItemsTheSame(oldArticle: Doctor, newArticle: Doctor): Boolean {
                    return oldArticle.id == newArticle.id
                }

                @SuppressLint("DiffUtilEquals")
                override fun areContentsTheSame(oldArticle: Doctor, newArticle: Doctor): Boolean {
                    return oldArticle == newArticle
                }
            }
    }

}