package com.app.sehatin.data.remote.response

import android.os.Parcelable
import com.app.sehatin.data.model.Article
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

@Parcelize
data class ArticlesResponse(

	@field:SerializedName("totalItems")
	val totalItems: Int? = null,

	@field:SerializedName("totalPages")
	val totalPages: Int? = null,

	@field:SerializedName("currentPage")
	val currentPage: Int? = null,

	@field:SerializedName("articles")
	val articles: List<Article>? = null
) : Parcelable
