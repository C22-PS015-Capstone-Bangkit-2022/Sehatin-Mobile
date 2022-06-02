package com.app.sehatin.data.model

data class HistoryChat(
    val id: String? = null,
    val server_timestamp: String? = null,
    val createdAt: String? = null,
    val sender: String? = null,
    val receiver: String? = null,
    val message: String? = null,
    val withUser: String? = null,
) {
    companion object {
        const val CREATED_AT = "createdAt"
    }
}