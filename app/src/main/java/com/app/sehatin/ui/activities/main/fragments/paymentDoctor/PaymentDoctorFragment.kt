package com.app.sehatin.ui.activities.main.fragments.paymentDoctor

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.app.sehatin.R
import com.app.sehatin.data.Result
import com.app.sehatin.data.model.Doctor
import com.app.sehatin.data.model.DoctorActiveSession
import com.app.sehatin.data.model.MyPaymentMethod
import com.app.sehatin.data.model.User
import com.app.sehatin.databinding.FragmentPaymentDoctorBinding
import com.app.sehatin.ui.viewmodel.ViewModelFactory
import com.app.sehatin.utils.DateHelper
import com.app.sehatin.utils.toCurrencyFormat
import com.bumptech.glide.Glide
import com.midtrans.sdk.corekit.callback.TransactionFinishedCallback
import com.midtrans.sdk.corekit.core.MidtransSDK
import com.midtrans.sdk.corekit.core.TransactionRequest
import com.midtrans.sdk.corekit.core.UIKitCustomSetting
import com.midtrans.sdk.corekit.core.themes.CustomColorTheme
import com.midtrans.sdk.corekit.models.CustomerDetails
import com.midtrans.sdk.corekit.models.snap.Gopay
import com.midtrans.sdk.corekit.models.snap.Shopeepay
import com.midtrans.sdk.corekit.models.snap.TransactionResult
import com.midtrans.sdk.uikit.SdkUIFlowBuilder

class PaymentDoctorFragment : Fragment(), TransactionFinishedCallback {
    private lateinit var binding: FragmentPaymentDoctorBinding
    private lateinit var doctor: Doctor
    private lateinit var viewModel: PaymentDoctorViewModel
    private lateinit var paymentMethodAdapter: PaymentMethodAdapter
    private var selectedMyPaymentMethod: MyPaymentMethod? = null

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        binding = FragmentPaymentDoctorBinding.inflate(inflater, container, false)
        initMidTransSdk()
        initVariable()
        initListener()
        return binding.root
    }

    private fun initVariable() = with(binding) {
        viewModel = ViewModelProvider(this@PaymentDoctorFragment, ViewModelFactory.getInstance())[PaymentDoctorViewModel::class.java]
        doctor = PaymentDoctorFragmentArgs.fromBundle(arguments as Bundle).doctor
        totalPayment.text = doctor.price?.toCurrencyFormat()
        paymentMethodAdapter = PaymentMethodAdapter {
            selectedMyPaymentMethod = it
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

        payButton.isEnabled = selectedMyPaymentMethod != null
        payButton.setOnClickListener {
            doctor.price?.let {
                createPayment(it.toDouble())
            }
        }

        viewModel.createDoctorActiveSessionState.observe(viewLifecycleOwner) {
            when(it) {
                is Result.Loading -> {
                    Log.d(TAG, "createDoctorActiveSessionState: Loading")
                }
                is Result.Error -> {
                    Log.d(TAG, "createDoctorActiveSessionState: Error = ${it.error}")
                }
                is Result.Success -> {
                    Log.d(TAG, "createDoctorActiveSessionState: Success = ${it.data}")
                }
            }
        }
    }

    private fun createPayment(price: Double) {
        if(selectedMyPaymentMethod != null)  {
            MidtransSDK.getInstance().transactionRequest = initTransactionRequest(price)
            MidtransSDK.getInstance().startPaymentUiFlow(requireContext(), selectedMyPaymentMethod!!.paymentMethod)
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
                TransactionResult.STATUS_SUCCESS -> {
                    val response = result.response
                    val doctorActiveSession = DoctorActiveSession(
                        id = response.transactionId,
                        doctorId = doctor.id,
                        validUntil = DateHelper.getCurrentDateAfterMinutes(30),
                        createdAt = DateHelper.getCurrentDate(),
                        active = true
                    )
                    User.currentUser.id?.let { viewModel.createDoctorActiveSession(doctorActiveSession, it) }
                    Toast.makeText(requireContext(), getString(R.string.transaksi_berhasil), Toast.LENGTH_SHORT).show()
                }
                TransactionResult.STATUS_PENDING -> Toast.makeText(requireContext(), getString(R.string.transaksi_pending), Toast.LENGTH_SHORT).show()
                TransactionResult.STATUS_FAILED -> Toast.makeText(requireContext(), getString(R.string.transaksi_gagal), Toast.LENGTH_SHORT).show()
            }
            result.response.validationMessages
        } else if (result.isTransactionCanceled) {
            Toast.makeText(requireContext(), getString(R.string.transaksi_batal), Toast.LENGTH_SHORT).show()
        } else {
            if (result.status.equals(TransactionResult.STATUS_INVALID, true)) {
                Toast.makeText(requireContext(), "Transaction Invalid", Toast.LENGTH_LONG).show()
            } else {
                Toast.makeText(requireContext(), "Transaction Finished with failure.", Toast.LENGTH_LONG).show()
            }
        }
    }

    private fun initTransactionRequest(price: Double): TransactionRequest {
        // Create new Transaction Request
        val transactionRequestNew = TransactionRequest(System.currentTimeMillis().toString() + "", price)
        transactionRequestNew.customerDetails = initCustomerDetails()
        transactionRequestNew.gopay = Gopay("mysamplesdk:://midtrans")
        transactionRequestNew.shopeepay = Shopeepay("mysamplesdk:://midtrans")
        return transactionRequestNew
    }

    private fun initCustomerDetails(): CustomerDetails {
        //define customer detail (mandatory for coreflow)
        val mCustomerDetails = CustomerDetails()
        mCustomerDetails.phone = "085310102020"
        mCustomerDetails.firstName = User.currentUser.username
        mCustomerDetails.email = User.currentUser.email
        mCustomerDetails.customerIdentifier = "mail@mail.com"
        return mCustomerDetails
    }

    private companion object {
        const val TAG = "PaymentDoctorFragment"
    }

}