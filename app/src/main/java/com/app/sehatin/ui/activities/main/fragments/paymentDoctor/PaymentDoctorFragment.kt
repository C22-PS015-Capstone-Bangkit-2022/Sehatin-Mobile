package com.app.sehatin.ui.activities.main.fragments.paymentDoctor

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.app.sehatin.R
import com.app.sehatin.data.model.Doctor
import com.app.sehatin.data.model.PaymentMethod
import com.app.sehatin.databinding.FragmentPaymentDoctorBinding
import com.app.sehatin.utils.toCurrencyFormat
import com.bumptech.glide.Glide
import com.midtrans.sdk.corekit.callback.TransactionFinishedCallback
import com.midtrans.sdk.corekit.core.UIKitCustomSetting
import com.midtrans.sdk.corekit.core.themes.CustomColorTheme
import com.midtrans.sdk.corekit.models.snap.TransactionResult
import com.midtrans.sdk.uikit.SdkUIFlowBuilder

class PaymentDoctorFragment : Fragment(), TransactionFinishedCallback {
    private lateinit var binding: FragmentPaymentDoctorBinding
    private lateinit var doctor: Doctor
    private lateinit var viewModel: PaymentDoctorViewModel
    private lateinit var paymentMethodAdapter: PaymentMethodAdapter
    private var selectedPaymentMethod: PaymentMethod? = null

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        binding = FragmentPaymentDoctorBinding.inflate(inflater, container, false)
        initMidTransSdk()
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

    //INIT MidTrans
    private fun initMidTransSdk() {
        val clientKey: String = SdkConfig.MERCHANT_CLIENT_KEY
        val baseUrl: String = SdkConfig.MERCHANT_BASE_CHECKOUT_URL
        val sdkUIFlowBuilder: SdkUIFlowBuilder = SdkUIFlowBuilder.init()
            .setClientKey(clientKey) // client_key is mandatory
            .setContext(requireContext()) // context is mandatory
            .setTransactionFinishedCallback(this) // set transaction finish callback (sdk callback)
            .setMerchantBaseUrl(baseUrl) //set merchant url
            .setUIkitCustomSetting(uiKitCustomSetting())
            .enableLog(true) // enable sdk log
            .setColorTheme(CustomColorTheme("#FF399BFA", "#FF83DAFF", "#FFFF8383")) // will replace theme on snap theme on MAP
            .setLanguage("id")
        sdkUIFlowBuilder.buildSDK()
    }

    private fun uiKitCustomSetting(): UIKitCustomSetting {
        val uIKitCustomSetting = UIKitCustomSetting()
        uIKitCustomSetting.isSkipCustomerDetailsPages = true
        uIKitCustomSetting.isShowPaymentStatus = true
        return uIKitCustomSetting
    }

    override fun onTransactionFinished(result: TransactionResult) {
        if (result.response != null) {
            when (result.status) {
                TransactionResult.STATUS_SUCCESS -> Toast.makeText(requireContext(), "Transaction Finished. ID: " + result.response.transactionId, Toast.LENGTH_LONG).show()
                TransactionResult.STATUS_PENDING -> Toast.makeText(requireContext(), "Transaction Pending. ID: " + result.response.transactionId, Toast.LENGTH_LONG).show()
                TransactionResult.STATUS_FAILED -> Toast.makeText(requireContext(), "Transaction Failed. ID: " + result.response.transactionId.toString() + ". Message: " + result.response.statusMessage, Toast.LENGTH_LONG).show()
            }
            result.response.validationMessages
        } else if (result.isTransactionCanceled) {
            Toast.makeText(requireContext(), "Transaction Canceled", Toast.LENGTH_LONG).show()
        } else {
            if (result.status.equals(TransactionResult.STATUS_INVALID, true)) {
                Toast.makeText(requireContext(), "Transaction Invalid", Toast.LENGTH_LONG).show()
            } else {
                Toast.makeText(requireContext(), "Transaction Finished with failure.", Toast.LENGTH_LONG).show()
            }
        }
    }

}