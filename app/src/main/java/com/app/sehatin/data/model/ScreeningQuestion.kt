package com.app.sehatin.data.model

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

@Parcelize
data class ScreeningQuestion(
    @field:SerializedName("id_pertanyaan")
    val id: Int,

    @field:SerializedName("pertanyaan")
    val question: String,

    var answer: String? = null
) : Parcelable {
    companion object {
        const val YES = "yes"
        const val NO = "no"
    }
}