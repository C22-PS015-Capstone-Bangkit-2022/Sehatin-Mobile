package com.app.sehatin.data.model

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

@Parcelize
data class Disease(
    @field:SerializedName("screening_questions")
    val screeningQuestions: List<ScreeningQuestion>,

    @field:SerializedName("nama_penyakit")
    val diseaseName: String,

    @field:SerializedName("id_penyakit")
    val id: Int
) : Parcelable