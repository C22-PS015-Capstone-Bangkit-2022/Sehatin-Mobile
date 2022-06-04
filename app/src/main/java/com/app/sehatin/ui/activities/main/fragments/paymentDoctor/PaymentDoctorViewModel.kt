package com.app.sehatin.ui.activities.main.fragments.paymentDoctor

import androidx.lifecycle.ViewModel
import com.app.sehatin.data.model.PaymentMethod

class PaymentDoctorViewModel: ViewModel() {

    fun getPaymentMethod(): List<PaymentMethod> {
        return listOf(
            PaymentMethod(
                "GoPay",
                "https://marcas-logos.net/wp-content/uploads/2021/06/GoPay-logo-2048x1392.png"
            ),
            PaymentMethod(
                "ShopeePay",
                "https://www.steelytoe.com/static/assets/shopeepay-logo/1-shopeepay-rectangle-orange.png"
            ),
        )
    }

}