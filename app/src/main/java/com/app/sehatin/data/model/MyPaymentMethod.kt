package com.app.sehatin.data.model

import com.midtrans.sdk.corekit.core.PaymentMethod

data class MyPaymentMethod(
    val name: String,
    val imageUrl: String,
    val paymentMethod: PaymentMethod
)