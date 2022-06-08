package com.app.sehatin.data.remote.response

import android.os.Parcelable
import com.app.sehatin.data.model.Exercise
import kotlinx.parcelize.Parcelize

@Parcelize
data class ExerciseResponse(
	val ok: Boolean? = null,
	val message: String? = null,
	val error: String? = null,
	val sport: List<Exercise>? = null
) : Parcelable