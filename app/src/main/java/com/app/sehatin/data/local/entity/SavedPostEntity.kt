package com.app.sehatin.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = SavedPostEntity.TABLE_NAME)
data class SavedPostEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Int,
    val postId: String,
    val createdAt: String,
) {
    companion object {
        const val TABLE_NAME = "saved_post"
    }
}