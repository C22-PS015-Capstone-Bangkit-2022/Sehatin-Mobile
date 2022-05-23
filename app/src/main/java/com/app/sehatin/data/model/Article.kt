package com.app.sehatin.data.model

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

@Parcelize
data class Article(
    @field:SerializedName("isi_artikel")
    val content: String? = null,

    @field:SerializedName("created_at")
    val createdAt: String? = null,

    @field:SerializedName("tag")
    val tags: List<String>? = null,

    @field:SerializedName("source")
    val source: String? = null,

    @field:SerializedName("id_artikel")
    val id: Int? = null,

    @field:SerializedName("judul")
    val title: String? = null,

    @field:SerializedName("thumbnail_image")
    val thumbnail: String? = null
) : Parcelable