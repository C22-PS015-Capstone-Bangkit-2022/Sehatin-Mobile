package com.app.sehatin.data.model

data class HistoryChat(
    val id: String? = null,
    val createdAt: String? = null,
    val sender: String? = null,
    val receiver: String? = null,
    val message: String? = null,
    var withUser: String? = null,
    val read: Boolean? = null,
    val forDoctor: Boolean? = null,
)