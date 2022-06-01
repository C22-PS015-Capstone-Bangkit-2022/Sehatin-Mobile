package com.app.sehatin.data.model

data class Chat(
    val id: String,
    val server_timestamp: String,
    val createdAt: String,
    val sender: String,
    val receiver: String,
    val message: String,
)