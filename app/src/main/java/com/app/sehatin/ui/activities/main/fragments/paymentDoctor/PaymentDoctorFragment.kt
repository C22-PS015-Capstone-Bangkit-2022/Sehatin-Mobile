package com.app.sehatin.ui.activities.main.fragments.paymentDoctor

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.app.sehatin.R
import com.app.sehatin.data.model.Doctor
import com.app.sehatin.data.model.PaymentMethod
import com.app.sehatin.databinding.FragmentPaymentDoctorBinding
import com.app.sehatin.utils.toCurrencyFormat
import com.bumptech.glide.Glide

class PaymentDoctorFragment : Fragment() {
    private lateinit var binding: FragmentPaymentDoctorBinding
    private lateinit var doctor: Doctor
    private lateinit var viewModel: PaymentDoctorViewModel
    private lateinit var paymentMethodAdapter: PaymentMethodAdapter
    private var selectedPaymentMethod: PaymentMethod? = null

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        binding = FragmentPaymentDoctorBinding.inflate(inflater, container, false)
        initVariable()
        initListener()
        return binding.root
    }

    private fun initVariable() = with(binding) {
        viewModel = ViewModelProvider(this@PaymentDoctorFragment)[PaymentDoctorViewModel::class.java]
        doctor = PaymentDoctorFragmentArgs.fromBundle(arguments as Bundle).doctor
        totalPayment.text = doctor.price?.toCurrencyFormat()
        paymentMethodAdapter = PaymentMethodAdapter {
            selectedPaymentMethod = it
            payButton.isEnabled = true
        }
        initDoctorInfo()
        initPaymentInfo()
        initPaymentMethod()
    }

    private fun initListener() = with(binding) {
        toolbar.setNavigationOnClickListener {
            requireActivity().onBackPressed()
        }
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

    private fun initPaymentMethod() = with(binding.paymentMethod) {
        rvPaymentMethod.setHasFixedSize(true)
        rvPaymentMethod.layoutManager = LinearLayoutManager(requireContext())
        rvPaymentMethod.adapter = paymentMethodAdapter
        paymentMethodAdapter.submitList(viewModel.getPaymentMethod())
    }

}