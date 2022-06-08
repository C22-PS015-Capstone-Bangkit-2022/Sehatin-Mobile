package com.app.sehatin.data.model

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

@Parcelize
data class Exercise(
    @field:SerializedName("id_sport")
    val idSport: Int? = null,

    @field:SerializedName("activity")
    val name: String? = null,

    @field:SerializedName("category")
    val category: String? = null,

    @field:SerializedName("energy")
    val energy: Int? = null,

    @field:SerializedName("thumbnail_image")
    val thumbnail: String? = null
) : Parcelable