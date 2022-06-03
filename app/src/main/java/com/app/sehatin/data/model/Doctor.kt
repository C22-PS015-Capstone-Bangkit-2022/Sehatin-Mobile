package com.app.sehatin.data.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class Doctor(
    val id: String? = null,
    val name: String? = null,
    val strNumber: Long? = null,
    val specialist: String? = null,
    val experience_year: Int? = null,
    val alumnus: String? = null,
    val practice_at: String? = null,
    val price: Long? = null,
    val rating: Double? = null,
    val review: Int? = null,
    val imageUrl: String? = null,
) : Parcelable