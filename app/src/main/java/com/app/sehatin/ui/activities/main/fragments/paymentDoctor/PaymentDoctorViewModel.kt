package com.app.sehatin.ui.activities.main.fragments.paymentDoctor

import androidx.lifecycle.ViewModel
import com.app.sehatin.data.model.MyPaymentMethod
import com.midtrans.sdk.corekit.core.PaymentMethod

class PaymentDoctorViewModel: ViewModel() {

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