package com.app.sehatin.data.remote.response

import android.os.Parcelable
import com.app.sehatin.data.model.Food
import kotlinx.parcelize.Parcelize

@Parcelize
data class FoodResponse(
    var ok: Boolean? = null,
    var message: String? = null,
    var error: String? = null,
    var food: List<Food>? = null,
) : Parcelable