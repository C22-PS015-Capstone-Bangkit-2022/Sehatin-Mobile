package com.app.sehatin.data.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class Food(
    var id_food: Long? = null,
    var name: String? = null,
    var thumbnail: String? = null,
    var energy: Double? = null,
    var avg_portion: Long? = null,
    var fat: Double? = null,
    var protein: Double? = null,
    var carbs: Double? = null,
    var type_food: String? = null,
) : Parcelable