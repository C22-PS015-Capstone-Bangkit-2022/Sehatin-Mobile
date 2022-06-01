package com.app.sehatin.data.model

data class HistoryChat(
    val id: String,
    val createdAt: String,
    val sender: String,
    val receiver: String,
    val message: String,
    val withUser: String,
) {
    companion object {
        const val CREATED_AT = "createdAt"
    }
}