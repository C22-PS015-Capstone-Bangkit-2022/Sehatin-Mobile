package com.app.sehatin.data.remote.response

import android.os.Parcelable
import com.app.sehatin.data.model.Disease
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

@Parcelize
data class DiseaseResponse(
	@field:SerializedName("DiseaseResponse")
	val diseaseResponse: List<Disease>
) : Parcelable
