package com.app.sehatin.data.model

data class HistoryChat(
    val id: String? = null,
    val createdAt: String? = null,
    val sender: String? = null,
    val receiver: String? = null,
    val message: String? = null,
    val withUser: String? = null,
    val read: Boolean? = null,
)