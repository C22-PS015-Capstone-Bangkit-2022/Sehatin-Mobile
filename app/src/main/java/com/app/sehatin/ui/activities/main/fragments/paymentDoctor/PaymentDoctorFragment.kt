package com.app.sehatin.ui.activities.main.fragments.paymentDoctor

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.app.sehatin.R
import com.app.sehatin.data.model.Doctor
import com.app.sehatin.databinding.FragmentPaymentDoctorBinding
import com.app.sehatin.utils.toCurrencyFormat
import com.bumptech.glide.Glide

class PaymentDoctorFragment : Fragment() {
    private lateinit var binding: FragmentPaymentDoctorBinding
    private lateinit var doctor: Doctor

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        binding = FragmentPaymentDoctorBinding.inflate(inflater, container, false)
        initVariable()
        initListener()
        return binding.root
    }

    private fun initVariable() = with(binding) {
        doctor = PaymentDoctorFragmentArgs.fromBundle(arguments as Bundle).doctor
        totalPayment.text = doctor.price?.toCurrencyFormat()
        initDoctorInfo()
        initPaymentInfo()
    }

    private fun initListener() {

    }

    //INIT VIEW
    private fun initDoctorInfo() = with(binding.doctorInfo){
        Glide.with(requireContext())
            .load(doctor.imageUrl)
            .placeholder(R.drawable.user_default)
            .error(R.drawable.user_default)
            .into(doctorImage)
        doctorName.text = doctor.name
        doctorSpecialist.text = doctor.specialist
    }

    private fun initPaymentInfo() = with(binding.paymentInfo) {
        doctorPrice.text = doctor.price?.toCurrencyFormat()
        discountAmount.text = "-"
        totalPrice.text = doctor.price?.toCurrencyFormat()
    }

}