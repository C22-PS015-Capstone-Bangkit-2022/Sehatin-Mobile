package com.app.sehatin.ui.activities.main.fragments.paymentDoctor

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.app.sehatin.data.Result
import com.app.sehatin.data.model.DoctorActiveSession
import com.app.sehatin.data.model.MyPaymentMethod
import com.app.sehatin.data.repository.DoctorSessionRepository
import com.app.sehatin.data.repository.UserRepository
import com.midtrans.sdk.corekit.core.PaymentMethod

class PaymentDoctorViewModel(private val doctorSessionRepository: DoctorSessionRepository): ViewModel() {

    val createDoctorActiveSessionState = MutableLiveData<Result<DoctorActiveSession>>()
    fun createDoctorActiveSession(doctorActiveSession: DoctorActiveSession, userId: String) = doctorSessionRepository.createDoctorActiveSession(createDoctorActiveSessionState, doctorActiveSession, userId)

    fun getPaymentMethod(): List<MyPaymentMethod> {
        return listOf(
            MyPaymentMethod(
                "GoPay",
                "https://firebasestorage.googleapis.com/v0/b/sehatin-eab72.appspot.com/o/OTHER%2FGoPay-logo-2048x1392.png?alt=media&token=82b63b90-29a5-48db-b1b1-dbbd208735ac",
                PaymentMethod.GO_PAY
            ),
            MyPaymentMethod(
                "ShopeePay",
                "https://firebasestorage.googleapis.com/v0/b/sehatin-eab72.appspot.com/o/OTHER%2Fshopee_pay.png?alt=media&token=85957418-8816-4789-8e8b-c4c2248b93da",
                PaymentMethod.SHOPEEPAY
            ),
        )
    }

}