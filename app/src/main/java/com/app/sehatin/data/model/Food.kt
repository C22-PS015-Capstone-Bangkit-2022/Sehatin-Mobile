package com.app.sehatin.data.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class Food(
    var id_food: Long? = null,
    var nameId: String? = null,
    var nameEn: String? = null,
    var energy: Double? = null,
    var avg_portion: Long? = null,
    var fat: Double? = null,
    var protein: Double? = null,
    var carbs: Double? = null,
    var type_food: String? = null,
    var thumbnail_image: String? = null,
) : Parcelable